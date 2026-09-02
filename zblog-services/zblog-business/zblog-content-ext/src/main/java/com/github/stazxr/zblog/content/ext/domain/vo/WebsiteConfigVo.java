package com.github.stazxr.zblog.content.ext.domain.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 网站配置信息
 *
 * @author SunTao
 * @since 2026-08-20
 */
@Getter
@Setter
@ApiModel("网站配置VO")
public class WebsiteConfigVo implements Serializable {
    private static final long serialVersionUID = 7489326294967407360L;

    /**
     * 主键
     */
    @ApiModelProperty("网站配置id")
    private Long id;

    /**
     * 网站名称
     */
    @ApiModelProperty("网站名称")
    private String websiteName;

    /**
     * 网站标题
     */
    @ApiModelProperty("网站标题")
    private String websiteTitle;

    /**
     * 网站简介
     */
    @ApiModelProperty("网站简介")
    private String websiteIntro;

    /**
     * 网站LOGO
     */
    @ApiModelProperty("网站LOGO")
    private String websiteLogo;

    /**
     * 网站ICON
     */
    @ApiModelProperty("网站ICON")
    private String websiteFavicon;

    /**
     * 网站封面
     */
    @ApiModelProperty("网站封面")
    private String websiteCover;

    /**
     * 作者名称
     */
    @ApiModelProperty("作者名称")
    private String websiteAuthor;

    /**
     * 作者头像
     */
    @ApiModelProperty("作者头像")
    private String websiteAvatar;

    /**
     * 网站创建日期
     */
    @JSONField(format = "yyyy-MM-dd")
    @ApiModelProperty("网站创建日期")
    private LocalDate websiteCreateTime;

    /**
     * 网站关键词
     */
    @ApiModelProperty("网站关键词")
    private String websiteKeywords;

    /**
     * 网站描述
     */
    @ApiModelProperty("网站描述")
    private String websiteDescription;

    /**
     * 页脚签名
     */
    @ApiModelProperty("页脚签名")
    private String footerSignature;

    /**
     * 页脚导航显示开关
     */
    @ApiModelProperty("页脚导航显示开关")
    private Boolean footerNavbarSwitch;

    /**
     * 页脚背景图
     */
    @ApiModelProperty("页脚背景图")
    private String footerBackground;

    /**
     * 网站字体地址
     */
    @ApiModelProperty("网站字体地址")
    private String fontUrl;

    /**
     * 友链申请开关
     */
    @ApiModelProperty("友链申请开关")
    private Boolean friendLinkApplySwitch;

    /**
     * 友链健康检测失败几次则下线友链
     */
    @ApiModelProperty("友链健康检测失败阙值")
    private Integer friendLinkCheckFailedCount;

    /**
     * 弹幕加载量
     */
    @ApiModelProperty("弹幕加载量")
    private Integer barrageMessageLoadSize;

    /**
     * HTTPS 升级开关
     */
    @ApiModelProperty("HTTPS 升级开关")
    private Boolean httpsSwitch;

    /**
     * ICP备案号
     */
    @ApiModelProperty("ICP备案号")
    private String websiteIcpNo;

    /**
     * 公安备案号
     */
    @ApiModelProperty("公安备案号")
    private String websitePoliceNo;

    /**
     * 网站统计代码
     */
    @ApiModelProperty("网站统计代码")
    private String statisticsCode;

    /**
     * 版本号
     */
    @ApiModelProperty("版本号")
    private Integer version;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 网站链接配置列表
     */
    @ApiModelProperty("网站链接配置列表")
    private List<WebsiteLinkConfigVo> links;

    /**
     * 评论表情包
     */
    @ApiModelProperty("评论表情包")
    private List<CommentEmojiVo> commentEmojis;
}