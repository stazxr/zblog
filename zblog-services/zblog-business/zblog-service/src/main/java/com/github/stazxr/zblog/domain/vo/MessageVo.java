package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 留言页面展示信息
 *
 * @author SunTao
 * @since 2023-02-03
 */
@Getter
@Setter
public class MessageVo extends BaseVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户IP
     */
    private String ipAddress;

    /**
     * 用户地址
     */
    private String ipSource;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 留言内容
     */
    private String messageContent;

    /**
     * 是否审核
     */
    private Boolean isReview;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 弹幕速度
     */
    private Integer time;
}
