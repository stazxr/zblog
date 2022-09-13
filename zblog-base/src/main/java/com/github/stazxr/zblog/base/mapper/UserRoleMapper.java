package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.base.domain.dto.UserRoleDto;
import com.github.stazxr.zblog.base.domain.entity.UserRoleRelation;
import org.apache.ibatis.annotations.Param;

/**
 * 用户角色数据持久层
 *
 * @author SunTao
 * @since 2022-01-14
 */
public interface UserRoleMapper extends BaseMapper<UserRoleRelation> {
    /**
     * 根据角色序列删除中间数据
     *
     * @param roleId 角色序列
     */
    void deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量删除用户角色信息
     *
     * @param userRoleDto 角色 - 用户对应信息
     */
    void batchDeleteUserRole(UserRoleDto userRoleDto);
}
