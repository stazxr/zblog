package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 弹幕
 *
 * @author SunTao
 * @since 2026-07-06
 */
@Getter
@Setter
@TableName("barrage_message")
public class BarrageMessage implements Serializable {
    private static final long serialVersionUID = 3004539345237773536L;

    /**
     * 弹幕id
     */
    @TableId
    private Long id;

    /**
     * 弹幕内容
     */
    private String content;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * ip
     */
    private String ip;

    /**
     * ip归属地
     */
    private String ipRegion;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 审核拒绝原因
     */
    private String auditReason;

    /**
     * 审核用户id
     */
    private Long auditUserId;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 弹幕速度
     */
    private Integer speed;

    /**
     * 弹幕颜色
     */
    private String color;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}