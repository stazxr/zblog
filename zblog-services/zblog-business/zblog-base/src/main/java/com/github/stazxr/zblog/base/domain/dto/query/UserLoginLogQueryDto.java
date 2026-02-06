package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 用户登录日志查询参数
 *
 * @author SunTao
 * @since 2025-01-23
 */
@Getter
@Setter
@ApiModel("用户登录日志查询参数")
public class UserLoginLogQueryDto extends PageParam {
    private static final long serialVersionUID = -8231077447608203396L;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 登录结果
     */
    @ApiModelProperty(value = "登录结果")
    private Boolean loginResult;

    /**
     * 登录开始时间
     */
    @ApiModelProperty("登录开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginStartTime;

    /**
     * 登录结束时间
     */
    @ApiModelProperty("登录结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginEndTime;

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
     * 登录ip
     */
    @ApiModelProperty(value = "登录ip")
    private String loginIp;
}
