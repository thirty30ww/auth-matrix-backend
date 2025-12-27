package com.thirty.user.service.domain.notice.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.mapper.NoticeMapper;
import com.thirty.user.model.entity.Notice;
import com.thirty.user.model.vo.NoticeVO;
import com.thirty.user.service.basic.UserService;
import com.thirty.user.service.domain.notice.NoticeQueryDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class NoticeQueryDomainImpl implements NoticeQueryDomain {

    @Resource
    private NoticeMapper noticeMapper;

    @Resource
    private UserService userService;

    /**
     * 获取通知列表
     * @param dto 分页查询DTO
     * @return 通知列表
     */
    @Override
    public IPage<NoticeVO> getNotices(PageQueryDTO<Void> dto) {
        IPage<Notice> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return noticeMapper.getNotices(page);
    }
}
