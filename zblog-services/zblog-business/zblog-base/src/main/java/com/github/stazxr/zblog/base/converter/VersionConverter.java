package com.github.stazxr.zblog.base.converter;

import com.github.stazxr.zblog.base.domain.dto.VersionDto;
import com.github.stazxr.zblog.base.domain.entity.Version;
import com.github.stazxr.zblog.base.domain.vo.VersionVo;
import com.github.stazxr.zblog.core.base.BaseConverter;
import org.springframework.stereotype.Component;

/**
 * VersionConverter
 *
 * @author SunTao
 * @since 2025-12-31
 */
@Component
public class VersionConverter implements BaseConverter<Version, VersionDto, VersionVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<Version> getEntityClass() {
        return Version.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<VersionVo> getVoClass() {
        return VersionVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<VersionDto> getDtoClass() {
        return VersionDto.class;
    }
}
