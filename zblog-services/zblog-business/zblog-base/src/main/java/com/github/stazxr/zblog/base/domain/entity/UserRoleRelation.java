package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户角色关系表
 *
 * @author SunTao
 * @since 2021-01-03
 */
@Getter
@Setter
@TableName("user_role_relation")
public class UserRoleRelation extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 用户序列
     */
    private Long userId;

    /**
     * 角色序列
     */
    private Long roleId;
}
