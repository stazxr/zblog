package com.github.stazxr.zblog.base.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.bo.MenuMeta;
import com.github.stazxr.zblog.base.domain.dto.PermissionQueryDto;
import com.github.stazxr.zblog.base.domain.dto.RolePermDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.enums.PermissionType;
import com.github.stazxr.zblog.base.domain.vo.*;
import com.github.stazxr.zblog.base.mapper.InterfaceMapper;
import com.github.stazxr.zblog.base.mapper.PermissionMapper;
import com.github.stazxr.zblog.base.mapper.RoleMapper;
import com.github.stazxr.zblog.base.mapper.RolePermMapper;
import com.github.stazxr.zblog.base.service.PermissionService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.EntityValidatedException;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.util.EntityValidated;
import com.github.stazxr.zblog.log.domain.vo.LogVo;
import com.github.stazxr.zblog.log.mapper.LogMapper;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.collection.CollectionUtils;
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
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    private final PermissionMapper permissionMapper;

    private final InterfaceMapper interfaceMapper;

    private final RoleMapper roleMapper;

    private final LogMapper logMapper;

    private final RolePermMapper rolePermMapper;

    /**
     * 查询权限列表（树）
     *
     * @param queryDto 查询参数
     * @return permsList
     */
    @Override
    public List<PermissionVo> queryPermTreeList(PermissionQueryDto queryDto) {
        List<PermissionVo> permissionVos = permissionMapper.selectPermList(queryDto);
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

        return queryDto.getNeedTop() != null && queryDto.getNeedTop() ? putTopMenu(result) : result;
    }

    /**
     * 查询权限详情
     *
     * @param permId 权限ID
     * @return PermissionVo
     */
    @Override
    public PermissionVo queryPermDetail(Long permId) {
        Assert.notNull(permId, "权限序列不能为空");
        PermissionVo permission = permissionMapper.selectPermDetail(permId);
        Assert.notNull(permission, "数据不存在，权限序列为：" + permId);
        return permission;
    }

    /**
     * 查询权限可访问的接口列表
     *
     * @param queryDto 查询参数
     * @return interfaceList
     */
    @Override
    public PageInfo<InterfaceVo> queryPermInterfaces(PermissionQueryDto queryDto) {
        queryDto.checkPage();
        Assert.notNull(queryDto.getPermId(), "权限序列不能为空");

        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        List<InterfaceVo> dataList = interfaceMapper.selectPermInterfaces(queryDto.getPermId());
        return new PageInfo<>(dataList);
    }

    /**
     * 查询可以访问权限的角色列表
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    @Override
    public PageInfo<RoleVo> queryPermRoles(PermissionQueryDto queryDto) {
        queryDto.checkPage();
        Assert.notNull(queryDto.getPermId(), "权限序列不能为空");

        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        List<RoleVo> dataList = roleMapper.selectPermRoles(queryDto.getPermId(), queryDto.getBlurry());
        return new PageInfo<>(dataList);
    }

    /**
     * 查询权限的操作日志列表
     *
     * @param queryDto 查询参数
     * @return logList
     */
    @Override
    public PageInfo<LogVo> queryPermLogs(PermissionQueryDto queryDto) {
        queryDto.checkPage();
        Assert.notNull(queryDto.getPermId(), "权限序列不能为空");

        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        Map<String, Object> param = new HashMap<>(CollectionUtils.mapSize(1));
        param.put("permId", queryDto.getPermId());
        List<LogVo> dataList = logMapper.selectLogList(param);
        return new PageInfo<>(dataList);
    }

    /**
     * 查询所有的权限编码
     *
     * @param searchKey 查询条件
     * @return PermCodeVo
     */
    @Override
    public List<PermCodeVo> queryPermCodes(String searchKey) {
        return permissionMapper.selectPermCodes(searchKey);
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
    @Transactional(rollbackFor = Exception.class)
    public void editPermission(Permission permission) {
        checkPermission(permission);
        if (!updateById(permission)) {
            throw new ServiceException(ResultCode.EDIT_FAILED);
        }
    }

    /**
     * 删除权限
     *
     * @param permId 权限序列
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Long permId) {
        Permission permission = permissionMapper.selectById(permId);
        Assert.notNull(permission, "待删除权限不存在，权限序列为：" + permId);

        Permission param = new Permission();
        param.setPid(permId);

        Long childCount = permissionMapper.selectCount(queryBuild().eq(Permission::getPid, permId));
        if (childCount > 0) {
            throw new ServiceException("存在子节点，无法删除，请先删除子节点");
        }

        if (removeById(permId)) {
            // 删除角色 - 权限的中间数据
            rolePermMapper.deleteByPermId(permId);
            return;
        }

        throw new ServiceException(ResultCode.DELETE_FAILED);
    }

    /**
     * 查询用户菜单列表
     *
     * @param userId 用户序列
     * @return menuTree
     */
    @Override
    public List<MenuVo> queryUserMenus(Long userId) {
        Assert.notNull(userId, "用户序列不能为空");

        // 查询用户对应的菜单权限列表，并构建菜单树，管理员可以查看所有的菜单
        boolean isAdmin = Constants.USER_ADMIN_ID.equals(userId);
        List<Permission> permList = isAdmin ? permissionMapper.selectAllMenu() : permissionMapper.selectMenuByUserId(userId);
        List<Permission> permTreeList = buildPermTree(permList);
        return parsePermToMenu(permTreeList);
    }

    /**
     * 批量删除角色权限
     *
     * @param rolePermDto 角色 - 权限对应信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteRolePerm(RolePermDto rolePermDto) {
        Assert.notNull(rolePermDto.getPermId(), "参数roleId不能为空");
        Set<Long> roleIds = rolePermDto.getRoleIds();
        if (roleIds == null || roleIds.size() == 0) {
            return;
        }

        rolePermMapper.batchDeleteRolePerm(rolePermDto);
    }

    private List<PermissionVo> putTopMenu(List<PermissionVo> permissionVos) {
        PermissionVo top = new PermissionVo();
        top.setId(Constants.TOP_PERM_ID);
        top.setPermName(Constants.TOP_PERM_NAME);
        top.setPermType(PermissionType.DIR.getType());
        top.setChildren(permissionVos == null ? Collections.emptyList() : permissionVos);

        List<PermissionVo> result = new ArrayList<>();
        result.add(top);
        return result;
    }

    private void checkPermission(Permission permission) {
        // 非空校验
        emptyCheck(permission);

        // 检查权限名称是否存在
        Permission dbPerm = permissionMapper.selectByPermName(permission.getPermName());
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
                dbPerm = permissionMapper.selectByComponentName(permission.getComponentName());
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
