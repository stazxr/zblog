package com.github.stazxr.zblog.base.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.component.security.handler.UserCacheHandler;
import com.github.stazxr.zblog.base.domain.bo.MenuMeta;
import com.github.stazxr.zblog.base.domain.dto.query.PermissionQueryDto;
import com.github.stazxr.zblog.base.domain.dto.RolePermDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.enums.PermissionType;
import com.github.stazxr.zblog.base.domain.vo.*;
import com.github.stazxr.zblog.base.mapper.*;
import com.github.stazxr.zblog.base.service.PermissionService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.exception.DataValidatedException;
import com.github.stazxr.zblog.core.util.CacheUtils;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.log.domain.dto.LogQueryDto;
import com.github.stazxr.zblog.log.domain.vo.LogVo;
import com.github.stazxr.zblog.log.mapper.LogMapper;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.math.MathUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.stazxr.zblog.base.util.Constants.CacheKey.interfaceLevel;

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

    private final RouterMapper routerMapper;

    private final RolePermMapper rolePermMapper;

    private final UserCacheHandler userCacheHandler;

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
        Set<Long> firstPid = MathUtils.calculateFirstPid(pidIdsMap);
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
        Assert.notNull(permId, "参数【permId】不能为空");
        PermissionVo permissionVo = permissionMapper.selectPermDetail(permId);
        Assert.notNull(permissionVo, "查询权限信息失败，权限【" + permId + "】不存在");
        return permissionVo;
    }

    /**
     * 查询权限对应的接口列表
     *
     * @param queryDto 查询参数
     * @return interfaceList
     */
    @Override
    public PageInfo<InterfaceVo> queryPermInterfaces(PermissionQueryDto queryDto) {
        Assert.notNull(queryDto.getPermId(), "参数【permId】不能为空");
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        List<InterfaceVo> dataList = interfaceMapper.selectPermInterfaces(queryDto.getPermId());
        return new PageInfo<>(dataList);
    }

    /**
     * 查询权限对应的角色列表
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    @Override
    public PageInfo<RoleVo> queryPermRoles(PermissionQueryDto queryDto) {
        Assert.notNull(queryDto.getPermId(), "参数【permId】不能为空");
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        List<RoleVo> dataList = roleMapper.selectPermRoles(queryDto.getPermId(), queryDto.getBlurry());
        return new PageInfo<>(dataList);
    }

    /**
     * 查询权限对应的日志列表
     *
     * @param queryDto 查询参数
     * @return logList
     */
    @Override
    public PageInfo<LogVo> queryPermLogs(PermissionQueryDto queryDto) {
        Assert.notNull(queryDto.getPermId(), "参数【permId】不能为空");
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        LogQueryDto logQueryDto = new LogQueryDto();
        logQueryDto.setPermId(queryDto.getPermId());
        List<LogVo> dataList = logMapper.selectLogList(logQueryDto);
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
        permission.setId(null);
        checkPermission(permission);
        Assert.isTrue(permissionMapper.insert(permission) != 1, "新增失败");
        removeCache(permission);
    }

    /**
     * 编辑权限
     *
     * @param permission 权限
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPermission(Permission permission) {
        // 查询菜单信息，赋值菜单类型和是否外联，这两项不允许编辑
        Assert.notNull(permission.getId(), "参数【id】不能为空");
        Permission dbPerm = permissionMapper.selectById(permission.getId());
        DataValidated.isTrue(!dbPerm.getPermType().equals(permission.getPermType()), "权限类型不允许编辑");
        permission.setIFrame(dbPerm.getIFrame());
        checkPermission(permission);
        Assert.isTrue(permissionMapper.updatePermission(permission) != 1, "编辑失败");
        removeCache(permission);
    }

    /**
     * 删除权限
     *
     * @param permId 权限序列
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Long permId) {
        Assert.notNull(permId, "参数【permId】不能为空");
        Permission permission = permissionMapper.selectById(permId);
        Assert.notNull(permission, "待删除权限不存在，权限序列为：" + permId);

        Permission param = new Permission();
        param.setPid(permId);
        Long childCount = permissionMapper.selectCount(queryBuild().eq(Permission::getPid, permId));
        DataValidated.isTrue(childCount > 0, "存在子节点，无法删除，请先删除子节点");
        Assert.isTrue(permissionMapper.deleteById(permId) != 1, "删除失败");
        rolePermMapper.deleteByPermId(permId);
        removeCache(permission);
    }

    /**
     * 查询用户菜单列表
     *
     * @param userId 用户序列
     * @return menuTree
     */
    @Override
    public List<MenuVo> queryUserMenus(Long userId) {
        // 查询用户对应的菜单权限列表，并构建菜单树，管理员可以查看所有的菜单
        Assert.notNull(userId, "参数【userId】不能为空");
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
        Assert.notNull(rolePermDto.getPermId(), "参数【permId】不能为空");
        Set<Long> roleIds = rolePermDto.getRoleIds();
        if (roleIds == null || roleIds.size() == 0) {
            return;
        }

        rolePermMapper.batchDeleteRolePerm(rolePermDto);
    }

    /**
     * 查询用户的权限列表（权限编码）
     *
     * @param userId 用户序列
     * @return permCodes
     */
    @Override
    public Set<String> queryUserPerms(Long userId) {
        if (userId == null) {
            return new HashSet<>();
        }

        if (Constants.USER_ADMIN_ID.equals(userId)) {
            return routerMapper.selectCode();
        } else {
            return permissionMapper.selectUserPerms(userId);
        }
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
        boolean isExist = dbPerm != null && !dbPerm.getId().equals(permission.getId());
        DataValidated.isTrue(isExist, "权限名称已存在");

        // PID校验
        boolean parentIsFrame = false;
        int parentType = PermissionType.DIR.getType();
        if (permission.getPid() == null || permission.getPid() == 0L) {
            permission.setPid(null);
        } else {
            Permission parent = permissionMapper.selectById(permission.getPid());
            Assert.notNull(parent, "上级类目不存在");
            parentType = parent.getPermType();
            parentIsFrame = parent.getIFrame();
        }

        // 赋默认值
        if (PermissionType.DIR.getType().equals(permission.getPermType())) {
            Assert.notNull(permission.getIFrame(), "请选择是否外链");
            Assert.notNull(permission.getHidden(), "请选择是否可见");
            Assert.isTrue(StringUtils.isBlank(permission.getRouterPath()), "路由地址不能为空");
            permission.setPermCode(null);
            permission.setComponentName(null);
            permission.setComponentPath(null);
            permission.setCache(null);
            if (permission.getIFrame()) {
                DataValidated.isTrue(permission.getPid() != null, "目录如果是外链，上级只能是顶级类目");
            } else {
                boolean isValid = parentIsFrame || !PermissionType.DIR.getType().equals(parentType);
                DataValidated.isTrue(isValid, "目录的上级只能是目录，且上级目录不允许为外链目录");
            }
        } else if (PermissionType.MENU.getType().equals(permission.getPermType())) {
            Assert.notNull(permission.getIFrame(), "请选择是否外链");
            Assert.notNull(permission.getHidden(), "请选择是否可见");
            Assert.notNull(permission.getCache(), "请选择是否缓存");
            Assert.isTrue(StringUtils.isBlank(permission.getRouterPath()), "路由地址不能为空");
            if (permission.getIFrame()) {
                permission.setPermCode(null);
                permission.setComponentName(null);
                permission.setComponentPath(null);
                String http = "http://", https = "https://";
                String routerPath = permission.getRouterPath().toLowerCase(Locale.ROOT);
                boolean isNulHttp = !routerPath.startsWith(http) && !routerPath.startsWith(https);
                DataValidated.isTrue(isNulHttp, "外链必须以http://或者https://开头");
            } else {
                permission.setPermCode(StringUtils.isBlank(permission.getPermCode()) ? null : permission.getPermCode());
                Assert.isTrue(StringUtils.isBlank(permission.getComponentPath()), "组件路径不能为空");
                Assert.isTrue(permission.getComponentPath().startsWith("/"), "组件路径不能以斜杠开头");
                Assert.isTrue(StringUtils.isBlank(permission.getComponentName()), "组件名称不能为空");
                dbPerm = permissionMapper.selectByComponentName(permission.getComponentName());
                DataValidated.isTrue(dbPerm != null && !dbPerm.getId().equals(permission.getId()), "组件名称已存在");
            }

            DataValidated.isTrue(parentIsFrame || !PermissionType.DIR.getType().equals(parentType), "菜单的上级只能是目录，且上级目录不允许为外链目录");
        } else if (PermissionType.BTN.getType().equals(permission.getPermType())) {
            permission.setIcon(null);
            permission.setIFrame(null);
            permission.setHidden(null);
            permission.setCache(null);
            permission.setComponentName(null);
            permission.setComponentPath(null);
            permission.setRouterPath(null);
            DataValidated.notNull(permission.getPermCode(), "权限编码不能为空");
            DataValidated.isTrue(parentIsFrame || !PermissionType.MENU.getType().equals(parentType), "按钮的上级只能是菜单，且上级菜单不允许为外链菜单");
        } else {
            throw new DataValidatedException("非法的权限类型：" + permission.getPermType());
        }
    }

    private void emptyCheck(Permission permission) {
        Assert.isTrue(StringUtils.isBlank(permission.getPermName()), "权限名称不能为空");
        Assert.notNull(permission.getPermLevel(), "访问级别不能为空");
        Assert.notNull(permission.getEnabled(), "权限状态不能为空");
        Assert.notNull(permission.getSort(), "权限排序不能为空");
        Assert.notNull(permission.getPermType(), "权限类型不能为空");
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
                } else if (PermissionType.DIR.getType().equals(permission.getPermType())) {
                    // 如果不是一级菜单，并且菜单类型为目录，则代表是多级菜单，默认组件为ParentView
                    menuVo.setComponent(StringUtils.isEmpty(permission.getComponentPath()) ? "ParentView" : permission.getComponentPath());
                } else {
                    menuVo.setComponent(StringUtils.isBlank(permission.getComponentPath()) ? null : permission.getComponentPath());
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

    private void removeCache(Permission permission) {
        userCacheHandler.clean();
        if (permission != null && StringUtils.isNotBlank(permission.getPermCode())) {
            List<InterfaceVo> interfaces = interfaceMapper.selectInterfacesByCode(permission.getPermCode());
            if (!interfaces.isEmpty()) {
                interfaces.forEach(interfaceVo -> {
                    String key = interfaceLevel.cacheKey().concat(":").concat(interfaceVo.getUri()).concat("_").concat(interfaceVo.getMethod());
                    CacheUtils.remove(key);
                });
            }
        }
    }

    private LambdaQueryWrapper<Permission> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
