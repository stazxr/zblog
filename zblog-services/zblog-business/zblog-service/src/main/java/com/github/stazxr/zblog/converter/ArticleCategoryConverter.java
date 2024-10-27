package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.ArticleCategoryDto;
import com.github.stazxr.zblog.domain.entity.ArticleCategory;
import com.github.stazxr.zblog.domain.vo.ArticleCategoryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * ArticleCategoryConverter
 *
 * @author SunTao
 * @since 2022-09-21
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleCategoryConverter extends BaseConverter<ArticleCategory, ArticleCategoryDto, ArticleCategoryVo> {

}
