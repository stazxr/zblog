package com.github.stazxr.zblog.base.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户更新字段信息
 *
 * @author SunTao
 * @since 2022-07-31
 */
@Getter
@Setter
@ApiModel("用户信息")
public class UserDto extends BaseDto {
    private static final long serialVersionUID = -2949645121252776835L;

    /**
     * 用户id
     */
    @NotNull(groups = Update.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty(value = "用户id")
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{USER_USERNAME_REQUIRED}")
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 用户邮箱
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{USER_EMAIL_REQUIRED}")
    @ApiModelProperty("用户邮箱")
    private String email;

    /**
     * 用户类型
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{USER_USERTYPE_REQUIRED}")
    @ApiModelProperty("用户类型")
    private Integer userType;

    /**
     * 用户状态
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{USER_USERSTATUS_REQUIRED}")
    @ApiModelProperty("用户状态")
    private Integer userStatus;

    /**
     * 临时用户账户过期时间
     */
    @ApiModelProperty("临时用户账户过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    /**
     * 角色id列表
     */
    @ApiModelProperty("角色id列表")
    private List<Long> roleIds;
}
