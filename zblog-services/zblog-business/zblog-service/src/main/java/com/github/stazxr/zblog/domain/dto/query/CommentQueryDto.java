package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 评论查询
 *
 * @author SunTao
 * @since 2023-02-03
 */
@Getter
@Setter
@ToString
@ApiModel("评论查询参数")
public class CommentQueryDto extends PageParam {
    /**
     * 页码
     */
    @ApiModelProperty("页码")
    private Integer current;

    /**
     * 评论类型
     */
    @ApiModelProperty("评论类型，1：文章、2：友链、3：说说")
    private Integer type;

    /**
     * 评论对象
     */
    @ApiModelProperty("评论对象")
    private Long objectId;

    /**
     * 上级评论
     */
    @ApiModelProperty("上级评论")
    private Long parentId;

    /**
     * 评论状态
     */
    @ApiModelProperty("评论状态，1：已审核、2、未审核")
    private Integer status;

    /**
     * 评论文章标题
     */
    @ApiModelProperty("评论文章标题")
    private String articleTitle;

    /**
     * 评论说说标题
     */
    @ApiModelProperty("评论说说标题")
    private String talkTitle;

    /**
     * 评论用户
     */
    @ApiModelProperty("评论用户")
    private String nickname;
}
