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
 * 敏感词信息
 *
 * @author SunTao
 * @since 2026-07-23
 */
@Getter
@Setter
@ApiModel("敏感词DTO")
public class SensitiveWordDto extends BaseDto {
    private static final long serialVersionUID = -5354240181727096000L;

    /**
     * 敏感词id
     */
    @NotNull(groups = Update.class, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("敏感词id")
    private Long id;

    /**
     * 敏感词
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{SENSITIVE_WORD_REQUIRED}")
    @ApiModelProperty("敏感词")
    private String word;

    /**
     * 状态
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{SENSITIVE_WORD_STATUS_REQUIRED}")
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
}
