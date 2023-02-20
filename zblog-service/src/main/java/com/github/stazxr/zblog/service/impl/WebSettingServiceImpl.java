package com.github.stazxr.zblog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.core.util.CacheUtils;
import com.github.stazxr.zblog.domain.dto.setting.OtherInfo;
import com.github.stazxr.zblog.domain.dto.setting.SocialInfo;
import com.github.stazxr.zblog.domain.dto.setting.WebInfo;
import com.github.stazxr.zblog.domain.entity.WebsiteConfig;
import com.github.stazxr.zblog.domain.enums.WebsiteConfigType;
import com.github.stazxr.zblog.mapper.WebSettingMapper;
import com.github.stazxr.zblog.service.WebSettingService;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.YwConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 网站设置业务实现层
 *
 * @author SunTao
 * @since 2022-12-08
 */
@Service
@RequiredArgsConstructor
public class WebSettingServiceImpl extends ServiceImpl<WebSettingMapper, WebsiteConfig> implements WebSettingService {
    /**
     * 查询网站配置信息
     *
     * @return WebInfo
     */
    @Override
    public WebInfo queryWebInfo() {
        Integer dbKey = WebsiteConfigType.WEB_INFO.value();
        WebsiteConfig websiteConfig = baseMapper.selectById(dbKey);
        Assert.notNull(websiteConfig, "网站配置信息不存在，KEY 为: " + dbKey);
        return JSON.parseObject(websiteConfig.getConfig(), WebInfo.class);
    }

    /**
     * 修改网站配置信息
     *
     * @param webInfo 网站信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWebInfo(WebInfo webInfo) {
        Integer dbKey = WebsiteConfigType.WEB_INFO.value();
        WebsiteConfig websiteConfig = baseMapper.selectById(dbKey);
        Assert.notNull(websiteConfig, "网站配置信息不存在，KEY 为: " + dbKey);
        websiteConfig.setConfig(JSON.toJSONString(webInfo));
        baseMapper.updateById(websiteConfig);
        CacheUtils.remove(YwConstants.CacheKey.webInfo.cacheKey());
    }

    /**
     * 查询网站社交信息
     *
     * @return SocialInfo
     */
    @Override
    public SocialInfo querySocialInfo() {
        Integer dbKey = WebsiteConfigType.SOCIAL_INFO.value();
        WebsiteConfig websiteConfig = baseMapper.selectById(dbKey);
        Assert.notNull(websiteConfig, "网站社交信息不存在，KEY 为: " + dbKey);
        return JSON.parseObject(websiteConfig.getConfig(), SocialInfo.class);
    }

    /**
     * 修改网站社交信息
     *
     * @param socialInfo 社交信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSocialInfo(SocialInfo socialInfo) {
        Integer dbKey = WebsiteConfigType.SOCIAL_INFO.value();
        WebsiteConfig websiteConfig = baseMapper.selectById(dbKey);
        Assert.notNull(websiteConfig, "网站社交信息不存在，KEY 为: " + dbKey);
        websiteConfig.setConfig(JSON.toJSONString(socialInfo));
        baseMapper.updateById(websiteConfig);
        CacheUtils.remove(YwConstants.CacheKey.socialInfo.cacheKey());
    }

    /**
     * 查询网站其他信息
     *
     * @return OtherInfo
     */
    @Override
    public OtherInfo queryOtherInfo() {
        Integer dbKey = WebsiteConfigType.OTHER_INFO.value();
        WebsiteConfig websiteConfig = baseMapper.selectById(dbKey);
        Assert.notNull(websiteConfig, "网站其他信息不存在，KEY 为: " + dbKey);
        return JSON.parseObject(websiteConfig.getConfig(), OtherInfo.class);
    }

    /**
     * 修改网站其他信息
     *
     * @param otherInfo 其他信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOtherInfo(OtherInfo otherInfo) {
        Integer dbKey = WebsiteConfigType.OTHER_INFO.value();
        WebsiteConfig websiteConfig = baseMapper.selectById(dbKey);
        Assert.notNull(websiteConfig, "网站其他信息不存在，KEY 为: " + dbKey);
        websiteConfig.setConfig(JSON.toJSONString(otherInfo));
        baseMapper.updateById(websiteConfig);
        CacheUtils.remove(YwConstants.CacheKey.otherInfo.cacheKey());
    }
}
