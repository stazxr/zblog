package com.github.stazxr.zblog.content.ext.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 网站配置信息
 *
 * @author SunTao
 * @since 2026-08-20
 */
@Getter
@Setter
@ApiModel("网站配置DTO")
public class WebsiteConfigDto implements Serializable {
    private static final long serialVersionUID = -9063664843210631407L;

    /**
     * 主键
     */
    @NotNull(message = "{TECH_PARAM_MISS}")
    @ApiModelProperty("网站配置id")
    private Long id;

    /**
     * 网站名称
     */
    @ApiModelProperty("网站名称")
    @NotBlank(message = "{WEBSITE_CONFIG_NAME_REQUIRED}")
    private String websiteName;

    /**
     * 网站标题
     */
    @ApiModelProperty("网站标题")
    @NotBlank(message = "{WEBSITE_CONFIG_TITLE_REQUIRED}")
    private String websiteTitle;

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
     * 网站签名
     */
    @ApiModelProperty("网站签名")
    private String websiteSignature;

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
     * 网站创建日期
     */
    @ApiModelProperty("网站创建日期")
    private LocalDate websiteCreateTime;

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
     * 网站统计代码
     */
    @ApiModelProperty("网站统计代码")
    private String statisticsCode;

    /**
     * 版本号
     */
    @ApiModelProperty("版本号")
    private Integer version;
}