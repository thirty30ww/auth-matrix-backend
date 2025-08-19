package com.thirty.user.model.dto;

import com.thirty.common.model.dto.BaseListDTO;
import com.thirty.user.enums.model.UserSex;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetUserListDTO extends BaseListDTO {
    private String username;
    private String name;
    private UserSex sex;
    private List<Integer> roleIds;
    private Boolean isValid;
}
