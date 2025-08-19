package com.thirty.common.model.dto;

import com.thirty.common.enums.model.SortDirection;
import lombok.Data;

@Data
public class BaseListDTO {
    /**
     * 排序字段
     */
    private SortDTO sort;

    /**
     * 添加排序字段
     * @param field 排序字段
     * @param direction 排序方向
     */
    public void addSort(String field, SortDirection direction) {
        sort = new SortDTO(field, direction);
    }

    /**
     * 添加排序字段(默认升序)
     * @param field 排序字段
     */
    public void addSort(String field) {
        sort = new SortDTO(field, SortDirection.ASC);
    }
}
