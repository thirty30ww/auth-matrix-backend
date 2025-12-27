package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.thirty.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("notice")
public class Notice extends BaseEntity {
    /**
     * 公告标题
     */
    private String title;

     /**
      * 公告内容
      */
    private String content;

    /**
     * 发布人ID
     */
    private Integer publisherId;

    /**
     * 是否置顶(1:是 0:否)
     */
    private Boolean isTop;
}
