package com.github.stazxr.zblog.base.converter;

import com.github.stazxr.zblog.base.domain.dto.UserDto;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * UserConverter
 *
 * @author SunTao
 * @since 2025-10-15
 */
@Component
public class UserConverter {
    /**
     * 数据对象转实体对象
     *
     * @param dto 用户数据对象
     * @return po 用户实体对象
     */
    public User dtoToEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User po = new User();
        BeanUtils.copyProperties(dto, po);
        return po;
    }

    /**
     * 实体对象转视图对象
     *
     * @param po  用户实体对象
     * @return vo 用户视图对象
     */
    public UserVo entityToVo(User po) {
        if (po == null) {
            return null;
        }

        UserVo vo = new UserVo();
        BeanUtils.copyProperties(po, vo);
        return vo;
    }
}
