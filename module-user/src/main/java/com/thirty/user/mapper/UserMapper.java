package com.thirty.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.user.model.dto.GetUserListDTO;
import com.thirty.user.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thirty.user.model.vo.UserVO;

/**
* @author Lenovo
* @description 针对表【user(用户主表)】的数据库操作Mapper
* @createDate 2025-07-07 15:24:51
* @Entity com.thirty.moduleuser.domain.User
*/
public interface UserMapper extends BaseMapper<User> {
    /**
     * 获取用户列表
     * @param params 获取用户列表请求参数
     * @return 用户列表
     */
    IPage<UserVO> getUserList(IPage<UserVO> page, GetUserListDTO params);
}




