package com.github.stazxr.zblog.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文章点赞信息
 *
 * @author SunTao
 * @since 2023-02-07
 */
@Getter
@Setter
@ToString
public class ArticleLikeDto {
    /**
     * 点赞用户
     */
    private Long userId;

    /**
     * 文章ID
     */
    private Long articleId;
}
