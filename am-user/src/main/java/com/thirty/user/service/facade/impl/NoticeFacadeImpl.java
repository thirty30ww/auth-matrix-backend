package com.thirty.user.service.facade.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.vo.NoticeVO;
import com.thirty.user.service.domain.notice.NoticeQueryDomain;
import com.thirty.user.service.facade.NoticeFacade;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class NoticeFacadeImpl implements NoticeFacade {
    @Resource
    private NoticeQueryDomain noticeQueryDomain;

    /**
     * 获取通知列表
     * @param dto 分页查询DTO
     * @return 通知列表
     */
    @Override
    public IPage<NoticeVO> getNotices(PageQueryDTO<Void> dto) {
        return noticeQueryDomain.getNotices(dto);
    }
}
