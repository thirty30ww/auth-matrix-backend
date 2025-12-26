package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.model.entity.UserDetail;
import com.thirty.user.service.basic.UserDetailService;
import com.thirty.user.mapper.DetailMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【detail(用户的具体信息)】的数据库操作Service实现
* @createDate 2025-07-18 12:09:47
*/
@Service
public class UserDetailServiceImpl extends ServiceImpl<DetailMapper, UserDetail>
    implements UserDetailService {

    /**
     * 创建用户详情
     * @param userId 用户ID
     * @param userDetail 用户详情
     */
    @Override
    public void createDetail(Integer userId, UserDetail userDetail) {
        // 设置用户ID
        userDetail.setId(userId);
        // 保存用户详情
        save(userDetail);
    }
}




