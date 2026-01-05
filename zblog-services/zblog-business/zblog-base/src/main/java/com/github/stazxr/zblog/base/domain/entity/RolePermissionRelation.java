package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 角色权限
 *
 * @author SunTao
 * @since 2020-01-03
 */
@Getter
@Setter
@TableName("role_permission_relation")
public class RolePermissionRelation implements Serializable {
    private static final long serialVersionUID = 6823950832644631052L;

    /**
     * 角色序列
     */
    private Long roleId;

    /**
     * 权限序列
     */
    private Long permId;
}
