package com.github.stazxr.zblog.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 说说点赞信息
 *
 * @author SunTao
 * @since 2023-02-07
 */
@Getter
@Setter
@ToString
public class TalkLikeDto {
    /**
     * 点赞用户
     */
    private Long userId;

    /**
     * 说说ID
     */
    private Long talkId;
}
