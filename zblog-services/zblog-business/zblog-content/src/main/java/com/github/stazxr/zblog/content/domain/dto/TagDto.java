package com.github.stazxr.zblog.content.domain.dto;

import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 标签信息
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
@ApiModel("标签DTO")
public class TagDto extends BaseDto {
    private static final long serialVersionUID = -3328999358062127622L;

    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    private Long id;

    /**
     * 标签名称
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{TAG_NAME_REQUIRED}")
    @ApiModelProperty("标签名称")
    private String name;

    /**
     * 唯一标识(URL)
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{TAG_SLUG_REQUIRED}")
    @ApiModelProperty("唯一标识(URL)")
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
     * 是否允许搜索引擎收录
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{TAG_ALLOWINDEX_REQUIRED}")
    @ApiModelProperty("是否允许搜索引擎收录")
    private Boolean allowIndex;

    /**
     * 是否启用
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{TAG_ENABLED_REQUIRED}")
    @ApiModelProperty("是否启用")
    private Boolean enabled;
}
