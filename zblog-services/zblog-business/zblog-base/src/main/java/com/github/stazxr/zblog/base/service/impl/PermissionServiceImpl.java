package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.router.Resource;
import com.github.stazxr.zblog.bas.security.authz.metadata.ResourceCacheService;
import com.github.stazxr.zblog.bas.security.cache.SecurityUserCache;
import com.github.stazxr.zblog.base.converter.PermissionConverter;
import com.github.stazxr.zblog.base.domain.dto.PermissionDto;
import com.github.stazxr.zblog.base.domain.dto.query.PermissionQueryDto;
import com.github.stazxr.zblog.base.domain.dto.RolePermDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.enums.PermissionType;
import com.github.stazxr.zblog.base.domain.vo.*;
import com.github.stazxr.zblog.base.mapper.*;
import com.github.stazxr.zblog.base.service.PermissionService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.exception.DataValidatedException;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.log.domain.vo.LogVo;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.math.MathUtils;
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

    private final PermissionConverter permissionConverter;

    private final InterfaceMapper interfaceMapper;

    private final RolePermMapper rolePermMapper;

    private final ResourceCacheService resourceCacheService;

    /**
     * 查询权限列表（树）
     *
     * @param queryDto 查询参数
     * @return permsList
     */
    @Override
    public List<PermissionVo> queryPermTree(PermissionQueryDto queryDto) {
        // 查询权限列表
        List<PermissionVo> permissionVos = permissionMapper.selectPermList(queryDto);

        // 按 PID 对权限进行分组
        Map<Long, List<PermissionVo>> permPidGroupMap = permissionVos.stream().collect(
            Collectors.groupingBy(v -> v.getPid() == null ? Constants.TOP_PERM_ID : v.getPid())
        );

        // 构建权限树
        List<PermissionVo> result = new ArrayList<>();
        Map<Long, Set<Long>> pidIdsMap = parsePermPidGroupMap(permPidGroupMap);
        Set<Long> firstPid = MathUtils.calculateFirstPid(pidIdsMap);
        for (Long pid : firstPid) {
            List<PermissionVo> permList = permPidGroupMap.get(pid);
            fetchPermVoChildren(permList, permPidGroupMap);
            result.addAll(permList);
        }

        // 返回权限树
        return queryDto.getNeedTop() != null && queryDto.getNeedTop() ? putTopPerm(result) : result;
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
     * 分页查询权限接口列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<InterfaceVo>
     */
    @Override
    public PageInfo<InterfaceVo> pagePermInterfaces(PermissionQueryDto queryDto) {
        queryDto.checkPage();
        Assert.notNull(queryDto.getPermId(), "参数【permId】不能为空");
        try (Page<InterfaceVo> page = PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize())) {
            List<InterfaceVo> dataList = permissionMapper.selectPermInterfaces(queryDto.getPermId());
            return page.doSelectPageInfo(() -> new PageInfo<>(dataList));
        }
    }

    /**
     * 分页查询权限角色列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<RoleVo>
     */
    @Override
    public PageInfo<RoleVo> pagePermRoles(PermissionQueryDto queryDto) {
        queryDto.checkPage();
        Assert.notNull(queryDto.getPermId(), "参数【permId】不能为空");
        try (Page<RoleVo> page = PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize())) {
            List<RoleVo> dataList = permissionMapper.selectPermRoles(queryDto.getPermId(), queryDto.getBlurry());
            return page.doSelectPageInfo(() -> new PageInfo<>(dataList));
        }
    }

    /**
     * 分页查询权限日志列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<LogVo>
     */
    @Override
    public PageInfo<LogVo> pagePermLogs(PermissionQueryDto queryDto) {
        queryDto.checkPage();
        Assert.notNull(queryDto.getPermId(), "参数【permId】不能为空");
        try (Page<LogVo> page = PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize())) {
            List<LogVo> dataList = permissionMapper.selectPermLogs(queryDto.getPermId(), queryDto.getBlurry());
            return page.doSelectPageInfo(() -> new PageInfo<>(dataList));
        }
    }

    /**
     * 查询权限编码列表
     *
     * @param searchKey 查询条件
     * @return List<PermCodeVo>
     */
    @Override
    public List<PermCodeVo> queryPermCodes(String searchKey) {
        return permissionMapper.selectPermCodes(searchKey);
    }

    /**
     * 根据权限编码查询资源信息
     *
     * @param permCode 权限编码
     * @return Resource
     */
    @Override
    public Resource queryResourceByPermCode(String permCode) {
        return permissionMapper.selectResourceByPermCode(permCode);
    }

    /**
     * 新增权限
     *
     * @param permissionDto 权限信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPermission(PermissionDto permissionDto) {
        // 获取权限信息
        Permission permission = permissionConverter.dtoToEntity(permissionDto);
        permission.setId(null);
        checkPermission(permission);
        Assert.isTrue(permissionMapper.insert(permission) != 1, "新增失败");
        // removeCache(permission);
        if (!permission.getPermType().equals(PermissionType.DIR.getType())) {
            resourceCacheService.clearCache();
        }
    }

    /**
     * 编辑权限
     *
     * @param permissionDto 权限信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPermission(PermissionDto permissionDto) {
//        // 获取权限信息
//        Permission permission = permissionConverter.dtoToEntity(permissionDto);
//
//        // 查询菜单信息，赋值菜单类型和是否外联，这两项不允许编辑
//        Assert.notNull(permission.getId(), "参数【id】不能为空");
//        Permission dbPermission = permissionMapper.selectById(permission.getId());
//        Assert.notNull(dbPermission, "权限不存在");
//
//        DataValidated.isTrue(!dbPerm.getPermType().equals(permission.getPermType()), "权限类型不允许编辑");
//        permission.setIFrame(dbPerm.getIFrame());
//        checkPermission(permission);
//        Assert.isTrue(permissionMapper.updatePermission(permission) != 1, "编辑失败");
//        removeCache(permission);
//        if (!permission.getPermType().equals(PermissionType.DIR.getType()) && dbPerm.getPermLevel().equals(permission.getPermLevel())) {
//            // 如果修改的权限类型不是目录，并且修改了权限访问级别，则清理缓存
//            resourceCacheService.clearCache();
//        }
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
        if (!permission.getPermType().equals(PermissionType.DIR.getType())) {
            resourceCacheService.clearCache();
        }
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

    private List<PermissionVo> putTopPerm(List<PermissionVo> permissionVos) {
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
        // 获取上级权限信息
        boolean parentIsFrame = false;
        int parentType = PermissionType.DIR.getType();
        if (permission.getPid() == null || Constants.TOP_PERM_ID.equals(permission.getPid())) {
            permission.setPid(null);
        } else {
            Permission parentPermission = permissionMapper.selectById(permission.getPid());
            Assert.notNull(parentPermission, "上级权限数据不存在");
            parentType = parentPermission.getPermType();
            parentIsFrame = parentPermission.getIFrame();
        }

        // 赋默认值
        if (PermissionType.DIR.getType().equals(permission.getPermType())) {
            // 目录
            permission.setPermCode(null);
            permission.setCacheable(null);
            permission.setComponentName(null);
            permission.setComponentPath(null);

            Assert.notNull(permission.getIFrame(), "请选择是否外链");
            Assert.notNull(permission.getHidden(), "请选择是否可见");
            Assert.isTrue(StringUtils.isBlank(permission.getRouterPath()), "路由地址不能为空");
            boolean isValid = parentIsFrame || !PermissionType.DIR.getType().equals(parentType);
            DataValidated.isTrue(isValid, "目录的上级只能是目录，且上级目录不允许为外链目录");
        } else if (PermissionType.MENU.getType().equals(permission.getPermType())) {
            // 菜单
            Assert.notNull(permission.getIFrame(), "请选择是否外链");
            Assert.notNull(permission.getHidden(), "请选择是否可见");
            Assert.notNull(permission.getCacheable(), "请选择是否缓存");
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
                // Permission dbPerm = permissionMapper.selectByComponentName(permission.getComponentName());
                // DataValidated.isTrue(dbPerm != null && !dbPerm.getId().equals(permission.getId()), "组件名称已存在");
            }

            DataValidated.isTrue(parentIsFrame || !PermissionType.DIR.getType().equals(parentType), "菜单的上级只能是目录，且上级目录不允许为外链目录");
        } else if (PermissionType.BTN.getType().equals(permission.getPermType())) {
            // 按钮
            permission.setIcon(null);
            permission.setIFrame(null);
            permission.setHidden(null);
            permission.setCacheable(null);
            permission.setRouterPath(null);
            permission.setComponentName(null);
            permission.setComponentPath(null);
            DataValidated.notNull(permission.getPermCode(), "权限编码不能为空");
            DataValidated.isTrue(parentIsFrame || !PermissionType.MENU.getType().equals(parentType), "按钮的上级只能是菜单，且上级菜单不允许为外链菜单");
        } else {
            throw new DataValidatedException("非法的权限类型：" + permission.getPermType());
        }
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

    private void fetchPermVoChildren(List<PermissionVo> permList, Map<Long, List<PermissionVo>> permPidGroupMap) {
        if (permList != null && !permList.isEmpty()) {
            for (PermissionVo perm : permList) {
                List<PermissionVo> childrenList = permPidGroupMap.get(perm.getId());
                fetchPermVoChildren(childrenList, permPidGroupMap);
                perm.setChildren(childrenList);
            }
        }
    }

    private void removeCache(Permission permission) {
        SecurityUserCache.clean();
        if (permission != null && StringUtils.isNotBlank(permission.getPermCode())) {
            List<InterfaceVo> interfaces = interfaceMapper.selectInterfacesByCode(permission.getPermCode());
            if (!interfaces.isEmpty()) {
                interfaces.forEach(interfaceVo -> {
                    String uriInfo = interfaceVo.getUri() + "_" + interfaceVo.getMethod();
                    String interfaceLevelCacheKey = String.format(Constants.SysCacheKey.interfaceLevel.cacheKey(), uriInfo, Locale.ROOT);
                    GlobalCache.remove(interfaceLevelCacheKey);
                });
            }
        }
    }

    private LambdaQueryWrapper<Permission> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
