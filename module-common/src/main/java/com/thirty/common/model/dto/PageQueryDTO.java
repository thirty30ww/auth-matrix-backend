package com.thirty.common.model.dto;

import lombok.Data;

/**
 * 分页查询DTO
 */
@Data
public class PageQueryDTO<T> {
    private static int PAGE_NUM = 1;
    private static int PAGE_SIZE = 10;

    private int pageNum = PAGE_NUM;
    private int pageSize = PAGE_SIZE;

    private T params;
}