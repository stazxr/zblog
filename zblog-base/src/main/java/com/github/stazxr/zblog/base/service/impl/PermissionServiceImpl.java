package com.github.stazxr.zblog.base.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.base.domain.bo.MenuMeta;
import com.github.stazxr.zblog.base.domain.dto.PermissionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.enums.PermissionType;
import com.github.stazxr.zblog.base.domain.vo.MenuVo;
import com.github.stazxr.zblog.base.domain.vo.PermCodeVo;
import com.github.stazxr.zblog.base.domain.vo.PermissionVo;
import com.github.stazxr.zblog.base.mapper.PermissionMapper;
import com.github.stazxr.zblog.base.mapper.RolePermMapper;
import com.github.stazxr.zblog.base.service.PermissionService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.EntityValidatedException;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.util.EntityValidated;
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

    private final RolePermMapper rolePermMapper;

    /**
     * 查询权限列表
     *
     * @param queryDto 查询参数
     * @return permsList
     */
    @Override
    public List<PermissionVo> queryPermList(PermissionQueryDto queryDto) {
        List<PermissionVo> permissionVos = permissionMapper.queryPermList(queryDto);
        Map<Long, List<PermissionVo>> permPidGroupMap = permissionVos.stream().collect(
                Collectors.groupingBy(v -> v.getPid() == null ? Constants.TOP_PERM_ID : v.getPid())
        );

        List<PermissionVo> result = new ArrayList<>();
        Map<Long, Set<Long>> pidIdsMap = parsePermPidGroupMap(permPidGroupMap);
        Set<Long> firstPid = calculateFirstFirstPid(pidIdsMap);
        for (Long pid : firstPid) {
            List<PermissionVo> permList = permPidGroupMap.get(pid);
            fetchPermVoChildren(permList, permPidGroupMap);
            result.addAll(permList);
        }
        return result;
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

    /**
     * 查找所有注册的权限编码
     *
     * @return permCodes
     */
    @Override
    public List<PermCodeVo> queryPermCodes() {
        return permissionMapper.queryPermCodes();
    }

    /**
     * 查询权限详情
     *
     * @param permId 权限ID
     * @return PermissionVo
     */
    @Override
    public PermissionVo queryPermDetail(Long permId) {
        Assert.notNull(permId, "权限ID不能为空");
        PermissionVo permission = permissionMapper.queryPermDetail(permId);
        Assert.notNull(permission, "数据不存在，权限ID为：" + permId);
        return permission;
    }

    /**
     * 新增权限
     *
     * @param permission 权限
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPermission(Permission permission) {
        checkPermission(permission);
        if (!save(permission)) {
            throw new ServiceException(ResultCode.ADD_FAILED);
        }
    }

    /**
     * 编辑权限
     *
     * @param permission 权限
     */
    @Override
    public void editPermission(Permission permission) {
        checkPermission(permission);
        if (!updateById(permission)) {
            throw new ServiceException(ResultCode.EDIT_FAILED);
        }
    }

    /**
     * 删除权限
     *
     * @param permId 权限ID
     */
    @Override
    public void deletePermission(Long permId) {
        Permission permission = permissionMapper.selectById(permId);
        Assert.notNull(permission, "待删除权限不存在，权限ID为：" + permId);

        Permission param = new Permission();
        param.setPid(permId);

        Long childCount = permissionMapper.selectCount(queryBuild().eq(Permission::getPid, permId));
        if (childCount > 0) {
            throw new ServiceException("存在子节点，无法删除，请先删除子节点");
        }

        if (removeById(permId)) {
            rolePermMapper.deleteByPermId(permId);
            return;
        }

        throw new ServiceException(ResultCode.DELETE_FAILED);
    }

    private void checkPermission(Permission permission) {
        // 非空校验
        emptyCheck(permission);

        // 检查权限名称是否存在
        Permission dbPerm = permissionMapper.findByPermName(permission.getPermName());
        if (dbPerm != null && !dbPerm.getId().equals(permission.getId())) {
            throw new EntityValidatedException("权限名称已存在");
        }

        // PID校验
        boolean parentIsFrame = false;
        int parentType = PermissionType.DIR.getType();
        if (permission.getPid() == null || permission.getPid() == 0L) {
            permission.setPid(null);
        } else {
            Permission parent = permissionMapper.selectById(permission.getPid());
            EntityValidated.notNull(parent, "上级类目不存在");
            parentType = parent.getPermType();
            parentIsFrame = parent.getIFrame();
        }

        // 赋默认值
        if (PermissionType.DIR.getType().equals(permission.getPermType())) {
            EntityValidated.notNull(permission.getIFrame(), "请选择是否外链");
            EntityValidated.notNull(permission.getHidden(), "请选择是否可见");
            EntityValidated.notNull(permission.getRouterPath(), "路由地址不能为空");
            permission.setPermCode(null);
            permission.setComponentName(null);
            permission.setComponentPath(null);
            permission.setCache(null);
            if (permission.getIFrame()) {
                if (permission.getPid() != null) {
                    throw new EntityValidatedException("目录如果是外链，上级只能是顶级类目");
                }
            } else {
                if (parentIsFrame || !PermissionType.DIR.getType().equals(parentType)) {
                    throw new EntityValidatedException("目录的上级只能是目录，且上级目录不允许为外链菜单");
                }
            }
        } else if (PermissionType.MENU.getType().equals(permission.getPermType())) {
            EntityValidated.notNull(permission.getIFrame(), "请选择是否外链");
            EntityValidated.notNull(permission.getHidden(), "请选择是否可见");
            EntityValidated.notNull(permission.getCache(), "请选择是否缓存");
            EntityValidated.notNull(permission.getRouterPath(), "路由地址不能为空");
            if (permission.getIFrame()) {
                permission.setPermCode(null);
                permission.setComponentName(null);
                permission.setComponentPath(null);
                String http = "http://", https = "https://";
                String routerPath = permission.getRouterPath().toLowerCase(Locale.ROOT);
                if (!routerPath.startsWith(http) && !routerPath.startsWith(https)) {
                    throw new EntityValidatedException("外链必须以http://或者https://开头");
                }
            } else {
                EntityValidated.notNull(permission.getComponentPath(), "组件路径不能为空");
                EntityValidated.notNull(permission.getComponentName(), "组件名称不能为空");
                dbPerm = permissionMapper.findByComponentName(permission.getComponentName());
                if (dbPerm != null && !dbPerm.getId().equals(permission.getId())) {
                    throw new EntityValidatedException("组件名称已存在");
                }
            }

            if (parentIsFrame || !PermissionType.DIR.getType().equals(parentType)) {
                throw new EntityValidatedException("菜单的上级只能是目录，且上级目录不允许为外链菜单");
            }
        } else if (PermissionType.BTN.getType().equals(permission.getPermType())) {
            permission.setIcon(null);
            permission.setIFrame(null);
            permission.setHidden(null);
            permission.setCache(null);
            permission.setComponentName(null);
            permission.setComponentPath(null);
            permission.setRouterPath(null);
            if (parentIsFrame || !PermissionType.MENU.getType().equals(parentType)) {
                throw new EntityValidatedException("按钮的上级只能是菜单，且上级菜单不允许为外链菜单");
            }
        } else {
            throw new EntityValidatedException("非法的权限类型：" + permission.getPermType());
        }
    }

    private void emptyCheck(Permission permission) {
        EntityValidated.notNull(permission.getPermName(), "权限名称不能为空");
        EntityValidated.notNull(permission.getPermLevel(), "访问级别不能为空");
        EntityValidated.notNull(permission.getEnabled(), "权限状态不能为空");
        EntityValidated.notNull(permission.getSort(), "权限排序不能为空");
        EntityValidated.notNull(permission.getPermType(), "权限类型不能为空");
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

    private void fetchPermVoChildren(List<PermissionVo> permList, Map<Long, List<PermissionVo>> permPidGroupMap) {
        if (permList != null && !permList.isEmpty()) {
            for (PermissionVo perm : permList) {
                List<PermissionVo> childrenList = permPidGroupMap.get(perm.getId());
                fetchPermVoChildren(childrenList, permPidGroupMap);
                perm.setChildren(childrenList);
            }
        }
    }

    private List<MenuVo> parsePermToMenu(List<Permission> permTreeList) {
        List<MenuVo> result = new ArrayList<>();
        permTreeList.forEach(permission -> {
            MenuVo menuVo = new MenuVo();
            menuVo.setName(StringUtils.isNotEmpty(permission.getComponentName()) ? permission.getComponentName() : permission.getPermName());
            boolean isTopAndNotFrame = permission.getPid() == null && !permission.getIFrame();
            menuVo.setPath(isTopAndNotFrame ? "/" + permission.getRouterPath() : permission.getRouterPath());
            menuVo.setHidden(permission.getHidden());
            if (!permission.getIFrame()) {
                if (permission.getPid() == null) {
                    // 一级菜单默认组件为Layout
                    menuVo.setComponent(StringUtils.isEmpty(permission.getComponentPath()) ? "Layout" : permission.getComponentPath());
                } else if (permission.getPermType() == 0) {
                    // 如果不是一级菜单，并且菜单类型为目录，则代表是多级菜单，默认组件为ParentView
                    menuVo.setComponent(StringUtils.isEmpty(permission.getComponentPath()) ? "ParentView" : permission.getComponentPath());
                } else if (StringUtils.isNotBlank(permission.getComponentPath())) {
                    menuVo.setComponent(permission.getComponentPath());
                }
            }
            boolean noCache = permission.getCache() == null || !permission.getCache();
            menuVo.setMeta(new MenuMeta(permission.getPermName(), permission.getIcon(), noCache));

            List<Permission> childrenList = permission.getChildren();
            if (CollectionUtil.isNotEmpty(childrenList)) {
                menuVo.setAlwaysShow(true);
                menuVo.setRedirect("noredirect");
                menuVo.setChildren(parsePermToMenu(childrenList));
            } else if (permission.getPid() == null) {
                // 一级菜单并且没有子菜单
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

    private Map<Long, Set<Long>> parsePermPidGroupMap(Map<Long, List<PermissionVo>> permPidGroupMap) {
        Map<Long, Set<Long>> result = new HashMap<>(permPidGroupMap.size());
        for (Long pid : permPidGroupMap.keySet()) {
            Set<Long> ids = new HashSet<>();
            for (PermissionVo permissionVo : permPidGroupMap.get(pid)) {
                ids.add(permissionVo.getId());
            }
            result.put(pid, ids);
        }
        return result;
    }

    /**
     * 计算一级PID列表
     *
     * @param pidIdsMap pid id 的对应关系
     * @return 需要一级展示的PID列表
     */
    private Set<Long> calculateFirstFirstPid(Map<Long, Set<Long>> pidIdsMap) {
        Set<Long> result = new HashSet<>();
        Loop: for (Long pid : pidIdsMap.keySet()) {
            for (Set<Long> ids: pidIdsMap.values()) {
                if (ids.contains(pid)) {
                    continue Loop;
                }
            }
            result.add(pid);
        }
        return result;
    }

    private LambdaQueryWrapper<Permission> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
