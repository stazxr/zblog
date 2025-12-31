package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.security.cache.SecurityUserCache;
import com.github.stazxr.zblog.bas.validation.Assert;
import com.github.stazxr.zblog.base.converter.RoleConverter;
import com.github.stazxr.zblog.base.domain.dto.RoleAuthDto;
import com.github.stazxr.zblog.base.domain.dto.RoleDto;
import com.github.stazxr.zblog.base.domain.dto.query.RoleQueryDto;
import com.github.stazxr.zblog.base.domain.dto.UserRoleDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.entity.RolePermissionRelation;
import com.github.stazxr.zblog.base.domain.entity.UserRoleRelation;
import com.github.stazxr.zblog.base.domain.vo.RoleVo;
import com.github.stazxr.zblog.base.mapper.RoleMapper;
import com.github.stazxr.zblog.base.mapper.RolePermMapper;
import com.github.stazxr.zblog.base.mapper.UserRoleMapper;
import com.github.stazxr.zblog.base.service.RoleService;
import com.github.stazxr.zblog.util.RegexUtils;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.github.stazxr.zblog.base.util.Constants.SecurityRole.*;

/**
 * 角色管理业务实现层
 *
 * @author SunTao
 * @since 2020-11-16
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    /**
     * 内置角色列表
     */
    private static final String[] INNER_ROLES = { OPEN, PUBLIC, FORBIDDEN, NONE, NULL, NO_TEST };

    private final RoleMapper roleMapper;

    private final RoleConverter roleConverter;

    private final UserRoleMapper userRoleMapper;

    private final RolePermMapper rolePermMapper;

    /**
     * 分页查询角色列表
     *
     * @param queryDto 查询参数
     * @return PageInfo<RoleVo>
     */
    @Override
    public PageInfo<RoleVo> queryRoleListByPage(RoleQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getRoleName())) {
            queryDto.setRoleName(queryDto.getRoleName().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getRoleCode())) {
            queryDto.setRoleCode(queryDto.getRoleCode().trim());
        }
        // 分页查询
        try (Page<RoleVo> page = PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize())) {
            List<RoleVo> dataList = roleMapper.selectRoleList(queryDto);
            return page.doSelectPageInfo(() -> new PageInfo<>(dataList));
        }
    }

    /**
     * 查询角色列表
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    @Override
    public List<RoleVo> queryRoleList(RoleQueryDto queryDto) {
        return roleMapper.selectRoleList(queryDto);
    }

    /**
     * 查询角色详情
     *
     * @param roleId 角色ID
     * @return RoleVo
     */
    @Override
    public RoleVo queryRoleDetail(Long roleId) {
        Assert.notNull(roleId, ExpMessageCode.of("valid.common.id.NotNull"));
        RoleVo roleVo = roleMapper.selectRoleDetail(roleId);
        Assert.notNull(roleVo, ExpMessageCode.of("valid.common.data.notFound"));
        return roleVo;
    }

    /**
     * 新增角色
     *
     * @param roleDto 角色
     */
    @Override
    public void addRole(RoleDto roleDto) {
        // 获取角色信息
        Role role = roleConverter.dtoToEntity(roleDto);
        // 新增时，不允许传入 RoleId
        Assert.isNull(role.getId(), ExpMessageCode.of("valid.common.addWithIdError"));
        // 角色信息检查
        checkRole(role);
        // 新增角色
        Assert.isTrue(roleMapper.insert(role) != 1, ExpMessageCode.of("result.common.add.failed"));
    }

    /**
     * 编辑角色
     *
     * @param roleDto 角色
     */
    @Override
    public void editRole(RoleDto roleDto) {
        // 获取角色信息
        Role role = roleConverter.dtoToEntity(roleDto);
        // 判断角色是否存在
        Role dbRole = roleMapper.selectById(role.getId());
        Assert.notNull(dbRole, ExpMessageCode.of("valid.common.data.notFound"));
        // 角色信息检查
        checkRole(role);
        // 编辑角色
        Assert.isTrue(roleMapper.updateById(role) != 1, ExpMessageCode.of("result.common.edit.failed"));
    }

    /**
     * 角色授权
     *
     * @param authDto 授权信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authRole(RoleAuthDto authDto) {
        Long roleId = authDto.getRoleId();
        rolePermMapper.deleteByRoleId(roleId);
        if (authDto.getPermIds() != null && !authDto.getPermIds().isEmpty()) {
            List<RolePermissionRelation> rolePerms = new ArrayList<>();
            for (Long permId : authDto.getPermIds()) {
                RolePermissionRelation rolePerm = new RolePermissionRelation();
                rolePerm.setRoleId(roleId);
                rolePerm.setPermId(permId);
                rolePerms.add(rolePerm);
            }
            rolePermMapper.insertBatch(rolePerms);
        }

        // 获取当前角色涉及的用户ID列表并清空缓存
        for (Long userId : userRoleMapper.selectUserIdsByRoleId(roleId)) {
            SecurityUserCache.remove(String.valueOf(userId));
        }
    }

    /**
     * 查询角色对应的权限id列表
     *
     * @param roleId 角色id
     * @return permIds
     */
    @Override
    public Set<Long> queryPermIdsByRoleId(Long roleId) {
        return rolePermMapper.selectPermIdsByRoleId(roleId);
    }

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId) {
        // 判断角色是否存在
        Role dbRole = roleMapper.selectById(roleId);
        Assert.notNull(dbRole, ExpMessageCode.of("valid.common.data.notFound"));
        // 判断角色是否关联用户
        List<Long> userIds = userRoleMapper.selectUserIdsByRoleId(roleId);
        Assert.isTrue(!userIds.isEmpty(), "所选角色存在用户关联，请解除关联再试！");
        // 删除角色
        Assert.isTrue(roleMapper.deleteById(roleId) != 1, ExpMessageCode.of("result.common.delete.failed"));
        // 删除角色关联信息
        userRoleMapper.deleteByRoleId(roleId);
        rolePermMapper.deleteByRoleId(roleId);
        // 清除缓存
        userIds.forEach(userId -> SecurityUserCache.remove(String.valueOf(userId)));
    }

    /**
     * 批量新增用户角色
     *
     * @param userRoleDto 角色 - 用户对应信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAddUserRole(UserRoleDto userRoleDto) {
        Assert.notNull(userRoleDto.getRoleId(), ExpMessageCode.of("valid.common.id.NotNull"));
        Set<Long> userIds = userRoleDto.getUserIds();
        if (userIds == null || userIds.size() == 0) {
            return;
        }

        for (Long userId : userIds) {
            UserRoleRelation userRole = new UserRoleRelation();
            userRole.setUserId(userId);
            userRole.setRoleId(userRoleDto.getRoleId());
            userRoleMapper.insert(userRole);
            SecurityUserCache.remove(String.valueOf(userId));
        }
    }

    /**
     * 批量删除用户角色
     *
     * @param userRoleDto 角色 - 用户对应信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteUserRole(UserRoleDto userRoleDto) {
        Assert.notNull(userRoleDto.getRoleId(), ExpMessageCode.of("valid.common.id.NotNull"));
        Set<Long> userIds = userRoleDto.getUserIds();
        if (userIds == null || userIds.size() == 0) {
            return;
        }

        userRoleMapper.batchDeleteUserRole(userRoleDto);
        userIds.forEach(userId -> SecurityUserCache.remove(String.valueOf(userId)));
    }

    /**
     * 获取指定资源的允许访问角色集合。
     *
     * @param requestUri    请求的 URL 路径，表示要访问的资源地址。
     * @param requestMethod 请求的 HTTP 方法类型（例如 GET、POST 等），
     *                      用于标识请求的操作类型。
     * @return 角色编码集合。
     */
    @Override
    public Set<String> selectResourceRoles(String requestUri, String requestMethod) {
        return roleMapper.selectResourceRoles(requestUri, requestMethod);
    }

    private void checkRole(Role role) {
        // 检查角色名称
        role.setRoleName(role.getRoleName().trim());
        Assert.isTrue(checkRoleNameExist(role), ExpMessageCode.of("valid.role.roleName.exist"));

        // 检查角色编码
        role.setRoleCode(role.getRoleCode().trim());
        Assert.isTrue(!role.getRoleCode().matches(RegexUtils.Regex.ROLE_CODE_REGEX), ExpMessageCode.of("valid.role.roleCode.patternError"));
        Assert.isTrue(Arrays.asList(INNER_ROLES).contains(role.getRoleCode()), ExpMessageCode.of("valid.role.roleCode.forbid"));
        Assert.isTrue(checkRoleCodeExist(role), ExpMessageCode.of("valid.role.roleCode.exist"));
    }

    private boolean checkRoleNameExist(Role role) {
        if (role.getRoleName() != null) {
            LambdaQueryWrapper<Role> queryWrapper = queryBuild().eq(Role::getRoleName, role.getRoleName());
            if (role.getId() != null) {
                queryWrapper.ne(Role::getId, role.getId());
            }
            return roleMapper.exists(queryWrapper);
        }
        return false;
    }

    private boolean checkRoleCodeExist(Role role) {
        if (role.getRoleCode() != null) {
            LambdaQueryWrapper<Role> queryWrapper = queryBuild().eq(Role::getRoleCode, role.getRoleCode());
            if (role.getId() != null) {
                queryWrapper.ne(Role::getId, role.getId());
            }
            return roleMapper.exists(queryWrapper);
        }
        return false;
    }

    private LambdaQueryWrapper<Role> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
