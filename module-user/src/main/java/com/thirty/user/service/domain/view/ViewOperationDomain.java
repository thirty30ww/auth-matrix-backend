package com.thirty.user.service.domain.view;

import com.thirty.user.model.dto.ViewDTO;

public interface ViewOperationDomain {
    /**
     * 添加视图
     * @param viewDTO 视图DTO
     */
    void addView(ViewDTO viewDTO);

    /**
     * 修改视图
     * @param viewDTO 视图DTO
     */
    void modifyView(ViewDTO viewDTO);

    /**
     * 删除视图
     * @param viewId 视图ID
     */
    void deleteView(Integer viewId);

    /**
     * 移动视图
     * @param viewId 视图ID
     * @param isUp 是否上移
     */
    void moveView(Integer viewId, Boolean isUp);
}
