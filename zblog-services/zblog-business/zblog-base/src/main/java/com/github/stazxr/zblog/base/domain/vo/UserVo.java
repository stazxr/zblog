package com.github.stazxr.zblog.base.domain.vo;

import com.github.stazxr.zblog.bas.mask.MaskType;
import com.github.stazxr.zblog.bas.mask.core.FieldMask;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * UserVo
 *
 * @author SunTao
 * @since 2022-08-29
 */
@Getter
@Setter
public class UserVo {
    /**
     * 用户唯一标识
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @FieldMask(type = MaskType.PASSWORD)
    private String password;

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
     * 登录时间
     */
    private String loginTime;

    /**
     * 是否有效
     */
    private Boolean deleted;

    /**
     * 创建用户，存储该实体的创建用户标识。
     */
    private Long createUser;

    /**
     * 创建时间，存储该实体的创建时间。
     */
    private String createTime;

    /**
     * 修改用户，存储最后修改该实体的用户标识。
     */
    private Long updateUser;

    /**
     * 修改时间，存储该实体的最后修改时间。
     */
    private String updateTime;
}
