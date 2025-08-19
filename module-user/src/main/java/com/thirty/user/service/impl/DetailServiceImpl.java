package com.thirty.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.model.entity.Detail;
import com.thirty.user.service.DetailService;
import com.thirty.user.mapper.DetailMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【detail(用户的具体信息)】的数据库操作Service实现
* @createDate 2025-07-18 12:09:47
*/
@Service
public class DetailServiceImpl extends ServiceImpl<DetailMapper, Detail>
    implements DetailService{
}




