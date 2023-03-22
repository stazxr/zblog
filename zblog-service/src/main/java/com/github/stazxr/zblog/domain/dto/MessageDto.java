package com.github.stazxr.zblog.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 弹幕信息
 *
 * @author SunTao
 * @since 2023-02-03
 */
@Getter
@Setter
@ToString
@ApiModel("弹幕信息")
public class MessageDto {
    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String avatar;

    /**
     * 留言内容
     */
    @ApiModelProperty("留言内容")
    private String messageContent;

    /**
     * 弹幕速度
     */
    @ApiModelProperty("弹幕速度")
    private Integer time;
}
