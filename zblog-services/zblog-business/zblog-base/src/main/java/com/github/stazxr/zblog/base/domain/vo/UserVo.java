package com.github.stazxr.zblog.base.domain.vo;

import com.github.stazxr.zblog.bas.mask.MaskType;
import com.github.stazxr.zblog.bas.mask.core.FieldMask;
import com.github.stazxr.zblog.core.base.BaseVo;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * UserVo
 *
 * @author SunTao
 * @since 2022-08-29
 */
@Getter
@Setter
public class UserVo extends BaseVo {
    private static final long serialVersionUID = -746423782527946064L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 用户状态
     */
    private Integer userStatus;

    /**
     * 如果是临时账户，需要注明过期时间
     * 格式：{@link DateUtils#defaultPattern}
     */
    private String expiredTime;

    /**
     * 用户修改密码时间
     */
    private String changePwdTime;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    @FieldMask(type = MaskType.EMAIL_WEAK)
    private String email;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 签名
     */
    private String signature;

    /**
     * 个人网站
     */
    private String website;

    /**
     * 头像地址
     */
    private String headImgUrl;

    /**
     * 登录时间（成功）
     */
    private String lastLoginTime;

    /**
     * 登录渠道, 01:移动端; 02:PC端
     */
    private String loginChan;

    /**
     * 登录类型, 00:访客登录; 01:密码登录; 02:QQ登录; 99:未知
     */
    private String loginType;

    /**
     * 登录地址
     */
    private String loginAddress;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 用户对应的角色序号列表
     */
    private List<Long> roleIds;

    /**
     * 用户对应的角色列表
     */
    private List<String> roleNames;
}
