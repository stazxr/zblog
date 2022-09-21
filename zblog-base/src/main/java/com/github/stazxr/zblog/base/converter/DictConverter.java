package com.github.stazxr.zblog.base.converter;

import com.github.stazxr.zblog.base.domain.dto.DictDto;
import com.github.stazxr.zblog.base.domain.entity.Dict;
import com.github.stazxr.zblog.base.domain.vo.DictVo;
import com.github.stazxr.zblog.core.base.BaseConverter;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * DictConverter
 *
 * @author SunTao
 * @since 2022-09-21
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictConverter extends BaseConverter<Dict, DictDto, DictVo> {

}
