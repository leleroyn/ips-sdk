package com.ips.sdk.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Base64 工具类
 */
public class Base64Utils {
    
    private static final int BUFFER_SIZE = 8192;
    
    /**
     * 将文件编码为Base64字符串
     * 
     * @param filePath 文件路径
     * @return Base64编码字符串
     * @throws IOException 读取文件失败
     */
    public static String encodeFile(String filePath) throws IOException {
        return encodeFile(Paths.get(filePath));
    }
    
    /**
     * 将文件编码为Base64字符串 (流式处理，避免大文件OOM)
     * 
     * @param path 文件路径
     * @return Base64编码字符串
     * @throws IOException 读取文件失败
     */
    public static String encodeFile(Path path) throws IOException {
        try (InputStream in = Files.newInputStream(path);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }
    
    /**
     * 将Base64字符串解码为字节数组
     * 
     * @param base64String Base64编码字符串
     * @return 字节数组
     */
    public static byte[] decode(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }
    
    /**
     * 将Base64字符串保存为文件
     * 
     * @param base64String Base64编码字符串
     * @param outputPath 输出文件路径
     * @throws IOException 写入文件失败
     */
    public static void decodeToFile(String base64String, String outputPath) throws IOException {
        byte[] bytes = decode(base64String);
        Files.write(Paths.get(outputPath), bytes);
    }
    
    /**
     * 将字节数组编码为Base64字符串
     * 
     * @param bytes 字节数组
     * @return Base64编码字符串
     */
    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
