package com.thirty.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.user.model.entity.Notice;
import com.thirty.user.model.vo.NoticeVO;

public interface NoticeMapper extends BaseMapper<Notice> {
    IPage<NoticeVO> getNotices(IPage<Notice> page);
}
