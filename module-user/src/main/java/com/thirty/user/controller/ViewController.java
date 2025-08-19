package com.thirty.user.controller;

import com.thirty.common.enums.result.GlobalResultCode;
import com.thirty.common.model.dto.ResultDTO;
import com.thirty.user.model.vo.ViewVO;
import com.thirty.user.model.entity.View;
import com.thirty.user.enums.result.ViewResultCode;
import com.thirty.user.service.ViewService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/view")
public class ViewController {
    @Resource
    private ViewService viewService;

    /**
     * 获取视图树
     * @param onlyMenu 是否只获取菜单节点
     * @return 视图树
     */
    @GetMapping("/tree")
    public ResultDTO<List<ViewVO>> getViewTree(@RequestParam(defaultValue = "false", required = false) boolean onlyMenu) {
        List<ViewVO> viewTree = viewService.getViewTree(onlyMenu);
        if (viewTree == null) {
            return ResultDTO.of(GlobalResultCode.ERROR);
        }
        return ResultDTO.of(ViewResultCode.GET_TREE_SUCCESS, viewTree);
    }

    /**
     * 获取视图列表
     * @param keyword 视图名称关键词
     * @return 视图列表
     */
    @GetMapping("/list")
    public ResultDTO<List<View>> getViewList(@RequestParam(required = false) String keyword) {
        List<View> viewList = viewService.getViewList(keyword);
        return ResultDTO.of(ViewResultCode.GET_LIST_SUCCESS, viewList);
    }
}
