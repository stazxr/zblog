package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.TalkDto;
import com.github.stazxr.zblog.domain.entity.Talk;
import com.github.stazxr.zblog.domain.vo.TalkVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * TalkConverter
 *
 * @author SunTao
 * @since 2022-12-12
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TalkConverter extends BaseConverter<Talk, TalkDto, TalkVo> {

}
