package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.ArticleTagDto;
import com.github.stazxr.zblog.domain.entity.ArticleTag;
import com.github.stazxr.zblog.domain.vo.ArticleTagVo;
import org.springframework.stereotype.Component;

/**
 * ArticleTagConverter
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Component
public class ArticleTagConverter implements BaseConverter<ArticleTag, ArticleTagDto, ArticleTagVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<ArticleTag> getEntityClass() {
        return ArticleTag.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<ArticleTagVo> getVoClass() {
        return ArticleTagVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<ArticleTagDto> getDtoClass() {
        return ArticleTagDto.class;
    }
}
