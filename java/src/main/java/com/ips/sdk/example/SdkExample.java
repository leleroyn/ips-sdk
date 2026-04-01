package com.ips.sdk.example;

import com.ips.sdk.IpsClient;
import com.ips.sdk.model.*;
import com.ips.sdk.model.upload.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * SDK 使用示例
 */
public class SdkExample {
    
    public static void main(String[] args) {
        // 创建客户端
        IpsClient client = new IpsClient();
        
        // ========== 身份证检测 ==========
        idCardExample(client);
        
        // ========== 营业执照检测 ==========
        bizLicExample(client);
        
        // ========== 印章检测 ==========
        sealExample(client);
        
        // ========== 通用证件检测 ==========
        cardExample(client);
        
        // ========== OCR 识别 ==========
        ocrExample(client);
        
        // ========== 发票识别 ==========
        invoiceExample(client);
        
        // ========== 手印检测 ==========
        handprintExample(client);
        
        // ========== 健康检查 ==========
        healthCheck(client);
        
        // 关闭客户端
        client.close();
    }
    
    /**
     * 身份证检测 - 推荐使用文件上传方式
     */
    static void idCardExample(IpsClient client) {
        // 方式一：使用 InputStream（通用）
        try (InputStream is = new FileInputStream("idcard.jpg")) {
            IdCardUploadParams params = IdCardUploadParams.builder()
                    .returnCorpImage(true)
                    .returnOcrText(true)
                    .build();
            
            ApiResponse<List<IdCardResult>> response = client.idCardPreprocessV3(is, "idcard.jpg", params);
            
            if (response.isSuccess()) {
                System.out.println("身份证检测成功: " + response.getData());
            } else {
                System.err.println("身份证检测失败 [" + response.getStatusCode() + "]: " + response.getErrorMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 方式二：使用 File（简单）
        File file = new File("idcard.jpg");
        IdCardUploadParams params = IdCardUploadParams.builder()
                .returnOcrText(true)
                .build();
        ApiResponse<List<IdCardResult>> response = client.idCardPreprocessV3(file, params);
    }
    
    /**
     * 营业执照检测
     */
    static void bizLicExample(IpsClient client) {
        try (InputStream is = new FileInputStream("bizlic.jpg")) {
            BizLicUploadParams params = BizLicUploadParams.builder()
                    .returnOcrText(true)
                    .build();
            ApiResponse<List<BizLicResult>> response = client.bizLicPreprocessV3(is, "bizlic.jpg", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 印章检测
     */
    static void sealExample(IpsClient client) {
        try (InputStream is = new FileInputStream("seal.jpg")) {
            SealUploadParams params = SealUploadParams.builder()
                    .returnSealImage(true)
                    .build();
            ApiResponse<List<SealResult>> response = client.sealPreprocessV3(is, "seal.jpg", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 通用证件检测
     */
    static void cardExample(IpsClient client) {
        try (InputStream is = new FileInputStream("card.jpg")) {
            ApiResponse<CardResult> response = client.cardDetectionV3(is, "card.jpg", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * OCR 识别
     */
    static void ocrExample(IpsClient client) {
        // Base64 方式
        String imageBase64 = "..."; // 图片的Base64编码
        OcrRequest request = OcrRequest.builder()
                .imageBase64(imageBase64)
                .fileType("pdf")
                .returnText(true)
                .build();
        ApiResponse<List<OcrResult>> response = client.ocrPreprocessV2(request);
        
        // 文件上传方式
        try (InputStream is = new FileInputStream("doc.pdf")) {
            OcrUploadParams params = OcrUploadParams.builder()
                    .format("txt")
                    .useCls(false)
                    .build();
            ApiResponse<OcrResult> response2 = client.ocrPreprocessV3(is, "doc.pdf", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 发票识别
     */
    static void invoiceExample(IpsClient client) {
        try (InputStream is = new FileInputStream("invoice.pdf")) {
            InvoiceUploadParams params = InvoiceUploadParams.builder()
                    .returnCorpImage(true)
                    .returnOcrText(false)
                    .build();
            ApiResponse<List<InvoiceResult>> response = client.invoicePreprocessV3(is, "invoice.pdf", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 手印检测
     */
    static void handprintExample(IpsClient client) {
        try (InputStream is = new FileInputStream("handprint.jpg")) {
            ApiResponse<HandprintResult> response = client.handprintDetectV2(is, "handprint.jpg", "left");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 健康检查
     */
    static void healthCheck(IpsClient client) {
        ApiResponse<HealthStatus> response = client.healthCheck();
        if (response.isSuccess()) {
            System.out.println("服务正常: " + response.getData());
        } else {
            System.err.println("服务异常 [" + response.getStatusCode() + "]: " + response.getErrorMessage());
        }
    }
}
