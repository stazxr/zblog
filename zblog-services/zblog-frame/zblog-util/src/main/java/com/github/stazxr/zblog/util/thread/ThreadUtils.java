package com.github.stazxr.zblog.util.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread工具类
 *
 * @author SunTao
 * @since 2022-02-05
 */
public class ThreadUtils {
    private static final Logger log = LoggerFactory.getLogger(ThreadUtils.class);

    /**
     * 休眠*分钟
     *
     * @param minutes 分钟数
     */
    public static void sleepMinute(int minutes) {
        sleepSecond(60 * minutes);
    }

    public static void sleepSecond(int seconds) {
        sleepMillisecond(1000L * seconds);
    }

    public static void sleepMillisecond(long milliseconds) {
        try {
            milliseconds = milliseconds < 0 ? 0 : milliseconds;
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            log.error("thread sleep catch interrupted eor", e);
        }
    }

    /**
     * 运行一个线程
     *
     * @param task 执行内容
     * @return Thread
     */
    @SuppressWarnings("all")
    public static Thread runThread(Task task) {
        return runThread("", task);
    }

    /**
     * 运行一个线程
     *
     * @param task 执行内容
     * @return Thread
     */
    @SuppressWarnings("all")
    public static Thread runThread(String name, Task task) {
        Thread thread = new Thread(() -> task.execute());
        if (task != null && !"".equals(task)) {
            thread.setName(name);
        }

        // 启动线程
        thread.start();
        return thread;
    }

    public interface Task {
        /**
         * 调用方法
         */
        void execute();
    }
}
