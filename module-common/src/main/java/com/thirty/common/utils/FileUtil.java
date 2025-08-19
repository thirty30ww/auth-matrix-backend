package com.thirty.common.utils;

import com.thirty.common.constant.FileConstant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class FileUtil {

    /**
     * 获取文件Content-Type
     *
     * @param suffix 文件后缀
     * @return Content-Type
     */
    public static String getContentType(String suffix) {
        return FileConstant.IMAGE_CONTENT_TYPES.getOrDefault(suffix, "image/jpeg");
    }

    /**
     * 判断文件是否为图片
     * @param file 图片文件
     * @return 是否为图片
     */
    public static boolean isImage(MultipartFile file) {
        if (file.isEmpty()) {
            return false;
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = getFileSuffix(originalFilename);
        return FileConstant.IMAGE_CONTENT_TYPES.containsKey(suffix);
    }

    /**
     * 判断图片大小是否超过阈值
     * @param file 图片文件
     * @param maxSize 最大允许大小（单位：字节）
     * @return 是否超过阈值
     */
    public static boolean isImageSizeExceeded(MultipartFile file, long maxSize) {
        log.debug("文件大小: {} 最大允许大小: {}", file.getSize(), maxSize);
        return file.getSize() > maxSize;
    }

    /**
     * 生成文件路径
     * @param suffix 文件后缀
     * @param directory 存储目录，例如：user/avatar/
     * @return 文件路径
     */
    public static String generateFilePath(String suffix, String directory) {
        // 使用UUID生成文件名，防止文件名重复
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;

        // 按照日期进行分类，例如：2023/09/01/xxx.jpg
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        return directory + datePath + "/" + fileName;   // 拼接文件完整路径
    }

    /**
     * 获取文件后缀(全小写)
     * @param originalFilename 文件名
     * @return 文件后缀
     */
    public static String getFileSuffix(String originalFilename) {
        if (originalFilename == null) {
            return null;
        }
        return originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
    }
}