package com.thirty.user.service.facade;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.user.model.vo.NoticeVO;

public interface NoticeFacade {
    /**
     * 获取通知列表
     * @param dto 分页查询DTO
     * @return 通知列表
     */
    IPage<NoticeVO> getNotices(PageQueryDTO<Void> dto);
}
