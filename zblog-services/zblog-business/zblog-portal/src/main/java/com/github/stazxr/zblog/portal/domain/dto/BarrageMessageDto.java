package com.github.stazxr.zblog.portal.domain.dto;

import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 弹幕信息
 *
 * @author SunTao
 * @since 2026-07-07
 */
@Getter
@Setter
@ApiModel("弹幕DTO")
public class BarrageMessageDto extends BaseDto {
    private static final long serialVersionUID = 1779268750313595097L;

    /**
     * 弹幕内容
     */
    @NotBlank(message = "{BARRAGE_MESSAGE_CONTENT_REQUIRED}")
    @ApiModelProperty("弹幕内容")
    private String content;
}
