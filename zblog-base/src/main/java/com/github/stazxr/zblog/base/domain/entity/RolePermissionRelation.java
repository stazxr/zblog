package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色权限关系表
 *
 * @author SunTao
 * @since 2020-01-03
 */
@Getter
@Setter
@TableName("role_permission_relation")
public class RolePermissionRelation extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 角色序列
     */
    private Long roleId;

    /**
     * 权限序列
     */
    private Long permId;
}
