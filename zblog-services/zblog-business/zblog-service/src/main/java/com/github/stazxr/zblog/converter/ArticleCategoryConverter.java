package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.ArticleCategoryDto;
import com.github.stazxr.zblog.domain.entity.ArticleCategory;
import com.github.stazxr.zblog.domain.vo.ArticleCategoryVo;
import org.springframework.stereotype.Component;

/**
 * ArticleCategoryConverter
 *
 * @author SunTao
 * @since 2022-09-21
 */
@Component
public class ArticleCategoryConverter implements BaseConverter<ArticleCategory, ArticleCategoryDto, ArticleCategoryVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<ArticleCategory> getEntityClass() {
        return ArticleCategory.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<ArticleCategoryVo> getVoClass() {
        return ArticleCategoryVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<ArticleCategoryDto> getDtoClass() {
        return ArticleCategoryDto.class;
    }
}
