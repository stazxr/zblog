package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.CommentDto;
import com.github.stazxr.zblog.domain.entity.Comment;
import com.github.stazxr.zblog.domain.vo.CommentVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * CommentConverter
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentConverter extends BaseConverter<Comment, CommentDto, CommentVo> {

}
