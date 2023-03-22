package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文章查询
 *
 * @author SunTao
 * @since 2022-12-03
 */
@Getter
@Setter
@ToString
@ApiModel("文章查询参数")
public class ArticleQueryDto extends PageParam {
    /**
     * 登录用户id
     */
    @ApiModelProperty("登录用户id")
    private Long loginUser;

    /**
     * 文章id
     */
    @ApiModelProperty("文章id")
    private Long articleId;

    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    private String title;

    /**
     * 文章关键字
     */
    @ApiModelProperty("文章关键字")
    private String keywords;

    /**
     * 文章分类
     */
    @ApiModelProperty("文章分类")
    private Long categoryId;

    /**
     * 文章标签
     */
    @ApiModelProperty("文章标签")
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

    /**
     * 状态查询
     */
    @ApiModelProperty("状态查询")
    private Integer tagStatus;

    /**
     * 作者用户名、昵称
     */
    @ApiModelProperty("作者用户名、昵称")
    private String author;
}
