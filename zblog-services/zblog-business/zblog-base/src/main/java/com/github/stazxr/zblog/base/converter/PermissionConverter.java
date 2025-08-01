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
    /**
     * 数据对象转实体对象
     *
     * @param dto 数据对象
     * @return po 实体对象
     */
    @Override
    Permission dtoToEntity(PermissionDto dto);

    /**
     * 实体对象转视图对象
     *
     * @param po 实体对象
     * @return vo 视图对象
     */
    @Override
    PermissionVo entityToVo(Permission po);
}
