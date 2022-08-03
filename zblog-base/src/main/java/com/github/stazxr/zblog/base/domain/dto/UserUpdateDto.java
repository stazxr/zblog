package com.github.stazxr.zblog.base.domain.dto;

import lombok.Data;

/**
 * 用户更新字段信息
 *
 * @author SunTao
 * @since 2022-07-31
 */
@Data
public class UserUpdateDto {
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
}
