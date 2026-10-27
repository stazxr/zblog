package com.github.stazxr.zblog.content.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 文章操作日志查询参数
 *
 * @author SunTao
 * @since 2026-10-27
 */
@Getter
@Setter
@ApiModel("文章操作日志查询参数")
public class ArticleOperationLogQueryDto extends PageParam {
    private static final long serialVersionUID = -3090316711495999592L;

    /**
     * 文章id
     */
    @NotNull(message = "{TECH_PARAM_MISS}")
    @ApiModelProperty(value = "文章id", required = true)
    private Long articleId;
}
