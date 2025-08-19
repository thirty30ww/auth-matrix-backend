package com.thirty.user.service;

import com.thirty.user.model.entity.View;
import com.baomidou.mybatisplus.extension.service.IService;
import com.thirty.user.model.vo.ViewVO;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【view(页面表)】的数据库操作Service
* @createDate 2025-08-03 10:06:39
*/
public interface ViewService extends IService<View> {
    List<ViewVO> getViewTree(boolean onlyMenu);

    List<View> getViewList(String keyword);
}
