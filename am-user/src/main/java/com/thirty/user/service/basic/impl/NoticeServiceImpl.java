package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.mapper.NoticeMapper;
import com.thirty.user.model.entity.Notice;
import com.thirty.user.service.basic.NoticeService;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
}
