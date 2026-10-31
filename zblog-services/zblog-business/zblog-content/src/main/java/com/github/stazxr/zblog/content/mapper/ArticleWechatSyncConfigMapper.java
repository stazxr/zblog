package com.github.stazxr.zblog.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.content.domain.entity.ArticleWechatSyncConfig;
import com.github.stazxr.zblog.content.domain.vo.ArticleWechatSyncConfigVo;
import org.apache.ibatis.annotations.Param;

/**
 * 文章微信公众号同步配置数据层
 *
 * @author SunTao
 * @since 2026-11-01
 */
public interface ArticleWechatSyncConfigMapper extends BaseMapper<ArticleWechatSyncConfig> {
    /**
     * 查询文章微信公众号同步配置信息
     *
     * @param articleId 文章id
     * @return ArticleWechatSyncConfigVo
     */
    ArticleWechatSyncConfigVo selectByArticleId(@Param("articleId") Long articleId);
}
