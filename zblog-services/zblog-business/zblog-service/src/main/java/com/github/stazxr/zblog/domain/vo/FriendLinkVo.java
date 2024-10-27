package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 友链信息
 *
 * @author SunTao
 * @since 2022-12-07
 */
@Getter
@Setter
public class FriendLinkVo extends BaseVo {
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
