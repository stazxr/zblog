package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 友链健康检测信息
 *
 * @author SunTao
 * @since 2026-08-31
 */
@Getter
@Setter
@TableName("friend_link_health")
public class FriendLinkHealth implements Serializable {
    private static final long serialVersionUID = -3201210819486424020L;

    /**
     * 友链id
     */
    @TableId
    private Long linkId;

    /**
     * 检测状态
     */
    private Boolean status;

    /**
     * 连续失败次数
     */
    private Integer failCount;

    /**
     * 最后检测时间
     */
    private LocalDateTime lastCheckTime;

    /**
     * 最后成功时间
     */
    private LocalDateTime lastSuccessTime;

    /**
     * 最后失败时间
     */
    private LocalDateTime lastFailTime;

    /**
     * 响应耗时ms
     */
    private Long responseTime;

    /**
     * HTTP状态码
     */
    private Integer httpStatus;

    /**
     * 错误信息
     */
    private String errorMsg;
}
