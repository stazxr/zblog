package com.github.stazxr.zblog.content.ext.domain.dto;

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
 * 主题页面信息
 *
 * @author SunTao
 * @since 2026-06-14
 */
@Getter
@Setter
@ApiModel("主题页面DTO")
public class ThemePageDto extends BaseDto {
    private static final long serialVersionUID = 6923634728898744749L;

    /**
     * 主题页面id
     */
    @NotNull(groups = Update.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("主题页面id")
    private Long id;

    /**
     * 主题id
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("主题id")
    private Long themeId;

    /**
     * 页面id
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{THEME_PAGEID_REQUIRED}")
    @ApiModelProperty("页面id")
    private Long pageId;

    /**
     * 页面预览图
     */
    @ApiModelProperty("页面预览图")
    private String pageCover;

    /**
     * 页面预览图id
     */
    @ApiModelProperty("页面预览图id")
    private Long pageCoverId;
}
