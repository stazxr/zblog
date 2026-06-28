package com.github.stazxr.zblog.content.ext.converter;

import com.github.stazxr.zblog.content.ext.domain.dto.ThemePageDto;
import com.github.stazxr.zblog.content.ext.domain.entity.ThemePage;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemePageVo;
import com.github.stazxr.zblog.core.base.BaseConverter;
import org.springframework.stereotype.Component;

/**
 * ThemePageConverter
 *
 * @author SunTao
 * @since 2026-06-27
 */
@Component
public class ThemePageConverter implements BaseConverter<ThemePage, ThemePageDto, ThemePageVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<ThemePage> getEntityClass() {
        return ThemePage.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<ThemePageVo> getVoClass() {
        return ThemePageVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<ThemePageDto> getDtoClass() {
        return ThemePageDto.class;
    }
}
