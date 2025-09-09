package com.thirty.user.service.domain.view.impl;

import com.thirty.user.converter.ViewConverter;
import com.thirty.user.model.dto.ViewDTO;
import com.thirty.user.model.entity.View;
import com.thirty.user.service.basic.ViewService;
import com.thirty.user.service.domain.view.ViewOperationDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ViewOperationDomainImpl implements ViewOperationDomain {

    @Resource
    private ViewService viewService;

    /**
     * 添加视图
     * @param viewDTO 视图DTO
     */
    @Override
    public void addView(ViewDTO viewDTO) {
        View view = ViewConverter.INSTANCE.toView(viewDTO);
        viewService.tailInsert(view);
    }

    /**
     * 修改视图
     * @param viewDTO 视图DTO
     */
    @Override
    public void modifyView(ViewDTO viewDTO) {
        View view = ViewConverter.INSTANCE.toView(viewDTO);
        viewService.modifyView(view);
    }

    /**
     * 删除视图
     * @param viewId 视图ID
     */
    @Override
    public void deleteView(Integer viewId) {
        viewService.deleteView(viewId);
    }

    /**
     * 移动视图
     * @param viewId 视图ID
     * @param isUp 是否上移
     */
    @Override
    public void moveView(Integer viewId, Boolean isUp) {
        if (isUp) {
            viewService.moveUp(viewId);
        } else {
            viewService.moveDown(viewId);
        }
    }
}
