package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 说说点赞
 *
 * @author SunTao
 * @since 2023-02-07
 */
@Getter
@Setter
@TableName("talk_like")
public class TalkLike extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 点赞用户
     */
    private Long userId;

    /**
     * 点赞说说
     */
    private Long talkId;

    /**
     * 点赞用户IP
     */
    private String ipAddress;

    /**
     * 点赞用户地址
     */
    private String ipSource;
}
