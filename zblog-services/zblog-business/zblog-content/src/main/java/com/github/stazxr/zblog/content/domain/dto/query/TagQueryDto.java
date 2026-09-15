package com.github.stazxr.zblog.content.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 标签查询
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
@ApiModel("标签查询参数")
public class TagQueryDto extends PageParam {
    private static final long serialVersionUID = 2296445247589027937L;

    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称", notes = "模糊查询")
    private String name;

    /**
     * 路径标识
     */
    @ApiModelProperty("路径标识")
    private String slug;

    /**
     * SEO收录状态
     */
    @ApiModelProperty("SEO收录状态")
    private Boolean seoSearch;

    /**
     * 标签状态
     */
    @ApiModelProperty("标签状态")
    private Boolean enabled;
}
