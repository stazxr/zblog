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
     * 业务id
     */
    @ApiModelProperty("业务id")
    private Long businessId;

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
