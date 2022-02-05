package com.github.stazxr.zblog.util.time;

import com.github.stazxr.zblog.util.Assert;
import lombok.extern.slf4j.Slf4j;

/**
 * Thread工具类
 *
 * @author SunTao
 * @since 2022-02-05
 */
@Slf4j
public class ThreadUtils {
    /**
     * 休眠*分钟
     *
     * @param minutes 分钟数
     */
    public static void sleepMinute(int minutes) {
        sleepSecond(60 * minutes);
    }

    public static void sleepSecond(int seconds) {
        sleepMillisecond(1000 * seconds);
    }

    public static void sleepMillisecond(int milliseconds) {
        Assert.isTrue(milliseconds > 0, "milliseconds must be greater than zero.");
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            log.error("thread sleep catch eor", e);
        }
    }
}
