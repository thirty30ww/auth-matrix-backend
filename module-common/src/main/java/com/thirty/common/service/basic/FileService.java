package com.thirty.common.service.basic;

import org.springframework.web.multipart.MultipartFile;

/**
 * 阿里云OSS服务接口
 */
public interface FileService {
    
    /**
     * 上传图片到OSS
     * 
     * @param file 图片文件
     * @param directory 存储目录，例如：user/avatar/
     * @return 图片访问URL
     */
    String uploadImage(MultipartFile file, String directory);
}