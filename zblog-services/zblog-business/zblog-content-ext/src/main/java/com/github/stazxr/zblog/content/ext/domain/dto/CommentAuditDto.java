package com.github.stazxr.zblog.content.ext.domain.dto;

import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 评论审核信息
 *
 * @author SunTao
 * @since 2026-09-09
 */
@Getter
@Setter
@ApiModel("评论审核DTO")
public class CommentAuditDto extends BaseDto {
    private static final long serialVersionUID = -5289861858508492439L;

    /**
     * 评论id
     */
    @NotNull(message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("评论id")
    private Long commentId;

    /**
     * 评论审核状态
     */
    @NotNull(message = "{COMMENT_AUDIT_STATUS_REQUIRED}")
    @ApiModelProperty("评论审核状态")
    private Integer status;
}
