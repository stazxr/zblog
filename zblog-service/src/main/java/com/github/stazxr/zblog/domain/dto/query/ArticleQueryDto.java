package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
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
public class ArticleQueryDto extends PageParam {
    /**
     * 登录用户序号
     */
    private Long loginUser;

    /**
     * 文章序列
     */
    private Long articleId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章关键字
     */
    private String keywords;

    /**
     * 文章分类
     */
    private Long categoryId;

    /**
     * 文章类型
     */
    private Integer articleType;

    /**
     * 评论状态
     */
    private Boolean commentFlag;

    /**
     * 状态查询
     */
    private Integer tagStatus;

    /**
     * 作者用户名、昵称
     */
    private String author;
}
