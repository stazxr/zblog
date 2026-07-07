package com.github.stazxr.zblog.content.ext.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 页面查询参数
 *
 * @author SunTao
 * @since 2026-06-12
 */
@Getter
@Setter
@ApiModel("页面查询参数")
public class PageQueryDto extends PageParam {
    private static final long serialVersionUID = -1214395791386269505L;

    /**
     * 页面名称
     */
    @ApiModelProperty("页面名称")
    private String pageName;

    /**
     * 页面标签
     */
    @ApiModelProperty("页面标签")
    private String pageLabel;
}
