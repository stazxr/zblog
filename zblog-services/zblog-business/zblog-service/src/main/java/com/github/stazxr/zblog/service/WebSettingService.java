package com.github.stazxr.zblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.domain.dto.setting.OtherInfo;
import com.github.stazxr.zblog.domain.dto.setting.SocialInfo;
import com.github.stazxr.zblog.domain.dto.setting.WebInfo;
import com.github.stazxr.zblog.domain.entity.WebsiteConfig;

/**
 * 网站设置业务层
 *
 * @author SunTao
 * @since 2022-12-08
 */
public interface WebSettingService extends IService<WebsiteConfig> {
    /**
     * 查询网站配置信息
     *
     * @return WebInfo
     */
    WebInfo queryWebInfo();

    /**
     * 修改网站配置信息
     *
     * @param webInfo 网站信息
     */
    void updateWebInfo(WebInfo webInfo);

    /**
     * 查询网站社交信息
     *
     * @return SocialInfo
     */
    SocialInfo querySocialInfo();

    /**
     * 修改网站社交信息
     *
     * @param socialInfo 社交信息
     */
    void updateSocialInfo(SocialInfo socialInfo);

    /**
     * 查询网站其他信息
     *
     * @return OtherInfo
     */
    OtherInfo queryOtherInfo();

    /**
     * 修改网站其他信息
     *
     * @param otherInfo 其他信息
     */
    void updateOtherInfo(OtherInfo otherInfo);
}
