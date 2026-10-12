use clap::{ArgAction, Parser, Subcommand};

#[derive(Parser)]
#[command(
    name = "ips",
    version,
    about = "IPS 综合服务平台命令行工具 (OCR / 证件检测 / 印章识别)"
)]
pub struct Cli {
    /// IPS 接口基础地址, 默认读取环境变量 IPS_BASE_URL
    #[arg(long, global = true, value_name = "URL")]
    pub base_url: Option<String>,

    /// 请求超时时间 (秒)
    #[arg(long, global = true, default_value_t = 30.0, value_name = "SECONDS")]
    pub timeout: f64,

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
    /// 服务健康检查
    Health,
}

#[derive(Parser)]
pub struct IdcardArgs {
    /// 图片/文件路径, 支持本地路径或 http(s) URL
    #[arg(value_name = "FILE")]
    pub file: String,

    /// 返回 OCR 文本
    #[arg(long)]
    pub return_ocr_text: bool,
    #[arg(long = "no-return-ocr-text", action = ArgAction::SetTrue)]
    pub no_return_ocr_text: bool,

    /// 初始置信度阈值
    #[arg(long, value_name = "FLOAT")]
    pub init_confidence: Option<f64>,

    /// 自动缩放
    #[arg(long)]
    pub resize: bool,
    #[arg(long = "no-resize", action = ArgAction::SetTrue)]
    pub no_resize: bool,

    /// 背景增强
    #[arg(long)]
    pub back_ground: bool,
    #[arg(long = "no-back-ground", action = ArgAction::SetTrue)]
    pub no_back_ground: bool,
}

#[derive(Parser)]
pub struct BizlicArgs {
    #[arg(value_name = "FILE")]
    pub file: String,

    #[arg(long)]
    pub return_ocr_text: bool,
    #[arg(long = "no-return-ocr-text", action = ArgAction::SetTrue)]
    pub no_return_ocr_text: bool,

    #[arg(long, value_name = "FLOAT")]
    pub init_confidence: Option<f64>,

    #[arg(long)]
    pub resize: bool,
    #[arg(long = "no-resize", action = ArgAction::SetTrue)]
    pub no_resize: bool,

    #[arg(long)]
    pub back_ground: bool,
    #[arg(long = "no-back-ground", action = ArgAction::SetTrue)]
    pub no_back_ground: bool,
}

#[derive(Parser)]
pub struct InvoiceArgs {
    #[arg(value_name = "FILE")]
    pub file: String,

    #[arg(long)]
    pub return_ocr_text: bool,
    #[arg(long = "no-return-ocr-text", action = ArgAction::SetTrue)]
    pub no_return_ocr_text: bool,

    #[arg(long, value_name = "FLOAT")]
    pub init_confidence: Option<f64>,

    #[arg(long)]
    pub resize: bool,
    #[arg(long = "no-resize", action = ArgAction::SetTrue)]
    pub no_resize: bool,

    #[arg(long)]
    pub back_ground: bool,
    #[arg(long = "no-back-ground", action = ArgAction::SetTrue)]
    pub no_back_ground: bool,
}

#[derive(Parser)]
pub struct SealArgs {
    #[arg(value_name = "FILE")]
    pub file: String,

    #[arg(long)]
    pub return_ocr_text: bool,
    #[arg(long = "no-return-ocr-text", action = ArgAction::SetTrue)]
    pub no_return_ocr_text: bool,

    #[arg(long, value_name = "FLOAT")]
    pub init_confidence: Option<f64>,

    #[arg(long)]
    pub resize: bool,
    #[arg(long = "no-resize", action = ArgAction::SetTrue)]
    pub no_resize: bool,

    #[arg(long)]
    pub back_ground: bool,
    #[arg(long = "no-back-ground", action = ArgAction::SetTrue)]
    pub no_back_ground: bool,
}

#[derive(Parser)]
pub struct OcrArgs {
    #[arg(value_name = "FILE")]
    pub file: String,

    /// 应用 ID
    #[arg(long, value_name = "ID")]
    pub app_id: Option<String>,

    /// 自动缩放
    #[arg(long)]
    pub resize: bool,
    #[arg(long = "no-resize", action = ArgAction::SetTrue)]
    pub no_resize: bool,

    /// 方向分类
    #[arg(long)]
    pub use_cls: bool,
    #[arg(long = "no-use-cls", action = ArgAction::SetTrue)]
    pub no_use_cls: bool,

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

/// 将 (flag, no-flag) 组合转换为可选布尔值
pub fn tri(on: bool, off: bool, name: &str) -> anyhow::Result<Option<bool>> {
    match (on, off) {
        (true, true) => anyhow::bail!("参数 --{name} 与 --no-{name} 不能同时使用"),
        (true, false) => Ok(Some(true)),
        (false, true) => Ok(Some(false)),
        _ => Ok(None),
    }
}

#[cfg(test)]
mod tests {
    use super::tri;

    #[test]
    fn tri_combos() {
        assert_eq!(tri(true, false, "x").unwrap(), Some(true));
        assert_eq!(tri(false, true, "x").unwrap(), Some(false));
        assert_eq!(tri(false, false, "x").unwrap(), None);
        assert!(tri(true, true, "x").is_err());
    }
}