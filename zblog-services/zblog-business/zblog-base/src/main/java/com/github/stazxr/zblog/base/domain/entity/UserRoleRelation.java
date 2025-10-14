package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户角色关系表
 *
 * @author SunTao
 * @since 2021-01-03
 */
@Getter
@Setter
@TableName("user_role_relation")
public class UserRoleRelation implements Serializable {
    private static final long serialVersionUID = 2721485954618944620L;

    /**
     * 用户序列
     */
    private Long userId;

    /**
     * 角色序列
     */
    private Long roleId;
}
