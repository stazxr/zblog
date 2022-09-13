package com.github.stazxr.zblog.base.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * UserVo
 *
 * @author SunTao
 * @since 2022-08-29
 */
@Data
public class UserVo {
    /**
     * ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 登录时间
     */
    private String loginTime;

    /**
     * 授权时间
     */
    private String authTime;

    /**
     * 用户是否启用
     */
    private Boolean enabled;

    /**
     * 账户是否登录锁定, 登录次数失败太多则锁定
     */
    private Boolean locked;

    /**
     * 是否是临时账户
     */
    private Boolean temp;

    /**
     * 用户对应的角色序号列表
     */
    private List<Long> roleIds;
}
