package com.github.stazxr.zblog.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 评论点赞信息
 *
 * @author SunTao
 * @since 2023-02-06
 */
@Getter
@Setter
@ToString
public class CommentLikeDto {
    /**
     * 点赞用户
     */
    private Long userId;

    /**
     * 评论ID
     */
    private Long commentId;
}
