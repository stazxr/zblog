package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.entity.TalkLike;
import org.apache.ibatis.annotations.Param;

/**
 * 说说点赞数据持久层
 *
 * @author SunTao
 * @since 2023-02-07
 */
public interface TalkLikeMapper extends BaseMapper<TalkLike> {
    /**
     * 判断用户是否点过赞
     *
     * @param userId 用户ID
     * @param talkId 说说ID
     * @return boolean
     */
    boolean isLiked(@Param("userId") Long userId, @Param("talkId") Long talkId);

    /**
     * 删除点赞记录
     *
     * @param userId 用户ID
     * @param talkId 说说ID
     */
    void deleteLike(@Param("userId") Long userId, @Param("talkId") Long talkId);
}
