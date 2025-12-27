package com.thirty.system.controller;

import com.thirty.common.annotation.RateLimiter;
import com.thirty.common.enums.model.LimitType;
import com.thirty.common.model.dto.ResultDTO;
import com.thirty.system.constant.FileConstant;
import com.thirty.system.enums.result.FileResultCode;
import com.thirty.system.service.basic.FileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.thirty.system.utils.FileUtil.isImage;
import static com.thirty.system.utils.FileUtil.isImageSizeExceeded;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/file")
@RateLimiter(count = 3, limitType = LimitType.TOKEN)
public class FileController {

    @Resource
    private FileService fileService;

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @param directory 存储目录，默认为images/
     * @return 图片URL
     */
    @PostMapping("/upload/image")
    public ResultDTO<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "directory", defaultValue = "images/") String directory) {

        if (!isImage(file)) {
            return ResultDTO.of(FileResultCode.FILE_UPLOAD_FORMAT_ERROR);
        }

        if (isImageSizeExceeded(file, FileConstant.MAX_IMAGE_SIZE)) {
            return ResultDTO.of(FileResultCode.FILE_UPLOAD_SIZE_EXCEEDED);
        }
        
        // 上传图片到OSS
        String imageUrl = fileService.uploadImage(file, directory);
        
        return ResultDTO.of(FileResultCode.IMAGE_UPLOAD_SUCCESS, imageUrl);
    }
}