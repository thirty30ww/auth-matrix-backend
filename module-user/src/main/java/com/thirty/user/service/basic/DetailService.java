package com.thirty.user.service.basic;

import com.thirty.user.model.entity.Detail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Lenovo
* @description 针对表【detail(用户的具体信息)】的数据库操作Service
* @createDate 2025-07-18 12:09:47
*/
public interface DetailService extends IService<Detail> {
    /**
     * 创建用户详情
     * @param userId 用户ID
     * @param detail 用户详情
     */
    void createDetail(Integer userId, Detail detail);
}
