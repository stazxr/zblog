package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.ArticleTagDto;
import com.github.stazxr.zblog.domain.entity.ArticleTag;
import com.github.stazxr.zblog.domain.vo.ArticleTagVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * ArticleTagConverter
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleTagConverter extends BaseConverter<ArticleTag, ArticleTagDto, ArticleTagVo> {

}
