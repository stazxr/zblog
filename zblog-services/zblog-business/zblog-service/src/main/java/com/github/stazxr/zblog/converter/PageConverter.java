package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.PageDto;
import com.github.stazxr.zblog.domain.entity.Page;
import com.github.stazxr.zblog.domain.vo.PageVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * PageConverter
 *
 * @author SunTao
 * @since 2022-12-12
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PageConverter extends BaseConverter<Page, PageDto, PageVo> {

}
