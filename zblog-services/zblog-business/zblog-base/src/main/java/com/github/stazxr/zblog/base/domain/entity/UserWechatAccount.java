package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户微信公众号配置
 *
 * @author SunTao
 * @since 2026-10-30
 */
@Getter
@Setter
@TableName("user_wechat_account")
public class UserWechatAccount implements Serializable {
    private static final long serialVersionUID = -8078982169678036066L;

    /**
     * 配置id
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 微信公众号AppID
     */
    private String appId;

    /**
     * 微信公众号AppSecret密文
     */
    private String appSecret;

    /**
     * 微信公众号名称
     */
    private String accountName;

    /**
     * 公众号头像
     */
    private String accountAvatar;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 授权验证状态
     */
    private Integer verifyStatus;

    /**
     * 验证时间
     */
    private LocalDateTime verifyTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
