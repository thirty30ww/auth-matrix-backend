package com.thirty.user.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AssignViewDTO {
    private Integer roleId;
    private List<Integer> viewIds;
}
