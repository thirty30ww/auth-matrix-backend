package com.thirty.user.controller;

import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.result.ViewResultCode;
import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;
import com.thirty.user.service.facade.ViewFacade;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 页面管理
 */
@RestController
@RequestMapping("/view")
public class ViewController {
    @Resource
    private ViewFacade viewFacade;

    /**
     * 获取视图树
     * @param onlyMenu 是否只获取菜单节点
     * @return 视图树
     */
    @GetMapping("/tree")
    public ResultDTO<List<ViewVO>> getViewTree(@RequestParam(defaultValue = "false", required = false) boolean onlyMenu) {
        List<ViewVO> viewTree = viewFacade.getViewTree(onlyMenu);
        return ResultDTO.of(ViewResultCode.GET_TREE_SUCCESS, viewTree);
    }

    /**
     * 获取视图列表
     * @param keyword 视图名称关键词
     * @return 视图列表
     */
    @GetMapping("/list")
    public ResultDTO<List<View>> getViewList(@RequestParam(required = false) String keyword) {
        List<View> viewList = viewFacade.getViews(keyword);
        return ResultDTO.of(ViewResultCode.GET_LIST_SUCCESS, viewList);
    }
}
