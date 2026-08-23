package com.github.stazxr.zblog.content.ext.converter;

import com.github.stazxr.zblog.content.ext.domain.dto.WebsiteLinkConfigDto;
import com.github.stazxr.zblog.content.ext.domain.entity.WebsiteLinkConfig;
import com.github.stazxr.zblog.content.ext.domain.vo.WebsiteLinkConfigVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * WebsiteLinkConverter
 *
 * @author SunTao
 * @since 2022-08-23
 */
@Component
public class WebsiteLinkConverter {
    /**
     * 数据对象转实体对象
     *
     * @param dto 网站链接配置数据对象
     * @return po 网站链接配置实体对象
     */
    public WebsiteLinkConfig dtoToEntity(WebsiteLinkConfigDto dto) {
        if (dto == null) {
            return null;
        }

        WebsiteLinkConfig po = new WebsiteLinkConfig();
        BeanUtils.copyProperties(dto, po);
        return po;
    }

    /**
     * 实体对象转视图对象
     *
     * @param po  网站链接配置实体对象
     * @return vo 网站链接配置视图对象
     */
    public WebsiteLinkConfigVo entityToVo(WebsiteLinkConfig po) {
        if (po == null) {
            return null;
        }

        WebsiteLinkConfigVo vo = new WebsiteLinkConfigVo();
        BeanUtils.copyProperties(po, vo);
        return vo;
    }
}
