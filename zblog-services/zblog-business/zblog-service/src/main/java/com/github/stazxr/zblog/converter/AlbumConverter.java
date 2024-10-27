package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.AlbumDto;
import com.github.stazxr.zblog.domain.entity.Album;
import com.github.stazxr.zblog.domain.vo.AlbumVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * AlbumConverter
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlbumConverter extends BaseConverter<Album, AlbumDto, AlbumVo> {

}
