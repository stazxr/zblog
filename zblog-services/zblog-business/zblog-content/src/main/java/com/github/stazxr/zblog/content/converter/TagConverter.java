package com.github.stazxr.zblog.content.converter;

import com.github.stazxr.zblog.content.domain.dto.TagDto;
import com.github.stazxr.zblog.content.domain.entity.Tag;
import com.github.stazxr.zblog.content.domain.vo.TagVo;
import com.github.stazxr.zblog.core.base.BaseConverter;
import org.springframework.stereotype.Component;

/**
 * TagConverter
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Component
public class TagConverter implements BaseConverter<Tag, TagDto, TagVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<Tag> getEntityClass() {
        return Tag.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<TagVo> getVoClass() {
        return TagVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<TagDto> getDtoClass() {
        return TagDto.class;
    }
}
