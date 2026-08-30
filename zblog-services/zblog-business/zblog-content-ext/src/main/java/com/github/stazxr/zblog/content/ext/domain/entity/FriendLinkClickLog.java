package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 友链点击日志
 *
 * @author SunTao
 * @since 2026-08-30
 */
@Getter
@Setter
@TableName("friend_link_click_log")
public class FriendLinkClickLog implements Serializable {
    private static final long serialVersionUID = 7436128475982059523L;

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 友链id
     */
    private Long linkId;

    /**
     * 访客id
     */
    private String visitorId;

    /**
     * IP
     */
    private String ip;

    /**
     * 访问时间
     */
    private LocalDateTime createTime;
}
