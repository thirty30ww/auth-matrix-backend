package com.thirty.common.service.basic.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.thirty.common.config.OssConfig;
import com.thirty.common.enums.result.FileResultCode;
import com.thirty.common.exception.BusinessException;
import com.thirty.common.service.basic.FileService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static com.thirty.common.utils.FileUtil.*;

/**
 * 阿里云OSS服务实现类
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private OssConfig ossConfig;

    /**
     * 上传图片到OSS
     *
     * @param file 图片文件
     * @param directory 存储目录，例如：user/avatar/
     * @return 图片访问URL
     */
    @Override
    public String uploadImage(MultipartFile file, String directory) {
        // 获取文件名
        String originalFilename = file.getOriginalFilename();
        // 获取文件后缀
        String suffix = getFileSuffix(originalFilename);
        // 生成文件路径
        String filePath = generateFilePath(suffix, directory);

        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(
                ossConfig.getEndpoint(),
                ossConfig.getAccessKeyId(),
                ossConfig.getAccessKeySecret()
        );

        return uploadFileToOss(file, filePath, suffix, ossClient);
    }

    /**
     * 上传文件到OSS
     *
     * @param file 图片文件
     * @param filePath 文件路径
     * @param ossClient OSS客户端实例
     * @return 图片访问URL
     */
    private String uploadFileToOss(MultipartFile file, String filePath, String suffix, OSS ossClient) {
        try {
            // 获取文件输入流
            InputStream inputStream = file.getInputStream();

            // 创建上传Object的元数据
            ObjectMetadata metadata = new ObjectMetadata();
            // 设置Content-Type
            metadata.setContentType(getContentType(suffix));

            // 上传文件
            ossClient.putObject(ossConfig.getBucketName(), filePath, inputStream, metadata);

            // 设置文件访问权限为公共读
            ossClient.setObjectAcl(ossConfig.getBucketName(), filePath, CannedAccessControlList.PublicRead);

            // 返回文件访问路径
            return ossConfig.getDomain() + "/" + filePath;
        } catch (Exception e) {
            log.error("上传文件到OSS失败: {}", e.getMessage());
            throw new BusinessException(FileResultCode.FILE_UPLOAD_FAILED);
        } finally {
            // 关闭OSSClient
            ossClient.shutdown();
        }
    }
}