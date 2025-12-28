package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.router.Resource;
import com.github.stazxr.zblog.bas.security.authz.metadata.ResourceCacheService;
import com.github.stazxr.zblog.bas.security.cache.SecurityUserCache;
import com.github.stazxr.zblog.bas.validation.AssertException;
import com.github.stazxr.zblog.base.converter.PermissionConverter;
import com.github.stazxr.zblog.base.domain.dto.PermissionDto;
import com.github.stazxr.zblog.base.domain.dto.query.PermissionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.enums.PermissionType;
import com.github.stazxr.zblog.base.domain.vo.*;
import com.github.stazxr.zblog.base.mapper.*;
import com.github.stazxr.zblog.base.service.PermissionService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.bas.validation.Assert;
import com.github.stazxr.zblog.util.RegexUtils;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.math.MathUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限管理业务实现层
 *
 * @author SunTao
 * @since 2020-11-16
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    private final PermissionMapper permissionMapper;

    private final PermissionConverter permissionConverter;

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
        // 参数检查
        if (StringUtils.isNotBlank(queryDto.getBlurry())) {
            queryDto.setBlurry(queryDto.getBlurry().trim());
        }

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
        // 查询权限信息
        Assert.notNull(permId, ExpMessageCode.of("valid.perm.id.NotNull"));
        PermissionVo permissionVo = permissionMapper.selectPermDetail(permId);
        Assert.notNull(permissionVo, ExpMessageCode.of("valid.perm.not.exist"));
        // 查询角色编码列表
        permissionVo.setRoleCodeList(permissionMapper.selectRoleCodesByPermId(permId));
        return permissionVo;
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
    public void addPermission(PermissionDto permissionDto) {
        // 获取新增权限信息
        Permission permission = permissionConverter.dtoToEntity(permissionDto);
        // 新增时，不允许传入 PermissionId
        Assert.isNull(permission.getId(), ExpMessageCode.of("valid.perm.add.idIsNull"));
        // 权限信息检查
        checkPermission(permission);
        // 新增权限
        Assert.isTrue(permissionMapper.insert(permission) != 1, ExpMessageCode.of("result.perm.add.failed"));
        // 删除相关缓存
        if (StringUtils.isNotBlank(permission.getPermCode())) {
            removeCache(permission.getId(), null, permission.getPermCode());
        }
    }

    /**
     * 编辑权限
     *
     * @param permissionDto 权限信息
     */
    @Override
    public void editPermission(PermissionDto permissionDto) {
        // 获取编辑权限信息
        Permission permission = permissionConverter.dtoToEntity(permissionDto);
        // 判断权限是否存在
        Permission dbPermission = permissionMapper.selectById(permission.getId());
        Assert.notNull(dbPermission, ExpMessageCode.of("valid.perm.not.exist"));
        // 权限类型不允许编辑
        boolean permTypeIsSame = dbPermission.getPermType().equals(permission.getPermType());
        Assert.isTrue(!permTypeIsSame, ExpMessageCode.of("valid.perm.permType.notAllowedEdit"));
        // 权限信息检查
        checkPermission(permission);
        // 编辑权限
        Assert.isTrue(permissionMapper.updateById(permission) != 1, ExpMessageCode.of("result.perm.edit.failed"));
        // 删除相关缓存
        if (!StringUtils.hasBlank(dbPermission.getPermCode(), permission.getPermCode())) {
            removeCache(permission.getId(), dbPermission.getPermCode(), permission.getPermCode());
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
        // 判断权限是否存在
        Permission dbPermission = permissionMapper.selectById(permId);
        Assert.notNull(dbPermission, ExpMessageCode.of("valid.perm.not.exist"));
        // 判断是否存在子节点
        Integer permType = dbPermission.getPermType();
        boolean dirOrMenu = PermissionType.DIR.getType().equals(permType) ||
                PermissionType.MENU.getType().equals(permType);
        if (dirOrMenu) {
            Long childCount = permissionMapper.selectCount(queryBuild().eq(Permission::getPid, permId));
            Assert.isTrue(childCount > 0, ExpMessageCode.of("valid.perm.delete.hasChild"));
        }
        // 删除权限
        Assert.isTrue(permissionMapper.deleteById(permId) != 1, ExpMessageCode.of("result.perm.delete.failed"));
        rolePermMapper.deleteByPermId(permId);
        // 删除相关缓存
        if (StringUtils.isNotBlank(dbPermission.getPermCode())) {
            removeCache(dbPermission.getId(), dbPermission.getPermCode(), null);
        }
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
        int parentType = PermissionType.DIR.getType();
        if (permission.getPid() == null || Constants.TOP_PERM_ID.equals(permission.getPid())) {
            permission.setPid(null);
        } else {
            Permission parentPermission = permissionMapper.selectById(permission.getPid());
            Assert.notNull(parentPermission, ExpMessageCode.of("valid.perm.parent.notExist"));
            parentType = parentPermission.getPermType();
        }

        // 赋默认值
        if (PermissionType.DIR.getType().equals(permission.getPermType())) {
            // 目录
            permission.setPermCode(null);
            permission.setCacheable(null);
            permission.setComponentName(null);
            permission.setComponentPath(null);
            // 参数校验
            String routerPath = permission.getRouterPath();
            Assert.notBlank(routerPath, ExpMessageCode.of("valid.perm.routerPath.NotBlank"));
            Assert.isTrue(!routerPath.matches(RegexUtils.Regex.LETTER_REGEX), ExpMessageCode.of("valid.perm.routerPath.patternError"));
            Assert.isTrue(checkRouterPathExist(permission), ExpMessageCode.of("valid.perm.routerPath.exist"));
            Assert.notNull(permission.getHidden(), ExpMessageCode.of("valid.perm.hidden.NotNull"));
            // 上级信息校验，要求上级只能是目录
            Assert.isTrue(!PermissionType.DIR.getType().equals(parentType), ExpMessageCode.of("valid.perm.dir.parentError"));
        } else if (PermissionType.MENU.getType().equals(permission.getPermType())) {
            // 菜单
            permission.setPermCode(StringUtils.isBlank(permission.getPermCode()) ? null : permission.getPermCode());
            // 参数校验
            Assert.notNull(permission.getHidden(), ExpMessageCode.of("valid.perm.hidden.NotNull"));
            Assert.notNull(permission.getCacheable(), ExpMessageCode.of("valid.perm.cacheable.NotNull"));
            String routerPath = permission.getRouterPath();
            Assert.notBlank(routerPath, ExpMessageCode.of("valid.perm.routerPath.NotBlank"));
            Assert.isTrue(!routerPath.matches(RegexUtils.Regex.LETTER_REGEX), ExpMessageCode.of("valid.perm.routerPath.patternError"));
            Assert.isTrue(checkRouterPathExist(permission), ExpMessageCode.of("valid.perm.routerPath.exist"));
            if (permission.getPermCode() != null) {
                Assert.isTrue(checkPermCodeExist(permission), ExpMessageCode.of("valid.perm.permCode.exist"));
            }
            Assert.notBlank(permission.getComponentName(), ExpMessageCode.of("valid.perm.componentName.NotBlank"));
            Assert.notBlank(permission.getComponentPath(), ExpMessageCode.of("valid.perm.componentPath.NotBlank"));

            // 上级信息校验，要求上级只能是目录或空
            Assert.isTrue(!PermissionType.DIR.getType().equals(parentType), ExpMessageCode.of("valid.perm.menu.parentError"));
        } else if (PermissionType.LINK.getType().equals(permission.getPermType())) {
            // 外链
            permission.setPermCode(null);
            permission.setCacheable(null);
            permission.setComponentName(null);
            permission.setComponentPath(null);
            // 参数校验
            String routerPath = permission.getRouterPath();
            Assert.notBlank(routerPath, ExpMessageCode.of("valid.perm.linkPath.NotBlank"));
            Assert.isTrue(!routerPath.matches(RegexUtils.Regex.LINK_REGEX), ExpMessageCode.of("valid.perm.linkPath.patternError"));
            Assert.notNull(permission.getHidden(), ExpMessageCode.of("valid.perm.hidden.NotNull"));
            // 上级信息校验，要求上级只能是目录
            Assert.isTrue(!PermissionType.DIR.getType().equals(parentType), ExpMessageCode.of("valid.perm.link.parentError"));
        } else if (PermissionType.BTN.getType().equals(permission.getPermType())) {
            // 按钮
            permission.setIcon(null);
            permission.setHidden(null);
            permission.setCacheable(null);
            permission.setRouterPath(null);
            permission.setComponentName(null);
            permission.setComponentPath(null);
            // 参数校验，权限编码校验，权限编码不能重复
            Assert.notBlank(permission.getPermCode(), ExpMessageCode.of("valid.perm.permCode.NotBlank"));
            Assert.isTrue(checkPermCodeExist(permission), ExpMessageCode.of("valid.perm.permCode.exist"));
            // 上级信息校验，要求上级只能是菜单或目录或空
            boolean dirOrMenu = PermissionType.DIR.getType().equals(parentType) ||
                    PermissionType.MENU.getType().equals(parentType);
            Assert.isTrue(!dirOrMenu, ExpMessageCode.of("valid.perm.btn.parentError"));
        } else {
            throw new AssertException(ExpMessageCode.of("valid.perm.permType.invalid", permission.getPermType()));
        }
    }

    private boolean checkPermCodeExist(Permission permission) {
        if (permission.getPermCode() != null) {
            LambdaQueryWrapper<Permission> queryWrapper = queryBuild().eq(Permission::getPermCode, permission.getPermCode());
            if (permission.getId() != null) {
                queryWrapper.ne(Permission::getId, permission.getId());
            }
            return permissionMapper.exists(queryWrapper);
        }
        return false;
    }

    private boolean checkRouterPathExist(Permission permission) {
        if (permission.getRouterPath() != null) {
            LambdaQueryWrapper<Permission> queryWrapper = queryBuild().eq(Permission::getRouterPath, permission.getRouterPath());
            if (permission.getId() != null) {
                queryWrapper.ne(Permission::getId, permission.getId());
            }
            return permissionMapper.exists(queryWrapper);
        }
        return false;
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

    private void removeCache(Long permId, String oldPermCode, String newPermCode) {
        try {
            // 清除用户缓存的权限信息
            List<Long> userIds = permissionMapper.selectUserIdsByPermId(permId);
            userIds.forEach(userId -> SecurityUserCache.remove(String.valueOf(userId)));
            // 清除资源缓存信息
            if (StringUtils.isNotBlank(oldPermCode)) {
                Resource resource = permissionMapper.selectResourceByPermCode(oldPermCode);
                if (resource != null) {
                    String cacheKey = resource.getResourceUri() + ":" + resource.getResourceMethod();
                    resourceCacheService.clearCache(cacheKey);
                }
            }
            if (StringUtils.isNotBlank(newPermCode)) {
                Resource resource = permissionMapper.selectResourceByPermCode(newPermCode);
                if (resource != null) {
                    String cacheKey = resource.getResourceUri() + ":" + resource.getResourceMethod();
                    resourceCacheService.clearCache(cacheKey);
                }
            }
        } catch (Exception e) {
            log.error("remove cache failed", e);
        }
    }

    private LambdaQueryWrapper<Permission> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
