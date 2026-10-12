# IPS CLI (Rust)

IPS 综合服务平台命令行工具, 命令名 `ips`。每个功能固定使用最新接口版本。

## 构建

```powershell
cd rust
cargo build --release
# 可执行文件: target/release/ips.exe
```

> 注意: 本机 MinGW 的 dlltool 不支持中文路径, 若源码位于中文目录,
> 请指定纯 ASCII 的 target 目录:
> `$env:CARGO_TARGET_DIR = "D:\Tools\ips-rust-target"`

## 配置

接口基础地址由环境变量 `IPS_BASE_URL` 提供:

```powershell
$env:IPS_BASE_URL = "http://localhost:8000"
```

也可用全局参数 `--base-url` 临时覆盖。

## 用法

```
ips <二级命令> <文件路径> [选项]
```

`<文件路径>` 支持本地路径与远程 URL (http/https), URL 会先下载再上传。

### 二级命令

| 命令 | 说明 | 接口 |
|------|------|------|
| `idcard` | 身份证识别 | POST /api/v3/idcard/preprocess |
| `bizlic` | 营业执照识别 | POST /api/v3/bizlic/preprocess |
| `invoice` | 发票识别 | POST /api/v3/invoice/preprocess |
| `seal` | 印章识别 | POST /api/v3/seal/preprocess |
| `ocr` | 通用 OCR | POST /api/v3/ocr/preprocess |
| `card` | 证件检测 | POST /api/v3/card/det |
| `handprint` | 手印检测 | POST /api/v2/handprint/detect |
| `health` | 健康检查 | GET /health |

### 常用选项

- `--return-ocr-text` / `--no-return-ocr-text`: 是否返回 OCR 文本
- `--init-confidence <float>`: 初始置信度
- `--resize` / `--no-resize`: 自动缩放
- `--back-ground` / `--no-back-ground`: 背景增强
- `--app-id <id>` / `--use-cls` / `--format <fmt>`: OCR 专属
- `--position <pos>`: 手印检测位置
- `--timeout <秒>`: 请求超时 (默认 30)
- `--base-url <url>`: 覆盖 IPS_BASE_URL

未显式指定的参数不发送, 由服务端默认值决定。

裁剪图一律不返回: CLI 对 idcard/bizlic/invoice/card 固定发送 `return_corp_image=false`,
对 seal 固定发送 `return_seal_image=false`。

### 示例

```powershell
$env:IPS_BASE_URL = "http://localhost:8000"

# 本地文件
ips idcard D:\images\idcard.jpg

# 远程 URL
ips idcard https://example.com/idcard.png --return-ocr-text

# 印章识别
ips seal photo.jpg --return-seal-image --init-confidence 0.7

# OCR
ips ocr doc.pdf --app-id myapp --format text

# 健康检查
ips health
```

成功时输出格式化 JSON; 失败时打印错误并以非零退出码结束。

## 测试

```powershell
cargo test --release
```