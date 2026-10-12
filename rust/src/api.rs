use crate::cli::{
    tri, BizlicArgs, CardArgs, HandprintArgs, IdcardArgs, InvoiceArgs, OcrArgs, SealArgs,
};
use anyhow::Result;

pub struct ApiRequest {
    pub path: String,
    pub fields: Vec<(String, String)>,
}

fn add_bool(fields: &mut Vec<(String, String)>, name: &str, value: Option<bool>) {
    if let Some(v) = value {
        fields.push((name.to_string(), v.to_string()));
    }
}

fn add_confidence(fields: &mut Vec<(String, String)>, value: Option<f64>) {
    if let Some(c) = value {
        fields.push(("init_confidence".into(), c.to_string()));
    }
}

/// 裁剪图一律不返回 (服务端默认 true, 必须显式关闭)
fn no_corp_image(fields: &mut Vec<(String, String)>) {
    fields.push(("return_corp_image".into(), "false".into()));
}

fn no_seal_image(fields: &mut Vec<(String, String)>) {
    fields.push(("return_seal_image".into(), "false".into()));
}

pub fn idcard(args: &IdcardArgs) -> Result<ApiRequest> {
    let mut fields = Vec::new();
    no_corp_image(&mut fields);
    add_bool(&mut fields, "return_ocr_text", tri(args.return_ocr_text, args.no_return_ocr_text, "return-ocr-text")?);
    add_bool(&mut fields, "resize", tri(args.resize, args.no_resize, "resize")?);
    add_bool(&mut fields, "back_ground", tri(args.back_ground, args.no_back_ground, "back-ground")?);
    add_confidence(&mut fields, args.init_confidence);
    Ok(ApiRequest { path: "/api/v3/idcard/preprocess".into(), fields })
}

pub fn bizlic(args: &BizlicArgs) -> Result<ApiRequest> {
    let mut fields = Vec::new();
    no_corp_image(&mut fields);
    add_bool(&mut fields, "return_ocr_text", tri(args.return_ocr_text, args.no_return_ocr_text, "return-ocr-text")?);
    add_bool(&mut fields, "resize", tri(args.resize, args.no_resize, "resize")?);
    add_bool(&mut fields, "back_ground", tri(args.back_ground, args.no_back_ground, "back-ground")?);
    add_confidence(&mut fields, args.init_confidence);
    Ok(ApiRequest { path: "/api/v3/bizlic/preprocess".into(), fields })
}

pub fn invoice(args: &InvoiceArgs) -> Result<ApiRequest> {
    let mut fields = Vec::new();
    no_corp_image(&mut fields);
    add_bool(&mut fields, "return_ocr_text", tri(args.return_ocr_text, args.no_return_ocr_text, "return-ocr-text")?);
    add_bool(&mut fields, "resize", tri(args.resize, args.no_resize, "resize")?);
    add_bool(&mut fields, "back_ground", tri(args.back_ground, args.no_back_ground, "back-ground")?);
    add_confidence(&mut fields, args.init_confidence);
    Ok(ApiRequest { path: "/api/v3/invoice/preprocess".into(), fields })
}

pub fn seal(args: &SealArgs) -> Result<ApiRequest> {
    let mut fields = Vec::new();
    no_seal_image(&mut fields);
    add_bool(&mut fields, "return_ocr_text", tri(args.return_ocr_text, args.no_return_ocr_text, "return-ocr-text")?);
    add_bool(&mut fields, "resize", tri(args.resize, args.no_resize, "resize")?);
    add_bool(&mut fields, "back_ground", tri(args.back_ground, args.no_back_ground, "back-ground")?);
    add_confidence(&mut fields, args.init_confidence);
    Ok(ApiRequest { path: "/api/v3/seal/preprocess".into(), fields })
}

pub fn ocr(args: &OcrArgs) -> Result<ApiRequest> {
    let mut fields = Vec::new();
    add_bool(&mut fields, "resize", tri(args.resize, args.no_resize, "resize")?);
    add_bool(&mut fields, "use_cls", tri(args.use_cls, args.no_use_cls, "use-cls")?);
    if let Some(id) = &args.app_id {
        fields.push(("app_id".into(), id.clone()));
    }
    if let Some(f) = &args.format {
        fields.push(("format".into(), f.clone()));
    }
    Ok(ApiRequest { path: "/api/v3/ocr/preprocess".into(), fields })
}

pub fn card(_args: &CardArgs) -> Result<ApiRequest> {
    let mut fields = Vec::new();
    no_corp_image(&mut fields);
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
    fn corp_image_always_disabled() {
        let args = IdcardArgs::try_parse_from(["idcard", "a.jpg"]).unwrap();
        assert!(idcard(&args).unwrap().fields.contains(&("return_corp_image".to_string(), "false".to_string())));

        let args = SealArgs::try_parse_from(["seal", "a.jpg"]).unwrap();
        assert!(seal(&args).unwrap().fields.contains(&("return_seal_image".to_string(), "false".to_string())));

        let args = CardArgs::try_parse_from(["card", "a.jpg"]).unwrap();
        assert!(card(&args).unwrap().fields.contains(&("return_corp_image".to_string(), "false".to_string())));
    }

    #[test]
    fn fields_only_include_explicit_flags() {
        let args = IdcardArgs::try_parse_from(["idcard", "a.jpg", "--return-ocr-text", "--init-confidence", "0.7"]).unwrap();
        let req = idcard(&args).unwrap();
        assert_eq!(
            req.fields,
            vec![
                ("return_corp_image".to_string(), "false".to_string()),
                ("return_ocr_text".to_string(), "true".to_string()),
                ("init_confidence".to_string(), "0.7".to_string()),
            ]
        );
    }
}