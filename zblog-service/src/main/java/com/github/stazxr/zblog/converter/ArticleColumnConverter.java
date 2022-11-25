package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.ArticleColumnDto;
import com.github.stazxr.zblog.domain.entity.ArticleColumn;
import com.github.stazxr.zblog.domain.vo.ArticleColumnVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * ArticleColumnConverter
 *
 * @author SunTao
 * @since 2022-11-25
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleColumnConverter extends BaseConverter<ArticleColumn, ArticleColumnDto, ArticleColumnVo> {

}
