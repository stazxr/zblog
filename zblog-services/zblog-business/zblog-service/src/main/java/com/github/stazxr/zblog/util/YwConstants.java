package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.base.util.Constants;

/**
 * 业务常量类
 *
 * @author SunTao
 * @since 2020-11-16
 */
public class YwConstants {
    /**
     * 高亮标签
     */
    public static final String PRE_TAG = "<span style='color:#f47466'>";

    /**
     * 高亮标签
     */
    public static final String POST_TAG = "</span>";

    /**
     * Cache Key
     *
     * @author SunTao
     * @since 2020-11-14
     */
    public enum CacheKey {
        /**
         * webInfo
         */
        webInfo("portal:webInfo", Constants.ONE_WEEK_SEC_UNIT),

        /**
         * socialInfo
         */
        socialInfo("portal:socialInfo", Constants.ONE_WEEK_SEC_UNIT),

        /**
         * otherInfo
         */
        otherInfo("portal:otherInfo", Constants.ONE_WEEK_SEC_UNIT),

        /**
         * githubCalendarData，默认缓存 24 个小时
         */
        githubCalendarData("portal:githubCalendar:", Constants.ONE_DAY_SEC_UNIT);

        /**
         * 缓存Key
         */
        private final String cacheKey;

        /**
         * 缓存有效时间，单位秒
         */
        private final int duration;

        CacheKey(String cacheKey, int duration) {
            this.cacheKey = cacheKey;
            this.duration = duration;
        }

        public String cacheKey() {
            return cacheKey;
        }

        public int duration() {
            return duration;
        }
    }
}
