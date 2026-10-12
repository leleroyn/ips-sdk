use clap::{Parser, Subcommand};

#[derive(Parser)]
#[command(
    name = "ips",
    version,
    about = "IPS 综合服务平台命令行工具 (OCR / 证件检测 / 印章识别)"
)]
pub struct Cli {
    /// 请求超时时间 (秒)
    #[arg(long, global = true, default_value_t = 30.0, value_name = "SECONDS")]
    pub timeout: f64,

    /// 跳过结果缓存, 强制请求服务器 (成功结果仍会写入缓存)
    #[arg(long, global = true)]
    pub no_cache: bool,

    #[command(subcommand)]
    pub command: Command,
}

#[derive(Subcommand)]
pub enum Command {
    /// 身份证识别
    Idcard(IdcardArgs),
    /// 营业执照识别
    Bizlic(BizlicArgs),
    /// 发票识别
    Invoice(InvoiceArgs),
    /// 印章识别
    Seal(SealArgs),
    /// 通用 OCR
    Ocr(OcrArgs),
    /// 证件检测
    Card(CardArgs),
    /// 手印检测
    Handprint(HandprintArgs),
}

#[derive(Parser)]
pub struct IdcardArgs {
    /// 图片/文件路径, 支持本地路径或 http(s) URL
    #[arg(value_name = "FILE")]
    pub file: String,

    /// 初始置信度阈值
    #[arg(long, value_name = "FLOAT")]
    pub init_confidence: Option<f64>,
}

#[derive(Parser)]
pub struct BizlicArgs {
    #[arg(value_name = "FILE")]
    pub file: String,

    #[arg(long, value_name = "FLOAT")]
    pub init_confidence: Option<f64>,
}

#[derive(Parser)]
pub struct InvoiceArgs {
    #[arg(value_name = "FILE")]
    pub file: String,

    #[arg(long, value_name = "FLOAT")]
    pub init_confidence: Option<f64>,
}

#[derive(Parser)]
pub struct SealArgs {
    #[arg(value_name = "FILE")]
    pub file: String,

    #[arg(long, value_name = "FLOAT")]
    pub init_confidence: Option<f64>,
}

#[derive(Parser)]
pub struct OcrArgs {
    #[arg(value_name = "FILE")]
    pub file: String,

    /// 输出格式
    #[arg(long, value_name = "FMT")]
    pub format: Option<String>,
}

#[derive(Parser)]
pub struct CardArgs {
    #[arg(value_name = "FILE")]
    pub file: String,
}

#[derive(Parser)]
pub struct HandprintArgs {
    #[arg(value_name = "FILE")]
    pub file: String,

    /// 位置参数
    #[arg(long, value_name = "POS")]
    pub position: Option<String>,
}
#[cfg(test)]
mod tests {
    use super::Cli;
    use clap::Parser;

    #[test]
    fn health_command_removed() {
        assert!(Cli::try_parse_from(["ips", "health"]).is_err());
        assert!(Cli::try_parse_from(["ips", "idcard", "a.jpg", "--no-cache"]).is_ok());
    }
}