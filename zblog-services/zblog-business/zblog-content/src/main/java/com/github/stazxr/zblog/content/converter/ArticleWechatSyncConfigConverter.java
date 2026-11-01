package com.github.stazxr.zblog.content.converter;

import com.github.stazxr.zblog.content.domain.dto.ArticleWechatSyncConfigDto;
import com.github.stazxr.zblog.content.domain.entity.ArticleWechatSyncConfig;
import com.github.stazxr.zblog.content.domain.vo.ArticleWechatSyncConfigVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * ArticleWechatSyncConfigConverter
 *
 * @author SunTao
 * @since 2026-11-01
 */
@Component
public class ArticleWechatSyncConfigConverter {
    /**
     * 数据对象转实体对象
     *
     * @param dto 数据对象
     * @return po 实体对象
     */
    public ArticleWechatSyncConfig dtoToEntity(ArticleWechatSyncConfigDto dto) {
        if (dto == null) {
            return null;
        }

        ArticleWechatSyncConfig po = new ArticleWechatSyncConfig();
        BeanUtils.copyProperties(dto, po);
        return po;
    }

    /**
     * 实体对象转视图对象
     *
     * @param po  实体对象
     * @return vo 视图对象
     */
    public ArticleWechatSyncConfigVo entityToVo(ArticleWechatSyncConfig po) {
        if (po == null) {
            return null;
        }

        ArticleWechatSyncConfigVo vo = new ArticleWechatSyncConfigVo();
        BeanUtils.copyProperties(po, vo);
        return vo;
    }
}
