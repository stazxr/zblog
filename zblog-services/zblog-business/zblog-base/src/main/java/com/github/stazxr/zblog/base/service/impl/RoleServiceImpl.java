package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.security.cache.SecurityUserCache;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.base.domain.dto.RoleAuthDto;
import com.github.stazxr.zblog.base.domain.dto.query.RoleQueryDto;
import com.github.stazxr.zblog.base.domain.dto.query.UserQueryDto;
import com.github.stazxr.zblog.base.domain.dto.UserRoleDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.entity.RolePermissionRelation;
import com.github.stazxr.zblog.base.domain.entity.UserRoleRelation;
import com.github.stazxr.zblog.base.domain.vo.RoleVo;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
import com.github.stazxr.zblog.base.mapper.RoleMapper;
import com.github.stazxr.zblog.base.mapper.RolePermMapper;
import com.github.stazxr.zblog.base.mapper.UserRoleMapper;
import com.github.stazxr.zblog.base.service.RoleService;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.util.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.stazxr.zblog.base.util.Constants.SecurityRole.*;

/**
 * 角色业务实现层
 *
 * @author SunTao
 * @since 2020-11-16
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    /**
     * 内置角色列表
     */
    private static final String[] INNER_ROLES = { OPEN, PUBLIC, FORBIDDEN, NONE, NULL, NO_TEST };

    private final RoleMapper roleMapper;

    private final UserRoleMapper userRoleMapper;

    private final RolePermMapper rolePermMapper;

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
     * 查询角色列表
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    @Override
    public PageInfo<RoleVo> queryRoleListByPage(RoleQueryDto queryDto) {
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(roleMapper.selectRoleList(queryDto));
    }

    /**
     * 查询角色详情
     *
     * @param roleId 角色ID
     * @return RoleVo
     */
    @Override
    public RoleVo queryRoleDetail(Long roleId) {
        Assert.notNull(roleId, "参数【roleId】不能为空");
        RoleVo roleVo = roleMapper.selectRoleDetail(roleId);
        Assert.notNull(roleVo, "查询角色信息失败，角色【" + roleId + "】不存在");
        return roleVo;
    }

    /**
     * 新增角色
     *
     * @param role 角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(Role role) {
        role.setId(null);
        checkRole(role);
        Assert.isTrue(roleMapper.insert(role) != 1, "新增失败");
    }

    /**
     * 编辑角色
     *
     * @param role 角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editRole(Role role) {
        Assert.notNull(role.getId(), "参数【id】不能为空");
        checkRole(role);
        Assert.isTrue(roleMapper.updateById(role) != 1, "修改失败");
        SecurityUserCache.clean();
    }

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId) {
        Assert.notNull(roleId, "参数【roleId】不能为空");
        UserQueryDto queryDto = new UserQueryDto();
        queryDto.setBusinessId(roleId);
        List<UserVo> userList = roleMapper.selectUsersByRoleId(queryDto);
        DataValidated.isTrue(!userList.isEmpty(), "所选角色存在用户关联，请解除关联再试！");
        Assert.isTrue(roleMapper.deleteById(roleId) != 1, "删除失败");
        userRoleMapper.deleteByRoleId(roleId);
        rolePermMapper.deleteByRoleId(roleId);
        SecurityUserCache.clean();
    }

    /**
     * 角色授权
     *
     * @param authDto 授权信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authRole(RoleAuthDto authDto) {
        Assert.notNull(authDto.getRoleId(), "参数【roleId】不能为空");
        rolePermMapper.deleteByRoleId(authDto.getRoleId());
        for (Long permId : authDto.getPermIds()) {
            RolePermissionRelation rolePerm = new RolePermissionRelation();
            rolePerm.setId(SequenceUtils.getId());
            rolePerm.setRoleId(authDto.getRoleId());
            rolePerm.setPermId(permId);
            rolePermMapper.insert(rolePerm);
        }

        SecurityUserCache.clean();
    }

    /**
     * 查询角色对应的权限列表
     *
     * @param roleId 角色序列
     * @return permIdList
     */
    @Override
    public Set<Long> queryPermIdsByRoleId(Long roleId) {
        Assert.notNull(roleId, "参数【roleId】不能为空");
        return rolePermMapper.selectPermIdsByRoleId(roleId);
    }

    /**
     * 查询角色对应的用户列表
     *
     * @param queryDto 查询参数
     * @return userList
     */
    @Override
    public PageInfo<UserVo> pageUsersByRoleId(UserQueryDto queryDto) {
        Assert.notNull(queryDto.getBusinessId(), "参数【businessId】不能为空");
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(roleMapper.selectUsersByRoleId(queryDto));
    }

    /**
     * 查询用户角色列表（包含被禁用的角色）
     *
     * @param userId 用户序列
     * @return Roles
     */
    @Override
    public List<Role> queryRolesByUserId(Long userId) {
        Assert.notNull(userId, "参数【userId】不能为空");
        return roleMapper.selectRolesByUserId(userId);
    }

    /**
     * 批量新增用户角色
     *
     * @param userRoleDto 角色 - 用户对应信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAddUserRole(UserRoleDto userRoleDto) {
        Assert.notNull(userRoleDto.getRoleId(), "参数【roleId】不能为空");
        Set<Long> userIds = userRoleDto.getUserIds();
        if (userIds == null || userIds.size() == 0) {
            return;
        }

        for (Long userId : userIds) {
            UserRoleRelation userRole = new UserRoleRelation();
            userRole.setId(SequenceUtils.getId());
            userRole.setUserId(userId);
            userRole.setRoleId(userRoleDto.getRoleId());
            userRoleMapper.insert(userRole);
            SecurityUserCache.clean();
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
        Assert.notNull(userRoleDto.getRoleId(), "参数【roleId】不能为空");
        Set<Long> userIds = userRoleDto.getUserIds();
        if (userIds == null || userIds.size() == 0) {
            return;
        }

        userRoleMapper.batchDeleteUserRole(userRoleDto);
        userIds.forEach(userId -> SecurityUserCache.remove(String.valueOf(userId)));
    }

    private void checkRole(Role role) {
        Assert.notNull(role.getRoleName(), "角色名称不能为空");
        Assert.notNull(role.getRoleCode(), "角色编码不能为空");
        Assert.notNull(role.isEnabled(), "角色状态不能为空");

        // 检查角色编码是否允许使用
        role.setRoleCode(role.getRoleCode().trim());
        DataValidated.isTrue(Arrays.asList(INNER_ROLES).contains(role.getRoleCode()), "该角色编码被禁止使用，请换一个");

        // 检查角色名称是否存在
        Role dbRole = roleMapper.selectByRoleName(role.getRoleName());
        DataValidated.isTrue(dbRole != null && !dbRole.getId().equals(role.getId()), "角色名称已存在");

        // 检查角色编码是否存在
        dbRole = roleMapper.selectByRoleCode(role.getRoleCode());
        DataValidated.isTrue(dbRole != null && !dbRole.getId().equals(role.getId()), "角色编码已存在");
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
        return new HashSet<>();
    }
}
