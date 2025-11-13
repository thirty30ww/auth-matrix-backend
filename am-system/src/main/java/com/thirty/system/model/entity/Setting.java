package com.thirty.system.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局设置表
 * @TableName setting
 */
@TableName(value ="setting")
@Data
public class Setting implements Serializable {
    /**
     * 设置ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 设置的字段名
     */
    private String field;

    /**
     * 设置的标题
     */
    private String title;

    /**
     * 设置的描述
     */
    private String description;

    /**
     * 设置的值
     */
    private String value;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否被删除(1:是 0:否)
     */
    @TableLogic
    private Boolean isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 构建ID到设置实体的映射
     * @param settings 设置实体列表
     * @return ID到设置实体的映射
     */
    public static Map<Integer, Setting> buildMap(List<Setting> settings) {
        return settings.stream().collect(Collectors.toMap(Setting::getId, setting -> setting));
    }
}