package com.github.stazxr.zblog.portal.domain.bo;

import com.github.stazxr.zblog.content.ext.domain.enums.CommentStatus;
import com.github.stazxr.zblog.portal.domain.vo.PortalCommentVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 新增评论返回结果
 *
 * @author SunTao
 * @since 2026-09-12
 */
@Getter
@Setter
@ApiModel("新增评论返回结果")
public class SaveCommentResBo {
    /**
     * 评论状态
     */
    @ApiModelProperty("评论状态")
    private CommentStatus status;

    /**
     * 评论ID
     */
    @ApiModelProperty("评论ID")
    private Long commentId;

    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
    private PortalCommentVo comment;
}
