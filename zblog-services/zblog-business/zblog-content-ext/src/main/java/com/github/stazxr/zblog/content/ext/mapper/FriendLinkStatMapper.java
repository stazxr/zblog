package com.github.stazxr.zblog.content.ext.mapper;

import com.github.stazxr.zblog.content.ext.domain.entity.FriendLinkStat;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 友链统计管理数据层
 *
 * @author SunTao
 * @since 2026-08-30
 */
public interface FriendLinkStatMapper extends BaseMapper<FriendLinkStat> {
    /**
     * 清理日统计数
     */
    void resetDayClick();

    /**
     * 清理周统计数
     */
    void resetWeekClick();

    /**
     * 清理月统计数
     */
    void resetMonthClick();

    /**
     * 统计数加一
     */
    void incrementClickCount(@Param("friendLinkId") Long friendLinkId);
}
