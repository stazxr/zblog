package com.github.stazxr.zblog.content.ext.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 主题查询参数
 *
 * @author SunTao
 * @since 2026-06-14
 */
@Getter
@Setter
@ApiModel("主题查询参数")
public class ThemeQueryDto extends PageParam {
    private static final long serialVersionUID = 8779876896683000485L;

    /**
     * 主题名称
     */
    @ApiModelProperty("主题名称")
    private String themeName;

    /**
     * 主题类型
     */
    @ApiModelProperty("主题类型")
    private String themeType;

    /**
     * 主题归属
     */
    @ApiModelProperty("主题归属")
    private String ownerType;

    /**
     * 主题所有者名称
     */
    @ApiModelProperty("主题所有者名称")
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
}
