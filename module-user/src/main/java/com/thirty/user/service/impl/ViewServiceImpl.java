package com.thirty.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.converter.ViewDtoConverter;
import com.thirty.user.enums.model.ViewType;
import com.thirty.user.model.vo.ViewVO;
import com.thirty.user.model.entity.View;
import com.thirty.user.service.ViewService;
import com.thirty.user.mapper.ViewMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.thirty.user.utils.ViewUtil.*;

/**
* @author Lenovo
* @description 针对表【view(页面表)】的数据库操作Service实现
* @createDate 2025-08-03 10:06:39
*/
@Service
public class ViewServiceImpl extends ServiceImpl<ViewMapper, View>
    implements ViewService{

    /**
     * 获取视图树
     * @param onlyMenu 是否获取菜单节点
     * @return 视图树
     */
    @Override
    public List<ViewVO> getViewTree(boolean onlyMenu) {
        // 获取菜单节点
        List<ViewVO> responses = getMenuTree();

        // 获取非菜单节点
        if (!onlyMenu) {
            responses.addAll(getNotMenuNode());
        }

        return responses;
    }

    /**
     * 获取视图列表
     * @param keyword 视图名称
     * @return 视图列表
     */
    @Override
    public List<View> getViewList(String keyword) {
        QueryWrapper<View> wrapper = new QueryWrapper<>();
        wrapper.like("name", keyword)
                .ne("type", ViewType.DIRECTORY); // 筛选不为目录的节点
        return list(wrapper);
    }

    /**
     * 获取菜单节点
     * @return 菜单节点
     */
    private List<ViewVO> getMenuTree() {
        // 菜单节点
        QueryWrapper<View> menuWrapper = new QueryWrapper<>();
        menuWrapper.ne("type", ViewType.PAGE);    // 筛选不为页面的节点
        List<View> nodes = list(menuWrapper);

        // 创建以ID为键的视图Map，提高查找效率
        Map<Integer, View> viewMap = listToMap(nodes);

        // 递归构建整棵页面树
        return getTreeByParentNodeId(nodes, viewMap, 0);
    }

    /**
     * 获取非菜单节点
     * @return 非菜单节点
     */
    private List<ViewVO> getNotMenuNode() {
        // 非菜单节点
        QueryWrapper<View> notMenuWrapper = new QueryWrapper<>();
        notMenuWrapper.eq("type", ViewType.PAGE); // 筛选为页面的节点

        List<View> notMenus = list(notMenuWrapper);
        return ViewDtoConverter.INSTANCE.toViewResponseList(notMenus);
    }
}




