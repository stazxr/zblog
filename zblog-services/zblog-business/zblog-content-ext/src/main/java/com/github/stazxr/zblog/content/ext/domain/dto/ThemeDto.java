package com.github.stazxr.zblog.content.ext.domain.dto;

import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.content.ext.domain.enums.ThemeType;
import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 主题信息
 *
 * @author SunTao
 * @since 2026-06-14
 */
@Getter
@Setter
@ApiModel("主题DTO")
public class ThemeDto extends BaseDto {
    private static final long serialVersionUID = -3434434170467181151L;

    /**
     * 主题id
     */
    @NotNull(groups = Update.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("主题id")
    private Long id;

    /**
     * 主题名称
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{THEME_THEMENAME_REQUIRED}")
    @ApiModelProperty("主题名称")
    private String themeName;

    /**
     * 主题类型
     */
    @NotNull(groups = Create.class, message = "{THEME_THEMETYPE_REQUIRED}")
    @ApiModelProperty("主题类型")
    private ThemeType themeType;

    /**
     * 主题预览图
     */
    @ApiModelProperty("主题预览图")
    private String previewCover;

    /**
     * 主题预览图id
     */
    @ApiModelProperty("主题预览图id")
    private Long previewCoverId;

    /**
     * 主题描述
     */
    @ApiModelProperty("主题描述")
    private String description;
}