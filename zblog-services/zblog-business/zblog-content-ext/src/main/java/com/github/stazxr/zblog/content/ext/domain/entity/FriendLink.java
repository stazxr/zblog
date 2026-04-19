package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
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
    private static final long serialVersionUID = 4781446474431803234L;

    /**
     * 友链ID
     */
    @TableId
    private Long id;

    /**
     * 网站名称
     */
    private String name;

    /**
     * 网站地址
     */
    private String url;

    /**
     * 网站Logo
     */
    private String logo;

    /**
     * 网站描述
     */
    private String description;

    /**
     * 申请人邮箱
     */
    private String email;

    /**
     * 联系方式（QQ/微信等）
     */
    private String contact;

    /**
     * 审批状态
     */
    private Integer status;

    /**
     * 是否展示
     */
    private Boolean isVisible;

    /**
     * 是否允许传递SEO权重
     */
    private Boolean allowFollow;

    /**
     * 排序值（越大越靠前）
     */
    private Integer sort;
}
