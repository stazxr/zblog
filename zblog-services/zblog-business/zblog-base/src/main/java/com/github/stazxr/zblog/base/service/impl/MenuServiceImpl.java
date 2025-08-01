package com.github.stazxr.zblog.base.service.impl;

import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.security.core.UserType;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.enums.PermissionType;
import com.github.stazxr.zblog.base.domain.vo.MenuMetaVo;
import com.github.stazxr.zblog.base.domain.vo.MenuVo;
import com.github.stazxr.zblog.base.mapper.MenuMapper;
import com.github.stazxr.zblog.base.service.MenuService;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.collection.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单服务默认实现
 *
 * @author SunTao
 * @since 2024-12-01
 */
@Service
public class MenuServiceImpl implements MenuService {
    private MenuMapper menuMapper;

    /**
     * 查询用户菜单列表（树）
     *
     * @return {@link List<MenuVo>} 返回构建的用户菜单树
     */
    @Override
    public List<MenuVo> queryUserMenuTree() {
        // 获取用户信息
        SecurityUser loginUser = SecurityUtils.getLoginUser();
        boolean isAdmin = UserType.ADMIN_USER.getType().equals(loginUser.getUserType());

        // 根据用户类型查询菜单权限
        List<Permission> permList = isAdmin ? menuMapper.selectAllMenu() : menuMapper.selectMenuByUserId(loginUser.getId());

        // 构建权限树, 并权限为菜单列表（树）
        List<Permission> permTreeList = buildPermTree(permList);
        return parsePermToMenu(permTreeList);
    }

    /**
     * 构建权限树
     * <p>
     * 该方法将扁平化的权限列表构建成树形结构。
     * </p>
     *
     * @param permList 权限列表
     * @return 根节点权限列表
     */
    private List<Permission> buildPermTree(List<Permission> permList) {
        // 按 PID 对权限进行分组
        Map<Long, List<Permission>> permPidGroupMap = permList.stream().collect(
                Collectors.groupingBy(p -> p.getPid() == null ? Constants.TOP_PERM_ID : p.getPid())
        );

        // 获取根节点
        List<Permission> rootList = permPidGroupMap.getOrDefault(Constants.TOP_PERM_ID, Collections.emptyList());

        // 递归设置子节点
        fetchChildren(rootList, permPidGroupMap);
        return rootList;
    }

    /**
     * 递归获取子权限
     * <p>
     * 该方法通过递归的方式将子权限赋值到父权限的 children 属性中。
     * </p>
     *
     * @param permList 当前权限列表
     * @param permPidGroupMap 按父权限ID分组的权限列表
     */
    private void fetchChildren(List<Permission> permList, Map<Long, List<Permission>> permPidGroupMap) {
        if (permList != null && !permList.isEmpty()) {
            for (Permission perm : permList) {
                List<Permission> childrenList = permPidGroupMap.get(perm.getId());
                if (!CollectionUtils.isEmpty(childrenList)) {
                    perm.setChildren(childrenList);
                    fetchChildren(childrenList, permPidGroupMap);
                }
            }
        }
    }

    /**
     * 将权限树转换为菜单VO列表
     * <p>
     * 该方法将权限树转换为前端所需的菜单格式。
     * </p>
     *
     * @param permTreeList 权限树
     * @return 转换后的菜单VO列表
     */
    private List<MenuVo> parsePermToMenu(List<Permission> permTreeList) {
        List<MenuVo> menuVoList = new ArrayList<>();

        for (Permission perm : permTreeList) {
            // 将权限转换为菜单VO
            MenuVo menuVo = createMenuVoFromPermission(perm);

            // 获取子节点权限，如果存在子节点，则递归处理
            List<Permission> childPermTreeList = perm.getChildren();
            if (!CollectionUtils.isEmpty(childPermTreeList)) {
                menuVo.setAlwaysShow(true);
                menuVo.setRedirect("noredirect");
                menuVo.setChildren(parsePermToMenu(perm.getChildren()));
            } else {
                addSubMenuForTopLevelMenu(menuVo, perm);
            }

            menuVoList.add(menuVo);
        }

        return menuVoList;
    }

    /**
     * 根据权限信息创建菜单VO
     * <p>
     * 将单个权限对象转换为菜单VO对象，并填充基本信息。
     * </p>
     *
     * @param perm 权限对象
     * @return 菜单VO对象
     */
    private MenuVo createMenuVoFromPermission(Permission perm) {
        MenuVo menuVo = new MenuVo();
        menuVo.setName(StringUtils.isNotEmpty(perm.getComponentName()) ? perm.getComponentName() : perm.getPermName());

        boolean isTopAndNotFrame = perm.getPid() == null && !perm.getIFrame();
        menuVo.setPath(isTopAndNotFrame ? "/" + perm.getRouterPath() : perm.getRouterPath());

        // 设置菜单是否显示
        menuVo.setShow(!perm.getHidden());

        if (!perm.getIFrame()) {
            menuVo.setComponent(determineComponentForMenu(perm));
        }

        // 设置元数据（菜单标题，菜单图标，菜单是否缓存）
        boolean cache = perm.getCacheable() != null && perm.getCacheable();
        menuVo.setMeta(new MenuMetaVo(perm.getPermName(), perm.getIcon(), cache, true));

        return menuVo;
    }

    /**
     * 根据权限类型确定菜单组件
     * <p>
     * 判断权限类型，确定菜单的组件路径。一级菜单、目录菜单、普通菜单的组件设置规则不同。
     * </p>
     *
     * @param perm 权限对象
     * @return 组件路径
     */
    private String determineComponentForMenu(Permission perm) {
        if (perm.getPid() == null) {
            // 一级菜单默认组件为Layout
            return StringUtils.isEmpty(perm.getComponentPath()) ? "Layout" : perm.getComponentPath();
        } else if (PermissionType.DIR.getType().equals(perm.getPermType())) {
            // 目录类型菜单
            return StringUtils.isEmpty(perm.getComponentPath()) ? "ParentView" : perm.getComponentPath();
        } else {
            return StringUtils.isBlank(perm.getComponentPath()) ? null : perm.getComponentPath();
        }
    }

    /**
     * 为一级菜单没有子菜单的情况添加子菜单项
     * <p>
     * 如果一级菜单没有子菜单，创建一个子菜单并设置其相关属性。
     * </p>
     *
     * @param menuVo 菜单VO
     * @param perm 权限对象
     */
    private void addSubMenuForTopLevelMenu(MenuVo menuVo, Permission perm) {
        if (perm.getPid() == null) {
            MenuVo subMenuVo = new MenuVo();
            subMenuVo.setMeta(menuVo.getMeta());

            if (!perm.getIFrame()) {
                subMenuVo.setPath("index");
                subMenuVo.setName(menuVo.getName());
                subMenuVo.setComponent(menuVo.getComponent());
            } else {
                subMenuVo.setPath(perm.getRouterPath());
            }

            menuVo.setName(null);
            menuVo.setMeta(null);
            menuVo.setComponent("Layout");

            List<MenuVo> subMenus = new ArrayList<>();
            subMenus.add(subMenuVo);
            menuVo.setChildren(subMenus);
        }
    }

    @Autowired
    public void setMenuMapper(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }
}

