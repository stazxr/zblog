package com.github.stazxr.zblog.base.converter;

import com.github.stazxr.zblog.base.domain.dto.DictDto;
import com.github.stazxr.zblog.base.domain.entity.Dict;
import com.github.stazxr.zblog.base.domain.vo.DictVo;
import com.github.stazxr.zblog.core.base.BaseConverter;
import org.springframework.stereotype.Component;

/**
 * DictConverter
 *
 * @author SunTao
 * @since 2022-09-21
 */
@Component
public class DictConverter implements BaseConverter<Dict, DictDto, DictVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<Dict> getEntityClass() {
        return Dict.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<DictVo> getVoClass() {
        return DictVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<DictDto> getDtoClass() {
        return DictDto.class;
    }
}
