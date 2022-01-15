package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.mapper.RoleMapper;
import com.github.stazxr.zblog.base.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色业务实现层
 *
 * @author SunTao
 * @since 2020-11-16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    /**
     * 查询用户角色列表
     *
     * @param userId 用户序列
     * @return Roles
     */
    @Override
    public List<Role> queryRolesByUserId(Long userId) {
        return roleMapper.queryRolesByUserId(userId);
    }
}
