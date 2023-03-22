package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户查询
 *
 * @author SunTao
 * @since 2022-09-13
 */
@Getter
@Setter
@ToString
@ApiModel("用户查询参数")
public class UserQueryDto extends PageParam {
    /**
     * 业务id
     */
    @ApiModelProperty("业务id")
    private Long businessId;

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
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String telephone;

    /**
     * 启用状态
     */
    @ApiModelProperty(value = "启用状态", example = "true")
    private Boolean enabled;

    /**
     * 登录开始时间
     */
    @ApiModelProperty("登录开始时间")
    private String loginStartTime;

    /**
     * 登录结束时间
     */
    @ApiModelProperty("登录结束时间")
    private String loginEndTime;
}
