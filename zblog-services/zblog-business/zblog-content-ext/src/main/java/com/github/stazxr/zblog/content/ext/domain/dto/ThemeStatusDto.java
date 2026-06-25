package com.github.stazxr.zblog.content.ext.domain.dto;

import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 主题状态信息
 *
 * @author SunTao
 * @since 2026-06-23
 */
@Getter
@Setter
@ApiModel("主题状态DTO")
public class ThemeStatusDto extends BaseDto {
    private static final long serialVersionUID = -3434434170467181151L;

    /**
     * 主题id
     */
    @NotNull(message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("主题id")
    private Long themeId;

    /**
     * 主题状态
     */
    @NotNull(message = "{THEME_STATUS_REQUIRED}")
    @ApiModelProperty("主题状态")
    private Boolean status;
}
