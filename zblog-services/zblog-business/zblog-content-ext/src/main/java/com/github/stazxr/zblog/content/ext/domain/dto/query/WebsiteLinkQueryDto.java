package com.github.stazxr.zblog.content.ext.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 网站链接查询参数
 *
 * @author SunTao
 * @since 2026-08-23
 */
@Getter
@Setter
@ApiModel("网站链接查询参数")
public class WebsiteLinkQueryDto extends PageParam {
    private static final long serialVersionUID = 322808654247242243L;

    /**
     * 链接名称
     */
    @ApiModelProperty("链接名称")
    private String linkName;

    /**
     * 链接类型
     */
    @ApiModelProperty("链接类型")
    private String linkType;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Boolean enabled;
}
