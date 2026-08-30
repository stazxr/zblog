package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 网站配置
 *
 * @author SunTao
 * @since 2026-08-20
 */
@Getter
@Setter
@TableName("website_config")
public class WebsiteConfig implements Serializable {
    private static final long serialVersionUID = 8221576795332750887L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 网站名称
     */
    private String websiteName;

    /**
     * 网站标题
     */
    private String websiteTitle;

    /**
     * 网站简介
     */
    private String websiteIntro;

    /**
     * 网站LOGO
     */
    private String websiteLogo;

    /**
     * 网站ICON
     */
    private String websiteFavicon;

    /**
     * 网站封面
     */
    private String websiteCover;

    /**
     * 作者名称
     */
    private String websiteAuthor;

    /**
     * 作者头像
     */
    private String websiteAvatar;

    /**
     * 网站创建日期
     */
    private LocalDate websiteCreateTime;

    /**
     * 网站创建日期
     */
    private String websiteNotice;

    /**
     * 网站关键词
     */
    private String websiteKeywords;

    /**
     * 网站描述
     */
    private String websiteDescription;

    /**
     * 页脚签名
     */
    private String footerSignature;

    /**
     * 页脚导航显示开关
     */
    private Boolean footerNavbarSwitch;

    /**
     * 页脚背景图
     */
    private String footerBackground;

    /**
     * 网站字体地址
     */
    private String fontUrl;

    /**
     * 友链申请开关
     */
    private Boolean friendLinkApplySwitch;

    /**
     * 友链健康检测失败几次则下线友链
     */
    private Integer friendLinkCheckFailedCount;

    /**
     * 弹幕加载量
     */
    private Integer barrageMessageLoadSize;

    /**
     * HTTPS 升级开关
     */
    private Boolean httpsSwitch;

    /**
     * ICP备案号
     */
    private String websiteIcpNo;

    /**
     * 公安备案号
     */
    private String websitePoliceNo;

    /**
     * 网站统计代码
     */
    private String statisticsCode;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}