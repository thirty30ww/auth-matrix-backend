package com.thirty.user.model.vo;

import com.thirty.user.model.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class RoleVO {
    private Role node;
    private Boolean hasPermission;
    private List<RoleVO> children;
}
