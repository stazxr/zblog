package com.github.stazxr.zblog.base.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.base.domain.bo.MenuMeta;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.vo.MenuVo;
import com.github.stazxr.zblog.base.mapper.PermissionMapper;
import com.github.stazxr.zblog.base.service.PermissionService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限业务实现层
 *
 * @author SunTao
 * @since 2020-11-16
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    private final PermissionMapper permissionMapper;

    /**
     * 查询用户权限列表
     *
     * @param userId 用户序列
     * @return Permissions
     */
    @Override
    public List<Permission> queryPermsByUserId(Long userId) {
        return null;
    }

    /**
     * 构造前端菜单模型
     *
     * @param userId 用户序列
     * @return menuTree
     */
    @Override
    public List<MenuVo> buildMenus(Long userId) {
        Assert.notNull(userId, "用户ID不能为空");

        // 查询用户对应的菜单权限列表，并构建菜单树
        boolean isAdmin = Constants.USER_ADMIN_ID.equals(userId);
        List<Permission> permList = isAdmin ? permissionMapper.selectMenu() : permissionMapper.selectMenuByUserId(userId);
        List<Permission> permTreeList = buildPermTree(permList);
        return parsePermToMenu(permTreeList);
    }

    private List<Permission> buildPermTree(List<Permission> permList) {
        Map<Long, List<Permission>> permPidGroupMap = permList.stream().collect(
                Collectors.groupingBy(v -> v.getPid() == null ? Constants.TOP_PERM_ID : v.getPid())
        );
        List<Permission> rootList = permPidGroupMap.getOrDefault(Constants.TOP_PERM_ID, Collections.emptyList());
        fetchChildren(rootList, permPidGroupMap);
        return rootList;
    }

    private void fetchChildren(List<Permission> permList, Map<Long, List<Permission>> permPidGroupMap) {
        if (permList != null && !permList.isEmpty()) {
            for (Permission perm : permList) {
                List<Permission> childrenList = permPidGroupMap.get(perm.getId());
                fetchChildren(childrenList, permPidGroupMap);
                perm.setChildren(childrenList);
            }
        }
    }

    private List<MenuVo> parsePermToMenu(List<Permission> permTreeList) {
        List<MenuVo> result = new ArrayList<>();
        permTreeList.forEach(permission -> {
            MenuVo menuVo = new MenuVo();
            menuVo.setName(StringUtils.isNotEmpty(permission.getComponentName()) ? permission.getComponentName() : permission.getPermName());
            menuVo.setPath(permission.getPid() == null ? "/" + permission.getRouterPath() : permission.getRouterPath());
            menuVo.setHidden(permission.getHidden());
            if (!permission.getIFrame()) {
                if (permission.getPid() == null) {
                    menuVo.setComponent(StringUtils.isEmpty(permission.getComponentPath()) ? "Layout" : permission.getComponentPath());
                } else if (permission.getPermType() == 0) {
                    menuVo.setComponent(StringUtils.isEmpty(permission.getComponentPath()) ? "ParentView" : permission.getComponentPath());
                } else if (StringUtils.isNoneBlank(permission.getComponentPath())) {
                    menuVo.setComponent(permission.getComponentPath());
                }
            }
            menuVo.setMeta(new MenuMeta(permission.getPermName(), permission.getIcon(), !permission.getCache()));

            List<Permission> childrenList = permission.getChildren();
            if (CollectionUtil.isNotEmpty(childrenList)) {
                menuVo.setAlwaysShow(true);
                menuVo.setRedirect("noredirect");
                menuVo.setChildren(parsePermToMenu(childrenList));
            } else if (permission.getPid() == null) {
                MenuVo menuVo1 = new MenuVo();
                menuVo1.setMeta(menuVo.getMeta());
                if (!permission.getIFrame()) {
                    menuVo1.setPath("index");
                    menuVo1.setName(menuVo.getName());
                    menuVo1.setComponent(menuVo.getComponent());
                } else {
                    menuVo1.setPath(permission.getRouterPath());
                }
                menuVo.setName(null);
                menuVo.setMeta(null);
                menuVo.setComponent("Layout");
                List<MenuVo> list1 = new ArrayList<>();
                list1.add(menuVo1);
                menuVo.setChildren(list1);
            }

            result.add(menuVo);
        });

        return result;
    }
}
