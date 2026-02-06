package com.github.stazxr.zblog.base.domain.vo;

import com.github.stazxr.zblog.bas.mask.MaskType;
import com.github.stazxr.zblog.bas.mask.core.FieldMask;
import com.github.stazxr.zblog.core.base.BaseVo;
import com.github.stazxr.zblog.util.time.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("用户VO")
public class UserVo extends BaseVo {
    private static final long serialVersionUID = -746423782527946064L;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型")
    private Integer userType;

    /**
     * 用户状态
     */
    @ApiModelProperty(value = "用户状态")
    private Integer userStatus;

    /**
     * 如果是临时账户，需要注明过期时间
     * 格式：{@link DateUtils#defaultPattern}
     */
    @ApiModelProperty(value = "账户过期时间")
    private String expireTime;

    /**
     * 用户修改密码时间
     */
    @ApiModelProperty(value = "用户修改密码时间")
    private String changePasswordTime;

    /**
     * 密码到期时间
     */
    @ApiModelProperty(value = "密码到期时间")
    private String passwordExpireTime;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;

    /**
     * 邮箱
     */
    @FieldMask(type = MaskType.EMAIL_WEAK)
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private Integer gender;

    /**
     * 签名
     */
    @ApiModelProperty(value = "签名")
    private String signature;

    /**
     * 个人网站
     */
    @ApiModelProperty(value = "个人网站")
    private String website;

    /**
     * 头像地址
     */
    @ApiModelProperty(value = "头像地址")
    private String headImgUrl;

    /**
     * 成功登录时间
     */
    @ApiModelProperty(value = "成功登录时间")
    private String successLoginTime;

    /**
     * 用户锁定到期时间
     */
    @ApiModelProperty(value = "用户锁定到期时间")
    private String lockedExpireTime;

    /**
     * 登录渠道
     */
    @ApiModelProperty(value = "登录渠道")
    private String loginChan;

    /**
     * 登录平台
     */
    @ApiModelProperty(value = "登录平台")
    private String loginPlatform;

    /**
     * 登录类型
     */
    @ApiModelProperty(value = "登录类型")
    private String loginType;

    /**
     * 登录地址
     */
    @ApiModelProperty(value = "登录地址")
    private String loginAddress;

    /**
     * 用户代理
     */
    @ApiModelProperty(value = "用户代理")
    private String userAgent;

    /**
     * 用户对应的角色序号列表
     */
    @ApiModelProperty(value = "用户对应的角色序号列表")
    private List<Long> roleIds;

    /**
     * 用户对应的角色列表
     */
    @ApiModelProperty(value = "用户对应的角色列表")
    private List<String> roleNames;
}
