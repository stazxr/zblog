package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.AlbumDto;
import com.github.stazxr.zblog.domain.entity.Album;
import com.github.stazxr.zblog.domain.vo.AlbumVo;
import org.springframework.stereotype.Component;

/**
 * AlbumConverter
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Component
public class AlbumConverter implements BaseConverter<Album, AlbumDto, AlbumVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<Album> getEntityClass() {
        return Album.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<AlbumVo> getVoClass() {
        return AlbumVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<AlbumDto> getDtoClass() {
        return AlbumDto.class;
    }
}
