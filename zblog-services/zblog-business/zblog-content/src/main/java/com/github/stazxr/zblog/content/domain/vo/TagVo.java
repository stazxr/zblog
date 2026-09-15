package com.github.stazxr.zblog.content.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 标签信息
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
@ApiModel("标签VO")
public class TagVo extends BaseVo {
    private static final long serialVersionUID = -8204822974488033568L;

    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    private Long id;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String name;

    /**
     * 路径标识
     */
    @ApiModelProperty("路径标识")
    private String slug;

    /**
     * SEO标题
     */
    @ApiModelProperty("SEO标题")
    private String seoTitle;

    /**
     * SEO关键词
     */
    @ApiModelProperty("SEO关键词")
    private String seoKeywords;

    /**
     * SEO描述
     */
    @ApiModelProperty("SEO描述")
    private String seoDescription;

    /**
     * SEO收录状态
     */
    @ApiModelProperty("SEO收录状态")
    private Boolean seoSearch;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Boolean enabled;

    /**
     * 关联文章数
     */
    @ApiModelProperty("关联文章数")
    private Integer articleCount;
}
