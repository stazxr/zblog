package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.bas.security.cache.SecurityUserCache;
import com.github.stazxr.zblog.base.converter.RoleConverter;
import com.github.stazxr.zblog.base.domain.dto.RoleAuthDto;
import com.github.stazxr.zblog.base.domain.dto.RoleDto;
import com.github.stazxr.zblog.base.domain.dto.query.RoleQueryDto;
import com.github.stazxr.zblog.base.domain.dto.UserRoleDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.entity.RolePermissionRelation;
import com.github.stazxr.zblog.base.domain.entity.UserRoleRelation;
import com.github.stazxr.zblog.base.domain.error.RoleErrorCode;
import com.github.stazxr.zblog.base.domain.vo.RoleVo;
import com.github.stazxr.zblog.base.mapper.RoleMapper;
import com.github.stazxr.zblog.base.mapper.RolePermMapper;
import com.github.stazxr.zblog.base.mapper.UserRoleMapper;
import com.github.stazxr.zblog.base.service.RoleService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
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
     * @return IPage<RoleVo>
     */
    @Override
    public IPage<RoleVo> queryRoleListByPage(RoleQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getRoleName())) {
            queryDto.setRoleName(queryDto.getRoleName().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getRoleCode())) {
            queryDto.setRoleCode(queryDto.getRoleCode().trim());
        }
        // 分页查询
        Page<RoleVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return roleMapper.selectRoleList(page, queryDto);
    }

    /**
     * 查询角色列表
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    @Override
    public List<RoleVo> queryRoleList(RoleQueryDto queryDto) {
        Page<RoleVo> page = new Page<>(1, Integer.MAX_VALUE);
        page.setSearchCount(false);
        return roleMapper.selectRoleList(page, queryDto).getRecords();
    }

    /**
     * 查询角色详情
     *
     * @param roleId 角色ID
     * @return RoleVo
     */
    @Override
    public RoleVo queryRoleDetail(Long roleId) {
        RoleVo roleVo = roleMapper.selectRoleDetail(roleId);
        return ThrowUtils.requireNonNull(roleVo, BaseErrorCode.ECOREA001);
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
        ThrowUtils.when(role.getId() != null).system(BaseErrorCode.SCOREB001);
        // 角色信息检查
        checkRole(role);
        // 新增角色
        ThrowUtils.when(!save(role)).system(BaseErrorCode.SCOREA001);
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
        ThrowUtils.throwIfNull(dbRole, BaseErrorCode.ECOREA001);
        // 角色信息检查
        checkRole(role);
        // 编辑角色
        ThrowUtils.when(!updateById(role)).system(BaseErrorCode.SCOREA002);
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
        rolePermMapper.deleteByRoleIdHard(roleId);
        if (authDto.getPermIds() != null && !authDto.getPermIds().isEmpty()) {
            List<RolePermissionRelation> rolePerms = new ArrayList<>();
            for (Long permId : authDto.getPermIds()) {
                RolePermissionRelation rolePerm = new RolePermissionRelation();
                rolePerm.setRoleId(roleId);
                rolePerm.setPermId(permId);
                rolePerm.setDeleted(Boolean.FALSE);
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
        ThrowUtils.throwIfNull(dbRole, BaseErrorCode.ECOREA001);
        // 判断角色是否关联用户
        List<Long> userIds = userRoleMapper.selectUserIdsByRoleId(roleId);
        ThrowUtils.when(!userIds.isEmpty()).service(RoleErrorCode.EROLEA004);
        // 删除角色
        ThrowUtils.when(!removeById(roleId)).system(BaseErrorCode.SCOREA003);
        // 删除角色关联信息
        rolePermMapper.deleteByRoleIdSoft(roleId);
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
        Set<Long> userIds = userRoleDto.getUserIds();
        if (userIds == null || userIds.size() == 0) {
            return;
        }

        for (Long userId : userIds) {
            UserRoleRelation userRole = new UserRoleRelation();
            userRole.setUserId(userId);
            userRole.setRoleId(userRoleDto.getRoleId());
            userRole.setDeleted(Boolean.FALSE);
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
        ThrowUtils.when(checkRoleNameExist(role)).service(RoleErrorCode.EROLEA000);

        // 检查角色编码
        String roleCode = role.getRoleCode().trim();
        role.setRoleCode(roleCode);
        ThrowUtils.throwIf(!roleCode.matches(RegexUtils.Regex.ROLE_CODE_REGEX), RoleErrorCode.EROLEA002);
        ThrowUtils.throwIf(Arrays.asList(INNER_ROLES).contains(roleCode), RoleErrorCode.EROLEA003);
        ThrowUtils.throwIf(checkRoleCodeExist(role), RoleErrorCode.EROLEA001);
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
            return roleMapper.existsIgnoreDeleted(queryWrapper);
        }
        return false;
    }

    private LambdaQueryWrapper<Role> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
