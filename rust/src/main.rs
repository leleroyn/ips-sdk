mod api;
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
    let base_url = resolve_base_url(&cli)?;
    let client = reqwest::Client::builder()
        .timeout(std::time::Duration::from_secs_f64(cli.timeout))
        .build()
        .context("构建 HTTP 客户端失败")?;

    match cli.command {
        Command::Health => {
            let url = format!("{base_url}/health");
            let resp = client
                .get(&url)
                .send()
                .await
                .with_context(|| format!("请求失败: {url}"))?;
            print_response(resp).await
        }
        Command::Idcard(args) => upload(&client, &base_url, api::idcard(&args)?, &args.file).await,
        Command::Bizlic(args) => upload(&client, &base_url, api::bizlic(&args)?, &args.file).await,
        Command::Invoice(args) => upload(&client, &base_url, api::invoice(&args)?, &args.file).await,
        Command::Seal(args) => upload(&client, &base_url, api::seal(&args)?, &args.file).await,
        Command::Ocr(args) => upload(&client, &base_url, api::ocr(&args)?, &args.file).await,
        Command::Card(args) => upload(&client, &base_url, api::card(&args)?, &args.file).await,
        Command::Handprint(args) => {
            upload(&client, &base_url, api::handprint(&args)?, &args.file).await
        }
    }
}

fn resolve_base_url(cli: &Cli) -> Result<String> {
    let raw = match (&cli.base_url, std::env::var("IPS_BASE_URL")) {
        (Some(u), _) => u.clone(),
        (None, Ok(u)) => u,
        (None, Err(_)) => bail!(
            "环境变量 IPS_BASE_URL 未设置, 请先设置 (PowerShell: $env:IPS_BASE_URL=\"http://localhost:8000\") 或使用 --base-url 参数"
        ),
    };
    let trimmed = raw.trim().trim_end_matches('/').to_string();
    if !(trimmed.starts_with("http://") || trimmed.starts_with("https://")) {
        bail!("IPS_BASE_URL 无效: {raw} (必须以 http:// 或 https:// 开头)");
    }
    Ok(trimmed)
}

async fn upload(
    client: &reqwest::Client,
    base_url: &str,
    req: api::ApiRequest,
    file: &str,
) -> Result<()> {
    let src = source::parse_source(file);
    let content = source::load(&src, client).await?;
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
    print_response(resp).await
}

async fn print_response(resp: reqwest::Response) -> Result<()> {
    let status = resp.status();
    let text = resp.text().await.context("读取响应失败")?;
    if !status.is_success() {
        bail!("API 错误: HTTP {} {}", status.as_u16(), text);
    }
    match serde_json::from_str::<serde_json::Value>(&text) {
        Ok(v) => println!("{}", serde_json::to_string_pretty(&v)?),
        Err(_) => println!("{text}"),
    }
    Ok(())
}