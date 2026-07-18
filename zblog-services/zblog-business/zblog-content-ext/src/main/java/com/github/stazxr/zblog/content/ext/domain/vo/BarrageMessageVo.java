package com.github.stazxr.zblog.content.ext.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.stazxr.zblog.audit.model.AuditRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 弹幕信息
 *
 * @author SunTao
 * @since 2026-07-06
 */
@Getter
@Setter
@ApiModel("弹幕VO")
public class BarrageMessageVo implements Serializable {
    private static final long serialVersionUID = -1783044350087059808L;

    /**
     * 弹幕id
     */
    @ApiModelProperty("弹幕id")
    private Long id;

    /**
     * 弹幕内容
     */
    @ApiModelProperty("弹幕内容")
    private String content;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String avatar;

    /**
     * IP
     */
    @ApiModelProperty("IP")
    private String ip;

    /**
     * ip归属地
     */
    @ApiModelProperty("ip归属地")
    private String ipRegion;

    /**
     * 用户代理
     */
    @ApiModelProperty("用户代理")
    private String userAgent;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private Integer auditStatus;

    /**
     * 审核拒绝原因
     */
    @ApiModelProperty("审核拒绝原因")
    private String auditReason;

    /**
     * 审核用户id
     */
    @ApiModelProperty("审核用户id")
    private Long auditUserId;

    /**
     * 审核用户名
     */
    @ApiModelProperty("审核用户名")
    private String auditUsername;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime auditTime;

    /**
     * 设备id
     */
    @ApiModelProperty("设备id")
    private String deviceId;

    /**
     * 弹幕颜色
     */
    @ApiModelProperty("弹幕颜色")
    private String color;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 审核记录
     */
    @ApiModelProperty("审核记录")
    private AuditRecord auditRecord;
}