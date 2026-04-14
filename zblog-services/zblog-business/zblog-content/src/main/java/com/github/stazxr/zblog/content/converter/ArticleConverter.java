package com.github.stazxr.zblog.content.converter;

import com.github.stazxr.zblog.content.domain.dto.ArticleDto;
import com.github.stazxr.zblog.content.domain.entity.Article;
import com.github.stazxr.zblog.content.domain.vo.ArticleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * ArticleConverter
 *
 * @author SunTao
 * @since 2026-04-08
 */
@Component
public class ArticleConverter {
    /**
     * 数据对象转实体对象
     *
     * @param dto 文章数据对象
     * @return po 文章实体对象
     */
    public Article dtoToEntity(ArticleDto dto) {
        if (dto == null) {
            return null;
        }

        Article po = new Article();
        BeanUtils.copyProperties(dto, po);
        return po;
    }

    /**
     * 实体对象转视图对象
     *
     * @param po  文章实体对象
     * @return vo 文章视图对象
     */
    public ArticleVo entityToVo(Article po) {
        if (po == null) {
            return null;
        }

        ArticleVo vo = new ArticleVo();
        BeanUtils.copyProperties(po, vo);
        return vo;
    }
}
