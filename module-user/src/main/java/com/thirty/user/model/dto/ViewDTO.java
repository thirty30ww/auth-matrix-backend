package com.thirty.user.model.dto;

import com.thirty.user.enums.model.ViewType;
import lombok.Data;

@Data
public class ViewDTO {
    private Integer id;
    private String name;
    private String path;
    private String component;
    private ViewType type;
    private String permissionCode;
    private Integer parentNodeId;
    private Integer frontNodeId;
    private Integer behindNodeId;
    private String icon;
    private Boolean isValid;
}
