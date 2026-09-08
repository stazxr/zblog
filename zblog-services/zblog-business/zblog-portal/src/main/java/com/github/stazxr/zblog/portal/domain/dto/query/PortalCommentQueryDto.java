package com.github.stazxr.zblog.portal.domain.dto.query;

import com.github.stazxr.zblog.bas.validation.group.Group1;
import com.github.stazxr.zblog.bas.validation.group.Group2;
import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 评论查询信息
 *
 * @author SunTao
 * @since 2026-09-07
 */
@Getter
@Setter
@ApiModel("评论查询参数")
public class PortalCommentQueryDto extends PageParam {
    private static final long serialVersionUID = 1689797746776034728L;

    /**
     * 评论类型
     */
    @NotNull(groups = Group1.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("评论类型")
    private Integer type;

    /**
     * 评论对象ID
     */
    @NotNull(groups = Group1.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("评论对象ID")
    private String objectId;

    /**
     * 父评论ID
     */
    @NotNull(groups = Group2.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("父评论ID")
    private Long parentId;

    /**
     * 评论用户ID
     */
    @ApiModelProperty("评论用户ID")
    private Long userId;

    /**
     * 评论访客ID
     */
    @ApiModelProperty("评论访客ID")
    private String visitorId;
}
