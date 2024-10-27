package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.ArticleDto;
import com.github.stazxr.zblog.domain.entity.Article;
import com.github.stazxr.zblog.domain.vo.ArticleVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * ArticleConverter
 *
 * @author SunTao
 * @since 2022-12-03
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleConverter extends BaseConverter<Article, ArticleDto, ArticleVo> {

}
