package com.github.stazxr.zblog.base.domain.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.Data;

/**
 * 用户综合信息
 *
 * @author SunTao
 * @since 2022-08-01
 */
@Data
public class UserDetailVo {
    /**
     * 用户主键
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户名（用于登录）
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 企鹅号
     */
    private String qq;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 签名
     */
    private String signature;

    /**
     * 头像地址
     */
    private String headImgUrl;

    /**
     * 登录时间
     */
    private String loginTime;

    /**
     * 修改密码时间
     */
    private String changePwdTime;

    /**
     * 是否需要登陆修改密码
     * 后台创建的用户第一次登录需要修改密码
     */
    private Boolean changePwd;

    /**
     * 账户是否登录锁定, 登录次数失败太多则锁定
     */
    private Boolean locked;

    /**
     * 是否是临时账户
     */
    private Boolean temp;

    /**
     * 如果是临时账户，需要注明过期时间
     * 格式：{@link DateUtils#defaultPattern}
     */
    private String expiredTime;

    /**
     * 是否是系统管理员
     */
    private Boolean admin;

    /**
     * 是否是内置用户, 默认否, 内置用户无法删除
     */
    private Boolean buildIn;

    /**
     * 用户是否启用
     */
    private Boolean enabled;

    /**
     * 是否有效
     */
    @TableLogic
    private Boolean deleted;
}
