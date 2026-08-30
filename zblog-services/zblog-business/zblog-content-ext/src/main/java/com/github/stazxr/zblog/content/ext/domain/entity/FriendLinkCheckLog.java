package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 友链健康检查日志
 *
 * @author SunTao
 * @since 2026-08-31
 */
@Getter
@Setter
@TableName("friend_link_check_log")
public class FriendLinkCheckLog implements Serializable {
    private static final long serialVersionUID = 9094866005662238266L;

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
     * 是否成功
     */
    private Boolean success;

    /**
     * HTTP状态码
     */
    private Integer httpStatus;

    /**
     * 响应耗时ms
     */
    private Long responseTime;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 检测时间
     */
    private LocalDateTime createTime;
}
