package com.github.stazxr.zblog.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 评论信息
 *
 * @author SunTao
 * @since 2023-02-07
 */
@Getter
@Setter
@ToString
public class CommentDeleteDto {
    /**
     * 评论用户
     */
    private Long userId;

    /**
     * 回复用户
     */
    private Long commentId;
}
