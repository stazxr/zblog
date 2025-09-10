package com.github.stazxr.zblog.base.converter;

import com.github.stazxr.zblog.base.domain.dto.PermissionDto;
import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.vo.PermissionVo;
import com.github.stazxr.zblog.core.base.BaseConverter;
import org.springframework.stereotype.Component;

/**
 * PermissionConverter
 *
 * @author SunTao
 * @since 2022-06-29
 */
@Component
public class PermissionConverter implements BaseConverter<Permission, PermissionDto, PermissionVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<Permission> getEntityClass() {
        return Permission.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<PermissionVo> getVoClass() {
        return PermissionVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<PermissionDto> getDtoClass() {
        return PermissionDto.class;
    }
}
