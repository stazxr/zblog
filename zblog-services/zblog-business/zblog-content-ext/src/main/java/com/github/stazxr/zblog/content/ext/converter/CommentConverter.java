package com.github.stazxr.zblog.content.ext.converter;

import com.github.stazxr.zblog.content.ext.domain.entity.Comment;
import com.github.stazxr.zblog.content.ext.domain.vo.CommentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * CommentConverter
 *
 * @author SunTao
 * @since 2026-09-09
 */
@Component
public class CommentConverter {
    /**
     * 实体对象转视图对象
     *
     * @param po  评论配置实体对象
     * @return vo 评论配置视图对象
     */
    public CommentVo entityToVo(Comment po) {
        if (po == null) {
            return null;
        }

        CommentVo vo = new CommentVo();
        BeanUtils.copyProperties(po, vo);
        return vo;
    }
}
