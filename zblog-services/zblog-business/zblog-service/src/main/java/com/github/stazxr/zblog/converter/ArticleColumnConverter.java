package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.ArticleColumnDto;
import com.github.stazxr.zblog.domain.entity.ArticleColumn;
import com.github.stazxr.zblog.domain.vo.ArticleColumnVo;
import org.springframework.stereotype.Component;

/**
 * ArticleColumnConverter
 *
 * @author SunTao
 * @since 2022-11-25
 */
@Component
public class ArticleColumnConverter implements BaseConverter<ArticleColumn, ArticleColumnDto, ArticleColumnVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<ArticleColumn> getEntityClass() {
        return ArticleColumn.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<ArticleColumnVo> getVoClass() {
        return ArticleColumnVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<ArticleColumnDto> getDtoClass() {
        return ArticleColumnDto.class;
    }
}
