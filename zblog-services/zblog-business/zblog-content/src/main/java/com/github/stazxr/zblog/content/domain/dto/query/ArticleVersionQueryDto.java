package com.github.stazxr.zblog.content.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 文章发布版本查询参数
 *
 * @author SunTao
 * @since 2026-10-27
 */
@Getter
@Setter
@ApiModel("文章发布版本查询参数")
public class ArticleVersionQueryDto extends PageParam {
    private static final long serialVersionUID = -5344294041388321680L;

    /**
     * 文章id
     */
    @NotNull(message = "{TECH_PARAM_MISS}")
    @ApiModelProperty(value = "文章id", required = true)
    private Long articleId;
}
