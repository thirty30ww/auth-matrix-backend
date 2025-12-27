package com.thirty.user.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeVO {
    /**
     * 公告ID
     */
    private Integer id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 是否置顶(1:是 0:否)
     */
    private Boolean isTop;

    /**
     * 发布人ID
     */
    private Integer publisherId;

    /**
     * 发布人姓名
     */
    private String publisherName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}