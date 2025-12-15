package com.thirty.user.service.basic.impl;

import com.thirty.user.enums.model.PermissionBkType;
import com.thirty.user.mapper.PermissionBkMapper;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.service.basic.PermissionBkService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【view(页面表)】的数据库操作Service实现
* @createDate 2025-08-03 10:06:39
*/
@Service
public class PermissionBkServiceImpl extends BasePermissionServiceImpl<
        PermissionBkMapper,
        PermissionBk
        >
        implements PermissionBkService {
}