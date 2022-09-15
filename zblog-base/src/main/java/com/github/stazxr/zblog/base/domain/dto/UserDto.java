package com.github.stazxr.zblog.base.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户更新字段信息
 *
 * @author SunTao
 * @since 2022-07-31
 */
@Data
public class UserDto {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像地址
     */
    private String headImg;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否临时用户
     */
    private Boolean temp;

    /**
     * 临时用户有效时间
     */
    private String expiredTime;

    /**
     * 角色序列列表
     */
    private List<Long> roleIds;
}
