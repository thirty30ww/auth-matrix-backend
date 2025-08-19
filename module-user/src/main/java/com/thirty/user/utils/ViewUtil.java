package com.thirty.user.utils;


import com.thirty.user.converter.ViewDtoConverter;
import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewUtil {

    /**
     * 将视图列表转换为以ID为键的Map
     * @param nodes 视图列表
     * @return 视图Map
     */
    public static Map<Integer, View> listToMap(List<View> nodes) {
        Map<Integer, View> nodeMap = new HashMap<>();
        for (View node : nodes) {
            nodeMap.put(node.getId(), node);
        }
        return nodeMap;
    }

    /**
     * 根据父节点id查找子节点
     * @param nodes 所有节点
     * @param parentNodeId 父节点id
     * @return 子节点列表
     */
    public static List<View> findByParentNodeId(List<View> nodes, Integer parentNodeId) {
        List<View> views = new ArrayList<>();
        for (View view : nodes) {
            if (view.getParentNodeId().equals(parentNodeId)) {
                views.add(view);
            }
        }
        return views;
    }

    /**
     * 根据父节点id查找第一个子节点
     * @param nodes 所有节点
     * @param parentNodeId 父节点id
     * @return 第一个子节点
     */
    public static View findFirstNode(List<View> nodes, Integer parentNodeId) {
        for (View node : nodes) {
            if (node.getParentNodeId().equals(parentNodeId) && node.getFrontNodeId().equals(0)) {
                return node;
            }
        }
        return null;
    }

    /**
     * 根据父节点id构建树
     * @param nodes 所有节点
     * @param nodeMap 节点map
     * @param parentNodeId 父节点id
     * @return 树
     */
    public static List<ViewVO> getTreeByParentNodeId(List<View> nodes, Map<Integer, View> nodeMap, Integer parentNodeId) {
        // 按照节点顺序构建树
        View currentNode = findFirstNode(nodes, parentNodeId);
        List<ViewVO> viewRespons = new ArrayList<>();

        while (currentNode != null) {
            ViewVO viewVO = ViewDtoConverter.INSTANCE.toViewResponse(currentNode);
            viewVO.setChildren(getTreeByParentNodeId(nodes, nodeMap, currentNode.getId()));
            viewRespons.add(viewVO);

            Integer nextNodeId = currentNode.getBehindNodeId();
            currentNode = nextNodeId != 0 ? nodeMap.get(nextNodeId) : null;
        }

        return viewRespons;
    }
}
