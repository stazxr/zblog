package com.github.stazxr.zblog.domain.dto.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 网站信息
 *
 * @author SunTao
 * @since 2022-12-08
 */
@Data
@ApiModel("网站基本信息")
public class WebInfo {
    /**
     * 网站头像
     */
    @ApiModelProperty("网站头像")
    private String websiteAvatar = "";

    /**
     * 网站名称
     */
    @ApiModelProperty("网站名称")
    private String websiteName = "";

    /**
     * 网站作者
     */
    @ApiModelProperty("网站作者")
    private String websiteAuthor = "";

    /**
     * 网站创建时间
     */
    @ApiModelProperty("网站创建时间")
    private String websiteCreateTime = "";

    /**
     * 网站介绍
     */
    @ApiModelProperty("网站介绍")
    private String websiteIntro = "";

    /**
     * 网站链接（前台）
     */
    @ApiModelProperty("网站链接（前台）")
    private String websiteLink = "";

    /**
     * 网站链接（后台）
     */
    @ApiModelProperty("网站链接（后台）")
    private String websiteAdminLink = "";

    /**
     * 关于我
     */
    @ApiModelProperty("关于我")
    private String readMeLink = "";

    /**
     * 网站公告
     */
    @ApiModelProperty("网站公告")
    private String websiteNotice = "";

    /**
     * 网站备案号
     */
    @ApiModelProperty("网站备案号")
    private String websiteRecordNo = "";

    /**
     * 社交登录列表
     */
    @ApiModelProperty("社交登录列表")
    private List<String> socialLoginList = new ArrayList<>();
}
