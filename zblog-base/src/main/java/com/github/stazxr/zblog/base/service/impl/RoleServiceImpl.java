package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.dto.RoleQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.vo.RoleVo;
import com.github.stazxr.zblog.base.mapper.RoleMapper;
import com.github.stazxr.zblog.base.service.RoleService;
import com.github.stazxr.zblog.log.domain.vo.LogVo;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.collection.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 查询用户角色列表（包含被禁用的角色）
     *
     * @param userId 用户序列
     * @return Roles
     */
    @Override
    public List<Role> queryRolesByUserId(Long userId) {
        return roleMapper.queryRolesByUserId(userId);
    }

    /**
     * 查询权限对应的角色详情
     *
     * @param queryDto 查询参数
     * @return roleList
     */
    @Override
    public PageInfo<RoleVo> queryPermRole(RoleQueryDto queryDto) {
        Assert.notNull(queryDto.getPage(), "参数page不能为空");
        Assert.notNull(queryDto.getPageSize(), "参数pageSize不能为空");
        Assert.notNull(queryDto.getPermId(), "权限ID不能为空");

        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        List<RoleVo> dataList = roleMapper.selectRoleList(queryDto);
        return new PageInfo<>(dataList);
    }
}
