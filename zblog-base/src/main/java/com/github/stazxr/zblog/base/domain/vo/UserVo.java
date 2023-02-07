package com.github.stazxr.zblog.base.domain.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

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
     * 性别
     */
    private Integer gender;

    /**
     * 头像地址
     */
    private String headImgUrl;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 签名
     */
    private String signature;

    /**
     * 个人网站
     */
    private String website;

    /**
     * 登录时间
     */
    private String loginTime;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改用户
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private String updateTime;

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
     * 授权时间
     */
    private String authTime;

    /**
     * 账号过期时间
     */
    private String expiredTime;

    /**
     * 用户对应的角色序号列表
     */
    private List<Long> roleIds;

    /**
     * 用户对应的角色列表
     */
    private List<String> roleNames;

    /**
     * 评论点赞列表
     */
    private Set<Long> commentLikeSet;

    /**
     * 说说点赞列表
     */
    private Set<Long> talkLikeSet;

    /**
     * 文章点赞列表
     */
    private Set<Long> articleLikeSet;
}
