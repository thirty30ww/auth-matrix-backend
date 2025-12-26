package com.thirty.user.service.basic;

import com.thirty.user.model.entity.UserDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Lenovo
* @description 针对表【detail(用户的具体信息)】的数据库操作Service
* @createDate 2025-07-18 12:09:47
*/
public interface UserDetailService extends IService<UserDetail> {
    /**
     * 创建用户详情
     * @param userId 用户ID
     * @param userDetail 用户详情
     */
    void createDetail(Integer userId, UserDetail userDetail);
}
