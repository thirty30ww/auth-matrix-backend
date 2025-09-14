package com.thirty.user.service.basic;

import com.baomidou.mybatisplus.extension.service.IService;
import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【view(页面表)】的数据库操作Service
* @createDate 2025-08-03 10:06:39
*/
public interface ViewService extends IService<View> {
    /**
     * 获取视图列表（不包含DIRECTORY）
     * @param keyword 视图名称
     * @return 视图列表
     */
    List<View> getNotDirectoryAndButtonViews(String keyword);

    /**
     * 获取非目录视图VO列表
     * @param keyword 视图名称
     * @return 非目录视图VO列表
     */
    List<ViewVO> getNotDirectoryAndButtonViewVOS(String keyword);

    /**
     * 获取目录VO列表
     * @return 目录VO列表
     */
    List<ViewVO> getDirectoryViewVOS();

    /**
     * 获取目录列表
     * @return 目录列表
     */
    List<View> getDirectoryViews();

    /**
     * 获取菜单和按钮列表
     * @return 菜单和按钮列表
     */
    List<View> getMenuAndButtonViews();

    /**
     * 获取菜单和按钮VO列表
     * @return 菜单和按钮VO列表
     */
    List<ViewVO> getMenuAndButtonViewVOS();

    /**
     * 获取菜单列表
     * @return 菜单列表
     */
    List<View> getMenuViews();

    /**
     * 获取非页面视图VO列表
     * @return 非页面视图VO列表
     */
    List<ViewVO> getMenuViewVOS();

    /**
     * 获取页面列表（仅包含PAGE）
     * @return 页面列表
     */
    List<View> getPageViews();

    /**
     * 获取页面VO列表
     * @return 页面VO列表
     */
    List<ViewVO> getPageViewVOS();

    /**
     * 获取视图的所有祖先ID（不包含当前视图）
     * @param viewId 视图ID
     * @return 祖先ID列表
     */
    List<Integer> getAncestorIds(Integer viewId);

    /**
     * 获取视图列表的所有祖先ID（不包含当前视图）
     * @param viewIds 视图ID列表
     * @return 祖先ID列表
     */
    List<Integer> getAncestorIds(List<Integer> viewIds);

    /**
     * 获取视图的所有后代ID（不包含当前视图）
     * @param viewId 视图ID
     * @return 后代ID列表
     */
    List<Integer> getDescendantIds(Integer viewId);

    /**
     * 获取视图列表的所有后代ID（不包含当前视图）
     * @param viewIds 视图ID列表
     * @return 后代ID列表
     */
    List<Integer> getDescendantIds(List<Integer> viewIds);

    /**
     * 获取视图列表的所有权限码
     * @param viewIds 视图ID列表
     * @return 权限码列表
     */
    List<String> getPermissionCodes(List<Integer> viewIds);

    /**
     * 修改视图
     * @param view 视图
     */
    void modifyView(View view);

    /**
     * 尾插法添加视图
     * @param view 视图
     */
    void tailInsert(View view);

    /**
     * 连接视图的邻居节点
     * @param view 视图
     */
    void connectNeighborViews(View view);

    /**
     * 删除视图
     * @param viewId 视图ID
     */
    void deleteView(Integer viewId);

    /**
     * 视图上移
     * @param viewId 视图ID
     */
    void moveUp(Integer viewId);

    /**
     * 视图下移
     * @param viewId 视图ID
     */
    void moveDown(Integer viewId);

    /**
     * 获取视图的尾节点
     * @param parentId 父节点ID
     * @return 尾节点
     */
    View getTailNode(Integer parentId);

    /**
     * 获取视图的子节点
     * @param parentId 父节点ID
     * @return 子节点列表
     */
    List<View> getByParentId(Integer parentId);
}
