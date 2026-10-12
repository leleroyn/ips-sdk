mod api;
mod cache;
mod cli;
mod source;

use anyhow::{bail, Context, Result};
use clap::Parser;
use cli::{Cli, Command};
use std::process::ExitCode;

#[tokio::main]
async fn main() -> ExitCode {
    match run().await {
        Ok(()) => ExitCode::SUCCESS,
        Err(e) => {
            eprintln!("错误: {e:#}");
            ExitCode::FAILURE
        }
    }
}

async fn run() -> Result<()> {
    let cli = Cli::parse();
    let base_url = resolve_base_url()?;
    let client = reqwest::Client::builder()
        .timeout(std::time::Duration::from_secs_f64(cli.timeout))
        .build()
        .context("构建 HTTP 客户端失败")?;

    match cli.command {
        Command::Idcard(args) => upload(&client, &base_url, "idcard", api::idcard(&args)?, &args.file, cli.no_cache).await,
        Command::Bizlic(args) => upload(&client, &base_url, "bizlic", api::bizlic(&args)?, &args.file, cli.no_cache).await,
        Command::Invoice(args) => upload(&client, &base_url, "invoice", api::invoice(&args)?, &args.file, cli.no_cache).await,
        Command::Seal(args) => upload(&client, &base_url, "seal", api::seal(&args)?, &args.file, cli.no_cache).await,
        Command::Ocr(args) => upload(&client, &base_url, "ocr", api::ocr(&args)?, &args.file, cli.no_cache).await,
        Command::Card(args) => upload(&client, &base_url, "card", api::card(&args)?, &args.file, cli.no_cache).await,
        Command::Handprint(args) => upload(&client, &base_url, "handprint", api::handprint(&args)?, &args.file, cli.no_cache).await,
    }
}

fn resolve_base_url() -> Result<String> {
    let raw = std::env::var("IPS_BASE_URL").map_err(|_| {
        anyhow::anyhow!("环境变量 IPS_BASE_URL 未设置, 请先设置 (PowerShell: $env:IPS_BASE_URL=\"http://localhost:8000\"; Linux: export IPS_BASE_URL=http://localhost:8000)")
    })?;
    let trimmed = raw.trim().trim_end_matches('/').to_string();
    if !(trimmed.starts_with("http://") || trimmed.starts_with("https://")) {
        bail!("IPS_BASE_URL 无效: {raw} (必须以 http:// 或 https:// 开头)");
    }
    Ok(trimmed)
}

/// 文件身份: 本地文件用内容哈希; 远程 URL 用 URL + HEAD 校验头 (etag/last-modified), 避免下载
async fn file_identity(client: &reqwest::Client, src: &source::FileSource) -> String {
    match src {
        source::FileSource::Local(path) => match std::fs::read(path) {
            Ok(bytes) => format!("sha256:{}", cache::file_sha256(&bytes)),
            Err(_) => format!("path:{}", path.display()),
        },
        source::FileSource::Url(url) => {
            let mut id = format!("url:{url}");
            if let Ok(resp) = client.head(url).send().await {
                if resp.status().is_success() {
                    let headers = resp.headers();
                    if let Some(etag) = headers.get("etag").and_then(|v| v.to_str().ok()) {
                        id.push_str("|etag:");
                        id.push_str(etag);
                    }
                    if let Some(lm) = headers.get("last-modified").and_then(|v| v.to_str().ok()) {
                        id.push_str("|lm:");
                        id.push_str(lm);
                    }
                }
            }
            id
        }
    }
}

async fn upload(
    client: &reqwest::Client,
    base_url: &str,
    command: &str,
    req: api::ApiRequest,
    file: &str,
    no_cache: bool,
) -> Result<()> {
    let src = source::parse_source(file);
    let cache_dir = cache::resolve_cache_dir();

    // 本地文件先读入 (便宜); URL 延迟到确认未命中后再下载
    let local_content = match &src {
        source::FileSource::Local(_) => Some(source::load(&src, client).await?),
        source::FileSource::Url(_) => None,
    };
    let identity = file_identity(client, &src).await;
    let key = cache::cache_key(command, &req.path, &req.fields, &identity);

    if !no_cache {
        if let Some(dir) = &cache_dir {
            if let Some(text) = cache::read(dir, &key) {
                print_body(&text)?;
                return Ok(());
            }
        }
    }

    let content = match local_content {
        Some(c) => c,
        None => source::load(&src, client).await?,
    };
    let part = reqwest::multipart::Part::bytes(content.bytes)
        .file_name(content.filename)
        .mime_str(&content.content_type)
        .context("构建上传文件失败")?;
    let mut form = reqwest::multipart::Form::new().part("file", part);
    for (k, v) in &req.fields {
        form = form.text(k.clone(), v.clone());
    }
    let url = format!("{base_url}{}", req.path);
    let resp = client
        .post(&url)
        .multipart(form)
        .send()
        .await
        .with_context(|| format!("请求失败: {url}"))?;

    let status = resp.status();
    let text = resp.text().await.context("读取响应失败")?;
    if !status.is_success() {
        bail!("API 错误: HTTP {} {}", status.as_u16(), text);
    }

    // 仅缓存成功响应; 写失败降级为警告, 不影响命令结果
    if let Some(dir) = &cache_dir {
        if let Err(e) = cache::write(dir, &key, &text) {
            eprintln!("警告: 写入缓存失败: {e}");
        }
    }
    print_body(&text)
}

/// 命中与首次调用走同一渲染路径, 保证 stdout 字节一致
fn print_body(text: &str) -> Result<()> {
    match serde_json::from_str::<serde_json::Value>(text) {
        Ok(v) => println!("{}", serde_json::to_string_pretty(&v)?),
        Err(_) => println!("{text}"),
    }
    Ok(())
}