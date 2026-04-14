package com.github.stazxr.zblog.content.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分类信息
 *
 * @author SunTao
 * @since 2022-11-18
 */
@Getter
@Setter
@ApiModel("分类VO")
public class CategoryVo extends BaseVo {
    private static final long serialVersionUID = 353567845249460600L;

    /**
     * 分类ID
     */
    @ApiModelProperty("分类ID")
    private Long id;

    /**
     * 父分类ID
     */
    @ApiModelProperty("父分类ID")
    private Long pid;

    /**
     * 父分类名称
     */
    @ApiModelProperty("父分类名称")
    private String parentName;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String name;

    /**
     * 唯一标识(URL)
     */
    @ApiModelProperty("唯一标识(URL)")
    private String slug;

    /**
     * 分类图片
     */
    @ApiModelProperty("分类图片")
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
     * 是否允许搜索引擎收录
     */
    @ApiModelProperty("是否允许搜索引擎收录")
    private Boolean allowIndex;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Boolean enabled;

    /**
     * 是否前台展示
     */
    @ApiModelProperty("是否前台展示")
    private Boolean visible;

    /**
     * 文章数
     */
    @ApiModelProperty("文章数")
    private Integer articleCount;

    /**
     * 子分类列表
     */
    @ApiModelProperty("子分类列表")
    private List<CategoryVo> children;

    /**
     * 子分类数
     */
    @ApiModelProperty("子分类数")
    private int subCount;

    /**
     * 是否包含子分类
     */
    @ApiModelProperty("是否包含子分类")
    private Boolean hasChildren;

    public Boolean getHasChildren() {
        return subCount > 0;
    }
}
