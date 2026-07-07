package com.github.stazxr.zblog.content.ext.domain.dto;

import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 弹幕审核信息
 *
 * @author SunTao
 * @since 2027-07-06
 */
@Getter
@Setter
@ApiModel("弹幕审核DTO")
public class BarrageMessageAuditDto extends BaseDto {
    private static final long serialVersionUID = -5369954701902504527L;

    /**
     * 弹幕id
     */
    @NotNull(groups = Update.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("弹幕id")
    private Long barrageMessageId;

    /**
     * 弹幕状态
     */
    @NotNull(message = "{BARRAGE_MESSAGE_STATUS_REQUIRED}")
    @ApiModelProperty("弹幕状态")
    private Integer auditStatus;

    /**
     * 审核拒绝原因
     */
    @ApiModelProperty("审核拒绝原因")
    private String auditReason;
}
