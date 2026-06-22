package com.github.stazxr.zblog.content.ext.domain.vo;

import com.github.stazxr.zblog.content.ext.domain.enums.ThemeOwnerType;
import com.github.stazxr.zblog.content.ext.domain.enums.ThemeType;
import com.github.stazxr.zblog.core.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 主题信息
 *
 * @author SunTao
 * @since 2026-06-14
 */
@Getter
@Setter
@ApiModel("主题VO")
public class ThemeVo extends BaseVo {
    private static final long serialVersionUID = -3434434170467181151L;

    /**
     * 主题id
     */
    @ApiModelProperty("主题id")
    private Long id;

    /**
     * 主题名称
     */
    @ApiModelProperty("主题名称")
    private String themeName;

    /**
     * 主题类型
     */
    @ApiModelProperty("主题类型")
    private ThemeType themeType;

    /**
     * 主题归属
     */
    @ApiModelProperty("主题归属")
    private ThemeOwnerType ownerType;

    /**
     * 所属用户id
     */
    @ApiModelProperty("所属用户id")
    private Long ownerId;

    /**
     * 所属用户名称
     */
    @ApiModelProperty("所属用户名称")
    private String ownerName;

    /**
     * 是否默认主题
     */
    @ApiModelProperty("是否默认主题")
    private Boolean isDefault;

    /**
     * 是否激活主题
     */
    @ApiModelProperty("是否激活主题")
    private Boolean isActive;

    /**
     * 主题预览图
     */
    @ApiModelProperty("主题预览图")
    private String previewCover;

    /**
     * 主题描述
     */
    @ApiModelProperty("主题描述")
    private String description;

    /**
     * 主题封面列表
     */
    @ApiModelProperty("主题封面列表")
    private List<ThemePageVo> pageList;
}