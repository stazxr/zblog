package com.github.stazxr.zblog.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 友链信息
 *
 * @author SunTao
 * @since 2022-12-07
 */
@Getter
@Setter
@ToString
public class FriendLinkDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 链接名称
     */
    private String name;

    /**
     * 头像地址
     */
    private String headUrl;

    /**
     * 链接地址
     */
    private String linkUrl;

    /**
     * 链接介绍
     */
    private String linkRemark;

    /**
     * 是否有效
     */
    private Boolean valid;
}
