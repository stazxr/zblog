package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.PageDto;
import com.github.stazxr.zblog.domain.entity.Page;
import com.github.stazxr.zblog.domain.vo.PageVo;
import org.springframework.stereotype.Component;

/**
 * PageConverter
 *
 * @author SunTao
 * @since 2022-12-12
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
