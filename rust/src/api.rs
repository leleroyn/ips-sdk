use crate::cli::{BizlicArgs, CardArgs, HandprintArgs, IdcardArgs, InvoiceArgs, OcrArgs, SealArgs};
use anyhow::Result;

pub struct ApiRequest {
    pub path: String,
    pub fields: Vec<(String, String)>,
}

fn add_confidence(fields: &mut Vec<(String, String)>, value: Option<f64>) {
    if let Some(c) = value {
        fields.push(("init_confidence".into(), c.to_string()));
    }
}

/// 裁剪图与 OCR 文本一律不返回 (服务端默认 true, 必须显式关闭)
fn no_image_no_text(fields: &mut Vec<(String, String)>) {
    fields.push(("return_corp_image".into(), "false".into()));
    fields.push(("return_ocr_text".into(), "false".into()));
}

pub fn idcard(args: &IdcardArgs) -> Result<ApiRequest> {
    let mut fields = Vec::new();
    no_image_no_text(&mut fields);
    add_confidence(&mut fields, args.init_confidence);
    Ok(ApiRequest { path: "/api/v3/idcard/preprocess".into(), fields })
}

pub fn bizlic(args: &BizlicArgs) -> Result<ApiRequest> {
    let mut fields = Vec::new();
    no_image_no_text(&mut fields);
    add_confidence(&mut fields, args.init_confidence);
    Ok(ApiRequest { path: "/api/v3/bizlic/preprocess".into(), fields })
}

pub fn invoice(args: &InvoiceArgs) -> Result<ApiRequest> {
    let mut fields = Vec::new();
    no_image_no_text(&mut fields);
    add_confidence(&mut fields, args.init_confidence);
    Ok(ApiRequest { path: "/api/v3/invoice/preprocess".into(), fields })
}

pub fn seal(args: &SealArgs) -> Result<ApiRequest> {
    let mut fields = Vec::new();
    fields.push(("return_seal_image".into(), "false".into()));
    fields.push(("return_ocr_text".into(), "false".into()));
    add_confidence(&mut fields, args.init_confidence);
    Ok(ApiRequest { path: "/api/v3/seal/preprocess".into(), fields })
}

pub fn ocr(args: &OcrArgs) -> Result<ApiRequest> {
    let mut fields = Vec::new();
    if let Some(f) = &args.format {
        fields.push(("format".into(), f.clone()));
    }
    Ok(ApiRequest { path: "/api/v3/ocr/preprocess".into(), fields })
}

pub fn card(_args: &CardArgs) -> Result<ApiRequest> {
    let mut fields = Vec::new();
    fields.push(("return_corp_image".into(), "false".into()));
    Ok(ApiRequest { path: "/api/v3/card/det".into(), fields })
}

pub fn handprint(args: &HandprintArgs) -> Result<ApiRequest> {
    let mut fields = Vec::new();
    if let Some(p) = &args.position {
        fields.push(("position".into(), p.clone()));
    }
    Ok(ApiRequest { path: "/api/v2/handprint/detect".into(), fields })
}

#[cfg(test)]
mod tests {
    use super::*;
    use clap::Parser;

    #[test]
    fn paths_use_latest_version() {
        let args = IdcardArgs::try_parse_from(["idcard", "a.jpg"]).unwrap();
        assert_eq!(idcard(&args).unwrap().path, "/api/v3/idcard/preprocess");

        let args = SealArgs::try_parse_from(["seal", "a.jpg"]).unwrap();
        assert_eq!(seal(&args).unwrap().path, "/api/v3/seal/preprocess");

        let args = HandprintArgs::try_parse_from(["handprint", "a.jpg"]).unwrap();
        assert_eq!(handprint(&args).unwrap().path, "/api/v2/handprint/detect");
    }

    #[test]
    fn images_and_text_always_disabled() {
        let args = IdcardArgs::try_parse_from(["idcard", "a.jpg"]).unwrap();
        let f = idcard(&args).unwrap().fields;
        assert!(f.contains(&("return_corp_image".to_string(), "false".to_string())));
        assert!(f.contains(&("return_ocr_text".to_string(), "false".to_string())));

        let args = SealArgs::try_parse_from(["seal", "a.jpg"]).unwrap();
        let f = seal(&args).unwrap().fields;
        assert!(f.contains(&("return_seal_image".to_string(), "false".to_string())));
        assert!(f.contains(&("return_ocr_text".to_string(), "false".to_string())));

        let args = CardArgs::try_parse_from(["card", "a.jpg"]).unwrap();
        assert!(card(&args).unwrap().fields.contains(&("return_corp_image".to_string(), "false".to_string())));
    }

    #[test]
    fn only_explicit_optional_fields_sent() {
        let args = IdcardArgs::try_parse_from(["idcard", "a.jpg", "--init-confidence", "0.7"]).unwrap();
        assert_eq!(
            idcard(&args).unwrap().fields,
            vec![
                ("return_corp_image".to_string(), "false".to_string()),
                ("return_ocr_text".to_string(), "false".to_string()),
                ("init_confidence".to_string(), "0.7".to_string()),
            ]
        );

        let args = OcrArgs::try_parse_from(["ocr", "a.jpg"]).unwrap();
        assert!(ocr(&args).unwrap().fields.is_empty());
    }
}