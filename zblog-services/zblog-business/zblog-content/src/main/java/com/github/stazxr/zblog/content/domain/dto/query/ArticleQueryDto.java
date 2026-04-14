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
     * 作者ID
     */
    @ApiModelProperty("作者ID")
    private Long authorId;

    /**
     * 状态查询
     */
    @ApiModelProperty("状态查询")
    private Integer tagStatus;

    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    private String title;

    /**
     * 文章唯一标识
     */
    @ApiModelProperty("文章唯一标识")
    private String slug;

    /**
     * 分类ID
     */
    @ApiModelProperty("分类ID")
    private Long categoryId;

    /**
     * 标签ID
     */
    @ApiModelProperty("标签ID")
    private Long tagId;

    /**
     * 文章类型
     */
    @ApiModelProperty("文章类型，1：原创、2：转载、3：翻译")
    private Integer articleType;

    /**
     * 评论状态
     */
    @ApiModelProperty("评论状态")
    private Boolean commentFlag;








    // TODO

    /**
     * 文章id
     */
    @ApiModelProperty("文章id")
    private Long articleId;

    /**
     * 文章专栏
     */
    @ApiModelProperty("文章专栏")
    private Long columnId;

    /**
     * 作者用户名、昵称
     */
    @ApiModelProperty("作者用户名、昵称")
    private String author;
}
