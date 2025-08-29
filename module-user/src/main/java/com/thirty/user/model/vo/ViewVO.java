package com.thirty.user.model.vo;

import com.thirty.user.model.entity.View;
import lombok.Data;

import java.util.List;

@Data
public class ViewVO {
    private View node;
    private boolean hasPermission;
    private List<ViewVO> children;
}
