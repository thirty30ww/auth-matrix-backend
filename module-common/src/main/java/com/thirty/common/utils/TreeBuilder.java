package com.thirty.common.utils;

import io.github.thirty30ww.defargs.annotation.DefaultValue;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeBuilder<T, ID> {

    /**
     * 构建树结构
     * @param nodes 节点列表
     * @param getId 节点ID获取函数
     * @param getParentId 父节点ID获取函数
     * @param getChildren 子节点列表获取函数
     * @param rootParentId 根节点的父节点ID
     * @param addOrphanNodes 是否添加孤立节点到树中
     * @return 树结构的节点列表
     */
    public List<T> buildTree(List<T> nodes,
                             Function<T, ID> getId,
                             Function<T, ID> getParentId,
                             Function<T, List<T>> getChildren,
                             ID rootParentId,
                             @DefaultValue("false") Boolean addOrphanNodes) {
        if (nodes == null || nodes.isEmpty()) {
            return Collections.emptyList();
        }

        Map<ID, T> nodeMap = buildMap(nodes, getId);    // 节点ID到节点的映射
        List<T> tree = new ArrayList<>();    // 结果树

        // 遍历所有节点
        nodes.forEach(node -> {
            ID parentId = getParentId.apply(node);

            if (Objects.equals(parentId, rootParentId)) {   // 根节点添加到树中
                tree.add(node);
            } else {
                // 其他节点添加到父节点的子节点列表中
                T parentNode = nodeMap.get(parentId);


                if (parentNode != null) {   // 父节点存在
                    getChildren.apply(parentNode).add(node);
                } else if (addOrphanNodes) {    // 父节点不存在
                    tree.add(node);
                }
            }
        });

        return tree;
    }

    /**
     * 构建节点ID到节点的映射
     * @param nodes 节点列表
     * @param getId 节点ID获取函数
     * @return 节点ID到节点的映射
     */
    private Map<ID, T> buildMap(List<T> nodes, Function<T, ID> getId) {
        return nodes.stream()
                .collect(Collectors.toMap(getId, Function.identity()));
    }
}
