package com.thirty.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.annotation.RateLimiter;
import com.thirty.common.enums.model.LimitType;
import com.thirty.common.model.dto.PageQueryDTO;
import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.result.NoticeResultCode;
import com.thirty.user.model.vo.NoticeVO;
import com.thirty.user.service.facade.NoticeFacade;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
@RateLimiter(limitType = LimitType.TOKEN)
public class NoticeController {
    @Resource
    private NoticeFacade noticeFacade;

    /**
     * 获取通知列表
     */
    @PostMapping("/list")
    public ResultDTO<IPage<NoticeVO>> getNotices(@RequestBody PageQueryDTO<Void> dto) {
        return ResultDTO.of(NoticeResultCode.GET_NOTICE_LIST_SUCCESS, noticeFacade.getNotices(dto));
    }
}
