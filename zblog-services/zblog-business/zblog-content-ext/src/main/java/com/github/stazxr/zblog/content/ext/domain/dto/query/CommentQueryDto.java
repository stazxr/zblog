package com.github.stazxr.zblog.content.ext.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 评论查询参数
 *
 * @author SunTao
 * @since 2026-09-09
 */
@Getter
@Setter
@ApiModel("评论查询参数")
public class CommentQueryDto extends PageParam {
    private static final long serialVersionUID = 5151131693274284637L;

}
