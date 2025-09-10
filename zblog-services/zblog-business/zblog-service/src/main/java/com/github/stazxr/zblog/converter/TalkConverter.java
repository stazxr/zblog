package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.TalkDto;
import com.github.stazxr.zblog.domain.entity.Talk;
import com.github.stazxr.zblog.domain.vo.TalkVo;
import org.springframework.stereotype.Component;

/**
 * TalkConverter
 *
 * @author SunTao
 * @since 2022-12-12
 */
@Component
public class TalkConverter implements BaseConverter<Talk, TalkDto, TalkVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<Talk> getEntityClass() {
        return Talk.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<TalkVo> getVoClass() {
        return TalkVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<TalkDto> getDtoClass() {
        return TalkDto.class;
    }
}
