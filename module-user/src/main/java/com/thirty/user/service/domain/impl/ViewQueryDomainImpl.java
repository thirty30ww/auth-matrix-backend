package com.thirty.user.service.domain.impl;

import com.thirty.user.converter.ViewDtoConverter;
import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;
import com.thirty.user.service.basic.ViewService;
import com.thirty.user.service.domain.ViewQueryDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ViewQueryDomainImpl implements ViewQueryDomain {

    @Resource
    private ViewService viewService;

    /**
     * 获取菜单树
     * @return 菜单树
     */
    @Override
    public List<ViewVO> getMenuTree() {
        Map<Integer, View> menuMap = viewService.getNotPageViewMap();
        return getTreeByParentNodeId(menuMap, 0);
    }

    /**
     * 获取视图树
     * @return 视图树
     */
    @Override
    public List<ViewVO> getViewTree() {
        // 菜单节点
        List<ViewVO> menuTree = getMenuTree();
        // 页面节点
        List<ViewVO> pages = viewService.getPageViewVOS();

        // 视图树
        List<ViewVO> viewTree = new ArrayList<>();
        viewTree.addAll(menuTree);
        viewTree.addAll(pages);

        return viewTree;
    }


    /**
     * 查找第一个节点
     * @param nodeMap 节点map
     * @param parentNodeId 父节点id
     * @return 第一个节点
     */
    private View findFirstNode(Map<Integer, View> nodeMap, Integer parentNodeId) {
        for (View node : nodeMap.values()) {
            if (node.getParentNodeId().equals(parentNodeId) && node.getFrontNodeId().equals(0)) {
                return node;
            }
        }
        return null;
    }

    /**
     * 根据父节点id构建树
     * @param nodeMap 节点map
     * @param parentNodeId 父节点id
     * @return 树
     */
    private List<ViewVO> getTreeByParentNodeId(Map<Integer, View> nodeMap, Integer parentNodeId) {
        // 按照节点顺序构建树
        View currentNode = findFirstNode(nodeMap, parentNodeId);
        List<ViewVO> response = new ArrayList<>();

        // 按照节点顺序构建树
        while (currentNode != null) {
            ViewVO viewVO = ViewDtoConverter.INSTANCE.toViewVO(currentNode);
            viewVO.setChildren(getTreeByParentNodeId(nodeMap, currentNode.getId()));
            response.add(viewVO);

            Integer nextNodeId = currentNode.getBehindNodeId();
            currentNode = nextNodeId != 0 ? nodeMap.get(nextNodeId) : null;
        }

        return response;
    }
}
