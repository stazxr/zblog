package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 弹幕点赞
 *
 * @author SunTao
 * @since 2026-07-20
 */
@Getter
@Setter
@TableName("barrage_message_like")
public class BarrageMessageLike implements Serializable {
    private static final long serialVersionUID = 2879273826278285548L;

    /**
     * 弹幕点赞id
     */
    @TableId
    private Long id;

    /**
     * 弹幕id
     */
    private Long barrageMessageId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 访客id
     */
    private String visitorId;

    /**
     * ip
     */
    private String ip;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 点赞时间
     */
    private LocalDateTime createTime;
}
