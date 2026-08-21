package com.github.stazxr.zblog.content.ext.service.impl;

import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.content.ext.converter.WebsiteConfigConverter;
import com.github.stazxr.zblog.content.ext.domain.dto.WebsiteConfigDto;
import com.github.stazxr.zblog.content.ext.domain.entity.WebsiteConfig;
import com.github.stazxr.zblog.content.ext.domain.vo.WebsiteConfigVo;
import com.github.stazxr.zblog.content.ext.mapper.WebsiteConfigMapper;
import com.github.stazxr.zblog.content.ext.service.WebsiteConfigService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 网站配置业务实现层
 *
 * @author SunTao
 * @since 2026-08-20
 */
@Service
@RequiredArgsConstructor
public class WebsiteConfigServiceImpl implements WebsiteConfigService {
    private final WebsiteConfigMapper websiteConfigMapper;

    private final WebsiteConfigConverter websiteConfigConverter;

    /**
     * 查询网站配置详情
     *
     * @return WebsiteConfigVo
     */
    @Override
    public WebsiteConfigVo queryWebsiteConfigDetail() {
        WebsiteConfig websiteConfig = websiteConfigMapper.selectById(1L);
        WebsiteConfigVo websiteConfigVo = websiteConfigConverter.entityToVo(websiteConfig);
        return ThrowUtils.requireNonNull(websiteConfigVo, BaseErrorCode.ECOREA001);
    }

    /**
     * 编辑网站配置
     *
     * @param websiteConfigDto 网站配置信息
     */
    @Override
    public void editWebsiteConfig(WebsiteConfigDto websiteConfigDto) {
        WebsiteConfig websiteConfig = websiteConfigConverter.dtoToEntity(websiteConfigDto);
        int updateRow = websiteConfigMapper.updateById(websiteConfig);
        ThrowUtils.when(updateRow != 1).system(BaseErrorCode.SCOREA002);
    }
}
