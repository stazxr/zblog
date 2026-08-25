package com.github.stazxr.zblog.content.ext.domain.dto;

import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 网站链接配置信息
 *
 * @author SunTao
 * @since 2026-08-22
 */
@Getter
@Setter
@ApiModel("网站配置VO")
public class WebsiteLinkConfigDto implements Serializable {
    private static final long serialVersionUID = 620060912562241496L;

    /**
     * 主键
     */
    @NotNull(groups = Update.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("网站链接配置id")
    private Long id;

    /**
     * 链接名称
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{WEBSITE_LINK_NAME_REQUIRED}")
    @ApiModelProperty("链接名称")
    private String linkName;

    /**
     * 链接类型
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{WEBSITE_LINK_TYPE_REQUIRED}")
    @ApiModelProperty("链接类型")
    private String linkType;

    /**
     * 链接地址
     */
    @ApiModelProperty("链接地址")
    private String linkUrl;

    /**
     * 图标
     */
    @ApiModelProperty("图标")
    private String linkIcon;

    /**
     * 排序
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{PARAM_SORT_REQUIRED}")
    @Min(value = 1, groups = {Create.class, Update.class}, message = "{PARAM_SORT_MIN1}")
    @Max(value = 99999, groups = {Create.class, Update.class}, message = "{PARAM_SORT_MAX99999}")
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 是否启用
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{WEBSITE_LINK_ENABLED_REQUIRED}")
    @ApiModelProperty("是否启用")
    private Boolean enabled;
}
