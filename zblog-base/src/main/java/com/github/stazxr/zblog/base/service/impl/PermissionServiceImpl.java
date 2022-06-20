package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.vo.MenuVo;
import com.github.stazxr.zblog.base.mapper.PermissionMapper;
import com.github.stazxr.zblog.base.mapper.RoleMapper;
import com.github.stazxr.zblog.base.service.PermissionService;
import com.github.stazxr.zblog.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限业务实现层
 *
 * @author SunTao
 * @since 2020-11-16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RoleMapper roleMapper;

    /**
     * 根据权限路径查找权限
     *
     * @param path 权限路径
     * @return Permission
     */
    @Override
    public Permission selectPermByPath(String path) {
        Assert.notBlank(path, "查询权限路径不能为空");
        return permissionMapper.selectOne(queryBuild().eq(Permission::getPermPath, path));
    }

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
        // 获取用户角色列表
        Assert.notNull(userId, "用户ID不能为空");
        List<Role> roles = roleMapper.queryRolesByUserId(userId);
        return new ArrayList<>();
    }

    private LambdaQueryWrapper<Permission> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
