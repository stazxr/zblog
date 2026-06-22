package com.github.stazxr.zblog.content.ext.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 主题封面信息
 *
 * @author SunTao
 * @since 2026-06-14
 */
@Getter
@Setter
@ApiModel("主题封面VO")
public class ThemePageVo extends BaseVo {
    private static final long serialVersionUID = 1491930301706205484L;

    /**
     * 主题封面id
     */
    @ApiModelProperty("主题封面id")
    private Long id;

    /**
     * 主题id
     */
    @ApiModelProperty("主题id")
    private Long themeId;

    /**
     * 页面id
     */
    @ApiModelProperty("页面id")
    private Long pageId;

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

    /**
     * 封面地址
     */
    @ApiModelProperty("封面地址")
    private String pageCover;
}