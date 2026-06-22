package com.github.stazxr.zblog.content.ext.converter;

import com.github.stazxr.zblog.content.ext.domain.dto.ThemeDto;
import com.github.stazxr.zblog.content.ext.domain.entity.Theme;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemeVo;
import com.github.stazxr.zblog.core.base.BaseConverter;
import org.springframework.stereotype.Component;

/**
 * ThemeConverter
 *
 * @author SunTao
 * @since 2026-06-14
 */
@Component
public class ThemeConverter implements BaseConverter<Theme, ThemeDto, ThemeVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<Theme> getEntityClass() {
        return Theme.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<ThemeVo> getVoClass() {
        return ThemeVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<ThemeDto> getDtoClass() {
        return ThemeDto.class;
    }
}
