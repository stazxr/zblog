package com.github.stazxr.zblog.content.ext.converter;

import com.github.stazxr.zblog.content.ext.domain.dto.PageDto;
import com.github.stazxr.zblog.content.ext.domain.entity.Page;
import com.github.stazxr.zblog.content.ext.domain.vo.PageVo;
import com.github.stazxr.zblog.core.base.BaseConverter;
import org.springframework.stereotype.Component;

/**
 * PageConverter
 *
 * @author SunTao
 * @since 2026-06-12
 */
@Component
public class PageConverter implements BaseConverter<Page, PageDto, PageVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<Page> getEntityClass() {
        return Page.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<PageVo> getVoClass() {
        return PageVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<PageDto> getDtoClass() {
        return PageDto.class;
    }
}
