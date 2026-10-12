package com.github.stazxr.zblog.content.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章自动保存列表查询参数
 *
 * @author SunTao
 * @since 2026-10-12
 */
@Getter
@Setter
@ApiModel("文章自动保存列表查询参数")
public class ArticleAutoSaveQueryDto extends PageParam {
    private static final long serialVersionUID = -8244574620577534915L;

    /**
     * 文章id
     */
    @ApiModelProperty("文章id")
    private Long articleId;
}
