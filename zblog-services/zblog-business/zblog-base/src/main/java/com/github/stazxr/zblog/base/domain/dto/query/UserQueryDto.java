package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户查询参数
 *
 * @author SunTao
 * @since 2022-09-13
 */
@Getter
@Setter
@ApiModel("用户查询参数")
public class UserQueryDto extends PageParam {
    private static final long serialVersionUID = -2928961627904492808L;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * 用户邮箱
     */
    @ApiModelProperty("用户邮箱")
    private String email;

    /**
     * 用户类型
     */
    @ApiModelProperty("用户类型")
    private Integer userType;

    /**
     * 用户状态
     */
    @ApiModelProperty("用户状态")
    private Integer userStatus;

    /**
     * 登录渠道, 01:移动端; 02:PC端
     */
    @ApiModelProperty("登录渠道")
    private String loginChan;

    /**
     * 登录类型, 00:访客登录; 01:密码登录; 02:QQ登录; 99:未知
     */
    @ApiModelProperty("登录类型")
    private String loginType;

    /**
     * 登录地址
     */
    @ApiModelProperty("登录地址")
    private String loginAddress;

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    private Long roleId;
}
