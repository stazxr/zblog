package com.github.stazxr.zblog.content.ext.converter;

import com.github.stazxr.zblog.content.ext.domain.dto.WebsiteConfigDto;
import com.github.stazxr.zblog.content.ext.domain.entity.WebsiteConfig;
import com.github.stazxr.zblog.content.ext.domain.vo.WebsiteConfigVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * WebsiteConfigConverter
 *
 * @author SunTao
 * @since 2026-08-20
 */
@Component
public class WebsiteConfigConverter {
    /**
     * 数据对象转实体对象
     *
     * @param dto 网站配置数据对象
     * @return po 网站配置实体对象
     */
    public WebsiteConfig dtoToEntity(WebsiteConfigDto dto) {
        if (dto == null) {
            return null;
        }

        WebsiteConfig po = new WebsiteConfig();
        BeanUtils.copyProperties(dto, po);
        return po;
    }

    /**
     * 实体对象转视图对象
     *
     * @param po  网站配置实体对象
     * @return vo 网站配置视图对象
     */
    public WebsiteConfigVo entityToVo(WebsiteConfig po) {
        if (po == null) {
            return null;
        }

        WebsiteConfigVo vo = new WebsiteConfigVo();
        BeanUtils.copyProperties(po, vo);
        return vo;
    }
}
