package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 友链
 *
 * @author SunTao
 * @since 2021-03-16
 */
@Getter
@Setter
@TableName("friend_link")
public class FriendLink extends BaseEntity {
    /**
     * 主键
     */
    @TableId
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

    /**
     * 是否已删除（逻辑操作，保护数据）
     */
    @TableLogic
    private Boolean deleted;
}
