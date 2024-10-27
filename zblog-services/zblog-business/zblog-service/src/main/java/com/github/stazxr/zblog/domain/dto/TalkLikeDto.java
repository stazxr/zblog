package com.github.stazxr.zblog.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("说说点赞信息")
public class TalkLikeDto {
    /**
     * 点赞用户id
     */
    @ApiModelProperty("点赞用户id")
    private Long userId;

    /**
     * 说说id
     */
    @ApiModelProperty("说说id")
    private Long talkId;
}
