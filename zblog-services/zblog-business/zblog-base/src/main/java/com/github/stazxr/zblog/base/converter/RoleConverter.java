package com.github.stazxr.zblog.base.converter;

import com.github.stazxr.zblog.base.domain.dto.RoleDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.vo.RoleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * RoleConverter
 *
 * @author SunTao
 * @since 2025-09-13
 */
@Component
public class RoleConverter {
    /**
     * 数据对象转实体对象
     *
     * @param dto 角色数据对象
     * @return po 角色实体对象
     */
    public Role dtoToEntity(RoleDto dto) {
        if (dto == null) {
            return null;
        }

        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        return role;
    }

    /**
     * 实体对象转视图对象
     *
     * @param po  角色实体对象
     * @return vo 角色视图对象
     */
    public RoleVo entityToVo(Role po) {
        if (po == null) {
            return null;
        }

        RoleVo roleVo = new RoleVo();
        BeanUtils.copyProperties(po, roleVo);
        return roleVo;
    }
}
