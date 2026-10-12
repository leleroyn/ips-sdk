use sha2::{Digest, Sha256};
use std::path::PathBuf;

fn hex_encode(bytes: &[u8]) -> String {
    bytes.iter().map(|b| format!("{b:02x}")).collect()
}

pub fn file_sha256(bytes: &[u8]) -> String {
    let mut hasher = Sha256::new();
    hasher.update(bytes);
    hex_encode(&hasher.finalize())
}

/// 缓存键 = SHA-256(command | path | 按序表单字段 | 文件身份)
/// 文件身份: 本地文件为 "sha256:<内容哈希>"; 远程 URL 为 "url:<url>|etag:...|lm:..."
pub fn cache_key(command: &str, path: &str, fields: &[(String, String)], identity: &str) -> String {
    let mut hasher = Sha256::new();
    hasher.update(command.as_bytes());
    hasher.update([0u8]);
    hasher.update(path.as_bytes());
    hasher.update([0u8]);
    for (k, v) in fields {
        hasher.update(k.as_bytes());
        hasher.update([0u8]);
        hasher.update(v.as_bytes());
        hasher.update([0u8]);
    }
    hasher.update(identity.as_bytes());
    hex_encode(&hasher.finalize())
}

/// 缓存目录: IPS_CACHE_DIR 优先, 否则 ~/.ips/cache
pub fn resolve_cache_dir() -> Option<PathBuf> {
    if let Ok(dir) = std::env::var("IPS_CACHE_DIR") {
        let dir = dir.trim();
        if !dir.is_empty() {
            return Some(PathBuf::from(dir));
        }
    }
    let home = std::env::var("HOME")
        .or_else(|_| std::env::var("USERPROFILE"))
        .ok()?;
    Some(PathBuf::from(home).join(".ips").join("cache"))
}

fn cache_path(dir: &std::path::Path, key: &str) -> PathBuf {
    dir.join(format!("{key}.json"))
}

/// 读取缓存; 不存在/读失败/空内容均视为未命中
pub fn read(dir: &std::path::Path, key: &str) -> Option<String> {
    let text = std::fs::read_to_string(cache_path(dir, key)).ok()?;
    if text.is_empty() {
        return None;
    }
    Some(text)
}

/// 原子写入缓存 (临时文件 + rename)
pub fn write(dir: &std::path::Path, key: &str, content: &str) -> std::io::Result<()> {
    std::fs::create_dir_all(dir)?;
    let final_path = cache_path(dir, key);
    let tmp_path = dir.join(format!("{key}.json.tmp.{}", std::process::id()));
    std::fs::write(&tmp_path, content)?;
    #[cfg(windows)]
    {
        // Windows 下 rename 不能覆盖已存在文件, 先删再改名
        let _ = std::fs::remove_file(&final_path);
    }
    std::fs::rename(&tmp_path, &final_path)
}

#[cfg(test)]
mod tests {
    use super::*;

    fn temp_dir(tag: &str) -> PathBuf {
        let dir = std::env::temp_dir().join(format!("ips-cache-test-{tag}-{}", std::process::id()));
        let _ = std::fs::remove_dir_all(&dir);
        dir
    }

    #[test]
    fn key_stable_and_sensitive() {
        let id_a = format!("sha256:{}", file_sha256(b"image-a"));
        let id_b = format!("sha256:{}", file_sha256(b"image-b"));
        let fields = vec![("return_ocr_text".to_string(), "false".to_string())];

        let k1 = cache_key("idcard", "/api/v3/idcard/preprocess", &fields, &id_a);
        let k2 = cache_key("idcard", "/api/v3/idcard/preprocess", &fields, &id_a);
        assert_eq!(k1, k2, "相同输入键必须稳定");

        let k3 = cache_key("idcard", "/api/v3/idcard/preprocess", &fields, &id_b);
        assert_ne!(k1, k3, "文件内容变化键必须变化");

        let fields2 = vec![("init_confidence".to_string(), "0.7".to_string())];
        let k4 = cache_key("idcard", "/api/v3/idcard/preprocess", &fields2, &id_a);
        assert_ne!(k1, k4, "参数变化键必须变化");

        let k5 = cache_key("seal", "/api/v3/seal/preprocess", &fields, &id_a);
        assert_ne!(k1, k5, "命令不同键必须变化");

        let url_id1 = "url:http://h/a.jpg|etag:W/\"1\"".to_string();
        let url_id2 = "url:http://h/a.jpg|etag:W/\"2\"".to_string();
        let k6 = cache_key("idcard", "/api/v3/idcard/preprocess", &fields, &url_id1);
        let k7 = cache_key("idcard", "/api/v3/idcard/preprocess", &fields, &url_id2);
        assert_ne!(k6, k7, "URL 内容校验头变化键必须变化");
    }

    #[test]
    fn write_read_roundtrip() {
        let dir = temp_dir("roundtrip");
        write(&dir, "k1", "{\"ok\":true}").unwrap();
        assert_eq!(read(&dir, "k1").as_deref(), Some("{\"ok\":true}"));
        write(&dir, "k1", "{\"ok\":false}").unwrap();
        assert_eq!(read(&dir, "k1").as_deref(), Some("{\"ok\":false}"));
        let _ = std::fs::remove_dir_all(&dir);
    }

    #[test]
    fn missing_or_empty_cache_is_miss() {
        let dir = temp_dir("miss");
        assert!(read(&dir, "nope").is_none());
        std::fs::create_dir_all(&dir).unwrap();
        std::fs::write(dir.join("empty.json"), "").unwrap();
        assert!(read(&dir, "empty").is_none());
        let _ = std::fs::remove_dir_all(&dir);
    }
}