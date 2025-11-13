package com.thirty.system.constant;

import java.util.Map;

public class FileConstant {
    /**
     * 图片最大大小限制（10MB）
     */
    public static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024;
    
    /**
     * 图片后缀对应的 MIME 类型
     */
    public static final Map<String, String> IMAGE_CONTENT_TYPES;
    
    static {
        IMAGE_CONTENT_TYPES = Map.of(".png", "image/png", ".jpg", "image/jpeg", ".jpeg", "image/jpeg", ".gif", "image/gif", ".bmp", "image/bmp", ".webp", "image/webp");
    }
}
