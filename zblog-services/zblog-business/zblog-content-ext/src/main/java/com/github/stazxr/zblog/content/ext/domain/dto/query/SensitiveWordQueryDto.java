package com.github.stazxr.zblog.content.ext.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 敏感词查询参数
 *
 * @author SunTao
 * @since 2026-06-12
 */
@Getter
@Setter
@ApiModel("敏感词查询参数")
public class SensitiveWordQueryDto extends PageParam {
    private static final long serialVersionUID = -1214395791386269505L;

    /**
     * 敏感词
     */
    @ApiModelProperty("敏感词")
    private String word;

    /**
     * 敏感词状态
     */
    @ApiModelProperty("敏感词状态")
    private Integer status;
}
