package com.github.stazxr.zblog.content.ext.domain.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 主题页面查询参数
 *
 * @author SunTao
 * @since 2026-06-25
 */
@Getter
@Setter
@ApiModel("主题页面查询参数")
public class ThemePageQueryDto implements Serializable {
    private static final long serialVersionUID = 5225022445642674702L;

    /**
     * 主题id
     */
    @ApiModelProperty("主题id")
    private Long themeId;
}
