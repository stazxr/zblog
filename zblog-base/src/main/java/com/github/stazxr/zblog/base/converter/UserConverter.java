package com.github.stazxr.zblog.base.converter;

import com.github.stazxr.zblog.base.domain.dto.UserDto;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
import com.github.stazxr.zblog.core.base.BaseConverter;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * UserConverter
 *
 * @author SunTao
 * @since 2022-06-29
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter extends BaseConverter<User, UserDto, UserVo> {

}
