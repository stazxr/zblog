package com.github.stazxr.zblog.domain.dto.setting;

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
public class WebInfo {
    /**
     * 网站头像
     */
    private String websiteAvatar = "";

    /**
     * 网站名称
     */
    private String websiteName = "";

    /**
     * 网站作者
     */
    private String websiteAuthor = "";

    /**
     * 网站创建时间
     */
    private String websiteCreateTime = "";

    /**
     * 网站介绍
     */
    private String websiteIntro = "";

    /**
     * 网站链接（前台）
     */
    private String websiteLink = "";

    /**
     * 网站链接（后台）
     */
    private String websiteAdminLink = "";

    /**
     * 网站公告
     */
    private String websiteNotice = "";

    /**
     * 网站备案号
     */
    private String websiteRecordNo = "";

    /**
     * 社交登录列表
     */
    private List<String> socialLoginList = new ArrayList<>();
}
