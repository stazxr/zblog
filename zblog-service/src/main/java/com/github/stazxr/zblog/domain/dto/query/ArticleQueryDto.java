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
     * 文章标题
     */
    private String title;

    /**
     * 文章状态
     */
    private Integer articleStatus;

    /**
     * 作者用户名、昵称
     */
    private String author;

    /**
     * 登录用户
     */
    private String loginUser;
}
