package com.github.stazxr.zblog.content.ext.service;

import com.github.stazxr.zblog.content.ext.domain.dto.WebsiteConfigDto;
import com.github.stazxr.zblog.content.ext.domain.vo.WebsiteConfigVo;

/**
 * 网站配置业务层
 *
 * @author SunTao
 * @since 2026-08-20
 */
public interface WebsiteConfigService {
    /**
     * 查询网站配置详情
     *
     * @return WebsiteConfigVo
     */
    WebsiteConfigVo queryWebsiteConfigDetail();

    /**
     * 编辑网站配置
     *
     * @param websiteConfigDto 网站配置信息
     */
    void editWebsiteConfig(WebsiteConfigDto websiteConfigDto);
}
