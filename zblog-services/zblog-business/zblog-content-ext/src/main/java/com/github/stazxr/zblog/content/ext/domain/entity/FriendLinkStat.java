package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 友链统计信息
 *
 * @author SunTao
 * @since 2026-08-30
 */
@Getter
@Setter
@TableName("friend_link_stat")
public class FriendLinkStat implements Serializable {
    private static final long serialVersionUID = -1752955371882697923L;

    /**
     * 友链id
     */
    @TableId
    private Long linkId;

    /**
     * 总点击数
     */
    private Long clickCount;

    /**
     * 日点击数
     */
    private Integer dayClick;

    /**
     * 周点击数
     */
    private Integer weekClick;

    /**
     * 月点击数
     */
    private Integer monthClick;

    /**
     * 上次访问时间
     */
    private LocalDateTime lastClickTime;
}
