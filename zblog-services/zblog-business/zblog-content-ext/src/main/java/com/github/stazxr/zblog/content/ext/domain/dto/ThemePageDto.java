package com.github.stazxr.zblog.content.ext.domain.dto;

import com.github.stazxr.zblog.core.base.BaseDto;
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
@ApiModel("主题封面DTO")
public class ThemePageDto extends BaseDto {
    private static final long serialVersionUID = 6923634728898744749L;

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
     * 封面地址
     */
    @ApiModelProperty("封面地址")
    private String pageCover;
}
