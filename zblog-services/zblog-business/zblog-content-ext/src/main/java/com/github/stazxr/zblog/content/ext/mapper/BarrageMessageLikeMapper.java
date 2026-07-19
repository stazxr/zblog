package com.github.stazxr.zblog.content.ext.mapper;

import com.github.stazxr.zblog.content.ext.domain.entity.BarrageMessageLike;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 弹幕点赞管理数据层
 *
 * @author SunTao
 * @since 2026-07-20
 */
public interface BarrageMessageLikeMapper extends BaseMapper<BarrageMessageLike> {
    /**
     * 根据用户id查找弹幕点赞信息
     *
     * @param barrageMessageId 弹幕id
     * @param userId 用户id
     * @return BarrageMessageLike
     */
    BarrageMessageLike selectOneByUserId(@Param("barrageMessageId") Long barrageMessageId, @Param("userId") Long userId);

    /**
     * 根据用户id查找弹幕点赞信息
     *
     * @param barrageMessageId 弹幕id
     * @param visitorId 访客id
     * @return BarrageMessageLike
     */
    BarrageMessageLike selectOneByVisitorId(@Param("barrageMessageId") Long barrageMessageId, @Param("visitorId") String visitorId);
}
