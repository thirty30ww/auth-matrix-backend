package com.thirty.user.controller;

import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.enums.result.ViewResultCode;
import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;
import com.thirty.user.service.facade.ViewFacade;
import com.thirty.user.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 页面管理
 */
@RestController
@RequestMapping("/view")
public class ViewController {
    @Resource
    private ViewFacade viewFacade;
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 获取视图树
     * @return 视图树
     */
    @GetMapping("/tree")
    public ResultDTO<List<ViewVO>> getViewTree() {
        List<ViewVO> viewTree = viewFacade.getViewTree();
        return ResultDTO.of(ViewResultCode.GET_VIEW_TREE_SUCCESS, viewTree);
    }

    @GetMapping("/menu/tree")
    public ResultDTO<List<ViewVO>> getMenuTree(@RequestParam(required = false) Integer targetRoleId, @RequestHeader("Authorization") String authHeader) {
        Integer userId = jwtUtil.getUserIdFromAuthHeader(authHeader);
        List<ViewVO> menuTree = viewFacade.getMenuTree(userId, targetRoleId);
        return ResultDTO.of(ViewResultCode.GET_MENU_TREE_SUCCESS, menuTree);
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
