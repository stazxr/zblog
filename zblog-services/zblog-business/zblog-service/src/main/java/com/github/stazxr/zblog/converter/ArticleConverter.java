package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.ArticleDto;
import com.github.stazxr.zblog.domain.entity.Article;
import com.github.stazxr.zblog.domain.vo.ArticleVo;
import org.springframework.stereotype.Component;

/**
 * ArticleConverter
 *
 * @author SunTao
 * @since 2022-12-03
 */
@Component
public class ArticleConverter implements BaseConverter<Article, ArticleDto, ArticleVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<Article> getEntityClass() {
        return Article.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<ArticleVo> getVoClass() {
        return ArticleVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<ArticleDto> getDtoClass() {
        return ArticleDto.class;
    }
}
