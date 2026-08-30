package com.github.stazxr.zblog.content.ext.domain.dto;

import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 友链审核信息
 *
 * @author SunTao
 * @since 2026-08-31
 */
@Getter
@Setter
@ApiModel("友链审核DTO")
public class FriendLinkAuditDto extends BaseDto {
    private static final long serialVersionUID = 7042566003586565623L;

    /**
     * 友链id
     */
    @NotNull(message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("友链id")
    private Long friendLinkId;

    /**
     * 友链状态
     */
    @NotNull(message = "{FRIEND_LINK_STATUS_REQUIRED}")
    @ApiModelProperty("友链状态")
    private Integer status;
}
