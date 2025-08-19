package com.thirty.common.enums.result;

import com.thirty.common.enums.IResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileResultCode implements IResult {
    IMAGE_UPLOAD_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "图片上传成功"),
    FILE_UPLOAD_FAILED(3001, "文件上传失败"),
    FILE_UPLOAD_SIZE_EXCEEDED(3002, "文件上传大小超过限制"),
    FILE_UPLOAD_FORMAT_ERROR(3003, "文件上传格式错误");

    private final Integer code;
    private final String message;
}
