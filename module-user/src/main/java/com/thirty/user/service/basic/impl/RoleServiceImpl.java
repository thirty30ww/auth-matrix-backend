package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.mapper.RoleMapper;
import com.thirty.user.model.entity.Role;
import com.thirty.user.service.basic.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
