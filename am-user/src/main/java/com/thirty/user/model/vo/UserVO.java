package com.thirty.user.model.vo;

import com.thirty.user.enums.model.UserSex;
import com.thirty.user.model.entity.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserVO {
    private Integer id;
    private String username;
    private List<Role> roles;
    private String name;
    private String avatarUrl;
    private UserSex sex;
    private String signature;
    private Boolean hasPermission;
    private Boolean isValid;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}