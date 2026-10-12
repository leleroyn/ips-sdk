use anyhow::{bail, Context, Result};
use std::path::PathBuf;

/// 输入文件来源: 本地路径或远程 URL
pub enum FileSource {
    Local(PathBuf),
    Url(String),
}

pub fn parse_source(input: &str) -> FileSource {
    if input.starts_with("http://") || input.starts_with("https://") {
        FileSource::Url(input.to_string())
    } else {
        FileSource::Local(PathBuf::from(input))
    }
}

pub struct FileContent {
    pub bytes: Vec<u8>,
    pub filename: String,
    pub content_type: String,
}

fn guess_content_type(filename: &str) -> &'static str {
    let ext = filename.rsplit('.').next().unwrap_or("").to_lowercase();
    match ext.as_str() {
        "jpg" | "jpeg" => "image/jpeg",
        "png" => "image/png",
        "gif" => "image/gif",
        "pdf" => "application/pdf",
        "bmp" => "image/bmp",
        "webp" => "image/webp",
        _ => "application/octet-stream",
    }
}

fn filename_from_url(url: &str) -> String {
    let path = url
        .split_once("://")
        .map(|(_, rest)| rest)
        .unwrap_or(url);
    let path = path.split(['?', '#']).next().unwrap_or(path);
    let name = path.rsplit('/').next().unwrap_or("");
    if name.is_empty() {
        "file".to_string()
    } else {
        name.to_string()
    }
}

pub async fn load(source: &FileSource, client: &reqwest::Client) -> Result<FileContent> {
    match source {
        FileSource::Local(path) => {
            if !path.exists() {
                bail!("文件不存在: {}", path.display());
            }
            let bytes = std::fs::read(path)
                .with_context(|| format!("读取文件失败: {}", path.display()))?;
            let filename = path
                .file_name()
                .map(|n| n.to_string_lossy().into_owned())
                .unwrap_or_else(|| "file".to_string());
            let content_type = guess_content_type(&filename).to_string();
            Ok(FileContent { bytes, filename, content_type })
        }
        FileSource::Url(url) => {
            let resp = client
                .get(url)
                .send()
                .await
                .with_context(|| format!("下载失败: {url}"))?;
            let status = resp.status();
            if !status.is_success() {
                bail!("下载失败: {url} (HTTP {})", status.as_u16());
            }
            let bytes = resp.bytes().await.context("下载中断")?;
            let filename = filename_from_url(url);
            let content_type = guess_content_type(&filename).to_string();
            Ok(FileContent { bytes: bytes.to_vec(), filename, content_type })
        }
    }
}
#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn parse_source_url_vs_local() {
        assert!(matches!(parse_source("http://a.com/x.jpg"), FileSource::Url(_)));
        assert!(matches!(parse_source("https://a.com/x.jpg"), FileSource::Url(_)));
        assert!(matches!(parse_source("C:\\x\\y.jpg"), FileSource::Local(_)));
        assert!(matches!(parse_source("./rel.png"), FileSource::Local(_)));
    }

    #[test]
    fn filename_from_url_strips_query() {
        assert_eq!(filename_from_url("http://h/a/b/photo.jpg?x=1#f"), "photo.jpg");
        assert_eq!(filename_from_url("https://h/a/b/"), "file");
        assert_eq!(filename_from_url("https://h/"), "file");
    }

    #[test]
    fn mime_guess() {
        assert_eq!(guess_content_type("a.JPG"), "image/jpeg");
        assert_eq!(guess_content_type("a.pdf"), "application/pdf");
        assert_eq!(guess_content_type("a"), "application/octet-stream");
    }
}