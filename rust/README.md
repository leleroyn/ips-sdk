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

接口基础地址仅由环境变量 `IPS_BASE_URL` 提供:

```powershell
$env:IPS_BASE_URL = "http://localhost:8000"
```

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

### 选项

- `--init-confidence <float>`: 初始置信度 (idcard/bizlic/invoice/seal)
- `--format <fmt>`: OCR 输出格式
- `--position <pos>`: 手印检测位置
- `--timeout <秒>`: 请求超时 (默认 30)
- `--no-cache`: 跳过缓存强制请求服务器 (成功结果仍写入缓存)

固定行为 (不暴露为参数):
- 裁剪图一律不返回: idcard/bizlic/invoice/card 发送 `return_corp_image=false`, seal 发送 `return_seal_image=false`
- OCR 文本固定返回: idcard/bizlic/invoice/seal 发送 `return_ocr_text=true`
- 其余参数不发送, 由服务端默认值决定

### 示例

```powershell
$env:IPS_BASE_URL = "http://localhost:8000"

# 本地文件
ips idcard D:\images\idcard.jpg

# 远程 URL
ips idcard https://example.com/idcard.png

# 指定置信度阈值
ips seal photo.jpg --init-confidence 0.7

# OCR
ips ocr doc.pdf --format text

```

成功时输出格式化 JSON; 失败时打印错误并以非零退出码结束。

## 测试

```powershell
cargo test --release
```

## Linux / Windows 安装 (GitHub Actions)

推送 `rust/**` 变更或打 `v*` 标签会触发 `.github/workflows/build-linux-cli.yml`,
产出两个二进制: `ips` (Linux x86_64, musl 静态链接, 任意发行版可直接运行) 和 `ips.exe` (Windows x86_64):

- 最新构建: Actions 页面 artifacts `ips-linux-x86_64` / `ips-windows-x86_64`
- 正式发布: [Releases](https://github.com/leleroyn/ips-sdk/releases) 中的 `ips`

```bash
curl -fsSL -o ips https://github.com/leleroyn/ips-sdk/releases/download/v1.0.2/ips
chmod +x ips
export IPS_BASE_URL=http://112.94.31.26:18080/ips
./ips idcard /path/to/idcard.jpg
```
Windows (PowerShell):

```powershell
Invoke-WebRequest -Uri https://github.com/leleroyn/ips-sdk/releases/download/v1.0.2/ips.exe -OutFile ips.exe
$env:IPS_BASE_URL = "http://112.94.31.26:18080/ips"
.\ips.exe idcard D:\images\idcard.jpg
```
## 幂等缓存

相同 命令 + 参数 + 文件内容 的调用只请求服务器一次, 重复调用直接返回缓存结果 (输出与首次完全一致):

- 缓存目录: 默认 `~/.ips/cache`, 环境变量 `IPS_CACHE_DIR` 可覆盖; 手动清理即删该目录
- 缓存键: 命令 + 接口路径 + 表单参数 + 文件身份
  - 本地文件: 内容 SHA-256
  - 远程 URL: URL + HEAD 请求的 ETag/Last-Modified (不下载文件; 无校验头时退化为纯 URL)
- 永不过期; 仅成功响应入缓存; `--no-cache` 强制走服务器