package com.github.stazxr.zblog.base.converter;

import com.github.stazxr.zblog.base.domain.dto.PermissionDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.vo.PermissionVo;
import com.github.stazxr.zblog.core.base.BaseConverter;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * PermissionConverter
 *
 * @author SunTao
 * @since 2022-06-29
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionConverter extends BaseConverter<Permission, PermissionDto, PermissionVo> {

}
