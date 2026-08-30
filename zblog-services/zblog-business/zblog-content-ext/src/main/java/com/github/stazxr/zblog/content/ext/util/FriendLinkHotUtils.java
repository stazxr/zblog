package com.github.stazxr.zblog.content.ext.util;

/**
 * 友链热力值计算工具类
 *
 * <p>热力值计算规则：</p>
 *
 * <pre>
 *
 * 热力值 =
 * 今日点击 × 10
 * * 本周新增点击 × 5
 * * 本月新增点击 × 2
 * * √历史点击 × 3
 *
 * 其中：
 * 本周新增点击 = 本周点击 - 今日点击
 * 本月新增点击 = 本月点击 - 本周点击
 * 历史点击 = 总点击 - 本月点击
 * </pre>
 *
 * <p>权重优先级：</p>
 *
 * <pre>
 *
 * 今日 > 本周 > 本月 > 历史
 * </pre>
 *
 * @author SunTao
 * @since 2026-08-30
 */
public final class FriendLinkHotUtils {
    /**
     * 今日点击权重
     */
    private static final double DAY_WEIGHT = 10D;

    /**
     * 本周点击权重
     */
    private static final double WEEK_WEIGHT = 5D;

    /**
     * 本月点击权重
     */
    private static final double MONTH_WEIGHT = 2D;

    /**
     * 历史点击权重
     */
    private static final double HISTORY_WEIGHT = 3D;

    /**
     * 私有构造方法
     */
    private FriendLinkHotUtils() {
    }

    /**
     * 计算友链热力值
     *
     * @param clickCount 总点击数
     * @param dayClick   今日点击数
     * @param weekClick  本周点击数
     * @param monthClick 本月点击数
     * @return 热力值
     */
    public static long calculate(Long clickCount, Integer dayClick, Integer weekClick, Integer monthClick) {
        // 空值处理
        long total = clickCount == null ? 0L : Math.max(clickCount, 0L);
        long day = dayClick == null ? 0L : Math.max(dayClick.longValue(), 0L);
        long week = weekClick == null ? 0L : Math.max(weekClick.longValue(), 0L);
        long month = monthClick == null ? 0L : Math.max(monthClick.longValue(), 0L);

        /*
         * 防止统计数据异常，例如：
         *
         * 今日点击 > 本周点击
         * 本周点击 > 本月点击
         * 本月点击 > 总点击
         */
        week = Math.max(week, day);
        month = Math.max(month, week);
        total = Math.max(total, month);

        // 本周除去今日点击
        long weekOther = week - day;

        // 本月除去本周点击
        long monthOther = month - week;

        // 历史点击
        long history = total - month;

        double hotScore = day * DAY_WEIGHT + weekOther * WEEK_WEIGHT + monthOther * MONTH_WEIGHT + Math.sqrt(history) * HISTORY_WEIGHT;
        return Math.round(hotScore);
    }
}
