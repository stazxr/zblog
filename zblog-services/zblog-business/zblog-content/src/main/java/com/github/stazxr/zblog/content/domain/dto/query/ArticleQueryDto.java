package com.github.stazxr.zblog.content.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章查询
 *
 * @author SunTao
 * @since 2022-12-03
 */
@Getter
@Setter
@ApiModel("文章查询参数")
public class ArticleQueryDto extends PageParam {
    private static final long serialVersionUID = 6190775176041347791L;

    /**
     * 作者id
     */
    @ApiModelProperty("作者id")
    private Long authorId;

    /**
     * 状态查询
     */
    @ApiModelProperty("状态查询")
    private String tagStatus;

    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    private String title;

    /**
     * 路径标识
     */
    @ApiModelProperty("路径标识")
    private String slug;

    /**
     * 分类id
     */
    @ApiModelProperty("分类id")
    private Long categoryId;

    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    private Long tagId;

    /**
     * 文章类型
     */
    @ApiModelProperty("文章类型")
    private Integer articleType;

    /**
     * 文章权限
     */
    @ApiModelProperty("文章权限")
    private Integer articlePerm;

    /**
     * 评论状态
     */
    @ApiModelProperty("评论状态")
    private Boolean commentFlag;
}
