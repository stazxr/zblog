package com.github.stazxr.zblog.base.domain.dto;

import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotNull(groups = Update.class, message = "{valid.common.id.NotNull}")
    @ApiModelProperty(value = "用户id")
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{valid.user.username.NotBlank}")
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 用户邮箱
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{valid.user.email.NotBlank}")
    @ApiModelProperty("用户邮箱")
    private String email;

    /**
     * 用户类型
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{valid.user.userType.NotNull}")
    @ApiModelProperty("用户类型")
    private Integer userType;

    /**
     * 用户状态
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{valid.user.userStatus.NotNull}")
    @ApiModelProperty("用户状态")
    private Integer userStatus;

    /**
     * 临时用户有效时间
     */
    @ApiModelProperty("临时用户有效时间")
    private String expiredTime;

    /**
     * 角色id列表
     */
    @ApiModelProperty("角色id列表")
    private List<Long> roleIds;
}
