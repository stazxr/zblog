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
import com.github.stazxr.zblog.util.collection.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单管理业务实现层
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

        // 根据用户类型查询权限列表
        List<Permission> permList;
        if (UserType.ADMIN_USER.getType().equals(loginUser.getUserType())) {
            permList = menuMapper.selectAllMenu();
        } else {
            permList = menuMapper.selectMenuByUserId(loginUser.getId());
        }

        // 根据 ID, PID 构建权限树
        List<Permission> permTreeList = buildPermTree(permList);

        // 讲权限树转为菜单列表
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
            List<Permission> childrenList = perm.getChildren();
            if (CollectionUtils.isEmpty(childrenList)) {
                menuVo.setAlwaysShow(false); // 上层目录不展示
                addSubMenuForTopLevelMenu(menuVo, perm);
            } else {
                menuVo.setChildren(parsePermToMenu(childrenList));
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

        // 设置路由名称（只有菜单类型涉及）
        if (PermissionType.MENU.getType().equals(perm.getPermType())) {
            menuVo.setName(perm.getComponentName());
        } else {
            // 目录和外链禁止重定向
            menuVo.setRedirect("noredirect");
        }

        // 设置路由路径
        if (perm.getPid() == null && !PermissionType.LINK.getType().equals(perm.getPermType())) {
            menuVo.setPath("/" + perm.getRouterPath());
        } else {
            menuVo.setPath(perm.getRouterPath());
        }

        // 设置菜单是否显示
        menuVo.setShow(!perm.getHidden());
        SecurityUser loginUser = SecurityUtils.getLoginUser();
        if (UserType.ADMIN_USER.getType().equals(loginUser.getUserType())) {
            menuVo.setShow(true);
        }

        // 设置菜单组件
        menuVo.setComponent(determineComponentForMenu(perm));

        // 设置元数据（菜单标题，菜单图标，菜单是否缓存）
        boolean cache = perm.getCacheable() != null && perm.getCacheable();
        boolean breadcrumb = !PermissionType.LINK.getType().equals(perm.getPermType());
        menuVo.setMeta(new MenuMetaVo(perm.getPermName(), perm.getIcon(), cache, breadcrumb));

        return menuVo;
    }

    /**
     * 获取组件路径
     *
     * @param perm 权限对象
     * @return 组件路径
     */
    private String determineComponentForMenu(Permission perm) {
        if (perm.getPid() == null) {
            return "Layout";
        } else if (!PermissionType.MENU.getType().equals(perm.getPermType())) {
            return "ParentView";
        } else {
            return perm.getComponentPath();
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
        if (PermissionType.DIR.getType().equals(perm.getPermType())) {
            // 目录
            menuVo.setShow(false);
        } else {
            if (perm.getPid() != null) {
                return;
            }

            MenuVo subMenuVo = new MenuVo();
            MenuMetaVo metaVo = menuVo.getMeta();
            if (PermissionType.LINK.getType().equals(perm.getPermType())) {
                // 外链
                subMenuVo.setPath(perm.getRouterPath());
            } else {
                // 菜单
                subMenuVo.setPath("index");
                subMenuVo.setName(menuVo.getName());
                subMenuVo.setComponent(perm.getComponentPath());
            }

            // 设置其他信息
            menuVo.setComponent("Layout");
            menuVo.setShow(true);
            subMenuVo.setShow(true);
            menuVo.setName(null);
            menuVo.setMeta(null);
            subMenuVo.setMeta(metaVo);

            // 构建新的菜单对象
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

