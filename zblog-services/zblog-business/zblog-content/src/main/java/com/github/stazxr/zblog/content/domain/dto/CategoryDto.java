package com.github.stazxr.zblog.content.domain.dto;

import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 分类信息
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
@ApiModel("分类DTO")
public class CategoryDto extends BaseDto {
    private static final long serialVersionUID = -4062052440872083450L;

    /**
     * 分类ID
     */
    @NotNull(groups = Update.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("分类ID")
    private Long id;

    /**
     * 父分类ID
     */
    @ApiModelProperty("父分类ID")
    private Long pid;

    /**
     * 分类名称
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{CATEGORY_NAME_REQUIRED}")
    @ApiModelProperty("分类名称")
    private String name;

    /**
     * 路径标识
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{CATEGORY_SLUG_REQUIRED}")
    @ApiModelProperty("路径标识")
    private String slug;

    /**
     * 分类封面图ID
     */
    @ApiModelProperty("分类封面图ID")
    private Long imageId;

    /**
     * 分类封面图
     */
    @ApiModelProperty("分类封面图")
    private String imageUrl;

    /**
     * 分类描述
     */
    @ApiModelProperty("分类描述")
    private String description;

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
     * 是否前台展示
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{CATEGORY_VISIBILITY_REQUIRED}")
    @ApiModelProperty("是否前台展示")
    private Boolean visible;

    /**
     * SEO收录配置
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{CATEGORY_SEARCH_INDEX_REQUIRED}")
    @ApiModelProperty("SEO收录配置")
    private Boolean seoSearch;

    /**
     * 是否启用
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{CATEGORY_STATUS_REQUIRED}")
    @ApiModelProperty("是否启用")
    private Boolean enabled;

    /**
     * 排序
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{PARAM_SORT_REQUIRED}")
    @Min(value = 1, groups = {Create.class, Update.class}, message = "{PARAM_SORT_MIN1}")
    @Max(value = 99999, groups = {Create.class, Update.class}, message = "{PARAM_SORT_MAX99999}")
    @ApiModelProperty("排序")
    private Integer sort;
}
