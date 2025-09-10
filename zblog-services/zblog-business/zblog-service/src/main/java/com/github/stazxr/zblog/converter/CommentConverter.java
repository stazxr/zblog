package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.CommentDto;
import com.github.stazxr.zblog.domain.entity.Comment;
import com.github.stazxr.zblog.domain.vo.CommentVo;
import org.springframework.stereotype.Component;

/**
 * CommentConverter
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Component
public class CommentConverter implements BaseConverter<Comment, CommentDto, CommentVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<Comment> getEntityClass() {
        return Comment.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<CommentVo> getVoClass() {
        return CommentVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<CommentDto> getDtoClass() {
        return CommentDto.class;
    }
}
