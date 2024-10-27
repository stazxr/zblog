package com.github.stazxr.zblog.base.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 首页面板数量统计（访问量，评论数等）
 *
 * @author SunTao
 * @since 2022-07-19
 */
@Getter
@Setter
public class HomePanelDataCountVo {
    /**
     * 浏览量，当用户打开一个网页并加载完成时，会被计为一个页面浏览量
     */
    private int pv = 0;

    /**
     * 立访客，访问网站的唯一用户数量，每个用户只被计为一个独立访客。
     */
    private int uv = 0;

    /**
     * 用户数，注册的系统用户数
     */
    private int uu = 0;

    /**
     * 阅读量，文章访问量
     */
    private int av = 0;
}
