package com.thirty.common.controller;

import com.thirty.common.constant.FileConstant;
import com.thirty.common.enums.result.FileResultCode;
import com.thirty.common.service.FileService;
import com.thirty.common.model.dto.ResultDTO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.thirty.common.utils.FileUtil.*;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/file")
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