package com.ips.sdk.util;

import java.io.File;
import java.io.IOException;

/**
 * 文件工具类
 */
public class FileUtils {
    
    /**
     * 获取文件扩展名
     * 
     * @param filename 文件名
     * @return 扩展名 (不包含点)
     */
    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int lastDot = filename.lastIndexOf('.');
        if (lastDot >= 0 && lastDot < filename.length() - 1) {
            return filename.substring(lastDot + 1);
        }
        return "";
    }
    
    /**
     * 获取文件扩展名
     * 
     * @param file 文件对象
     * @return 扩展名 (不包含点)
     */
    public static String getExtension(File file) {
        return file != null ? getExtension(file.getName()) : null;
    }
    
    /**
     * 判断文件是否为图像文件
     * 
     * @param filename 文件名
     * @return 是否为图像文件
     */
    public static boolean isImage(String filename) {
        String ext = getExtension(filename).toLowerCase();
        return ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png") 
                || ext.equals("gif") || ext.equals("bmp") || ext.equals("webp");
    }
    
    /**
     * 判断文件是否为PDF文件
     * 
     * @param filename 文件名
     * @return 是否为PDF文件
     */
    public static boolean isPdf(String filename) {
        String ext = getExtension(filename).toLowerCase();
        return ext.equals("pdf");
    }
    
    /**
     * 创建目录 (如果不存在)
     * 
     * @param dirPath 目录路径
     * @return 创建的目录
     * @throws IOException 创建失败
     */
    public static File createDirectoryIfNotExists(String dirPath) throws IOException {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new IOException("无法创建目录: " + dirPath);
            }
        }
        return dir;
    }
    
    /**
     * 获取文件大小 (字节)
     * 
     * @param file 文件对象
     * @return 文件大小
     */
    public static long getFileSize(File file) {
        return file != null && file.exists() ? file.length() : 0;
    }
    
    /**
     * 格式化文件大小
     * 
     * @param bytes 字节数
     * @return 格式化后的文件大小
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }
}
