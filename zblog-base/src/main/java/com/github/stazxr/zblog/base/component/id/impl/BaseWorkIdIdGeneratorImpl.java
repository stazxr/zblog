package com.github.stazxr.zblog.base.component.id.impl;

import com.github.stazxr.zblog.base.component.id.model.WorkId;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于机器ID的全局唯一ID生成器基本实现
 *
 *  产生long类型的唯一id，基于Twitter的snow flake算法实现，单台机器每毫秒支持2^12=4096个id
 *  第1位为0，符号位
 *  第2-42位表示毫秒数，共41位，当前时间毫秒-START_TIME_MILLIS
 *  第43-52位表示workId，即机器id，共10位，能支持1024台机器（机器位给1位，生成的ID是16位的，非多节点部署可用）
 *  第53-64位表示序列号，共12位
 *
 *  最大峰值型 vs 最小粒度型
 *    通过调短时间戳长度和调长序列号长度，可以解决峰值压力大的情况。
 *    最大峰值型能够承受更大的峰值压力，但是粗略有序的粒度有点大；最小粒度型有较细致的粒度，但是没毫秒能承受的峰值较小。
 *
 * @author https://blog.csdn.net/u010266988/article/details/87899533
 * @since 2021-12-19
 */
public abstract class BaseWorkIdIdGeneratorImpl extends BaseWorkIdIdGenerator {
    /**
     * 开始时间
     */
    public static final long START_TIME_MILLIS;

    private static final long SEQUENCE_BITS = 12L;

    public static final long WORKER_ID_BITS = 10L;

    /**
     * 2^12 - 1 = 4095 ==> 111 111 111 111
     */
    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;

    /**
     * 2^10 - 1 = 1023 ===> 1 111 111 111
     */
    private static final long WORK_ID_MASK = (1 << WORKER_ID_BITS) - 1;

    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;

    /**
     * 22
     */
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;

    private long sequence;

    /**
     * default 0L
     */
    private long lastTime;

    private final Lock lock = new ReentrantLock();

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1996, Calendar.MARCH, 12);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        START_TIME_MILLIS = calendar.getTimeInMillis();
    }

    /**
     * 生成全局唯一ID
     *
     * @return Long
     */
    @Override
    public Long generateId() {
        long currentTime;
        long seq;

        lock.lock();
        try {
            currentTime = System.currentTimeMillis();
            if (currentTime < lastTime) {
                // 时钟被回拨，直接拒绝服务
                throw new IllegalStateException("Clock go back, refused generator guid service.");
            }

            if (currentTime == lastTime) {
                // 如果1ms内单台机器的4096个序号用完了，等待下一毫秒
                if (0L == (sequence = ++sequence & SEQUENCE_MASK)) {
                    lastTime = waitUntilNextMillis(currentTime);
                }
            } else {
                lastTime = currentTime;
                sequence = 0;
            }

            currentTime = lastTime;
            seq = sequence;
        } finally {
            lock.unlock();
        }

        return (
                (currentTime - START_TIME_MILLIS) << TIMESTAMP_LEFT_SHIFT_BITS
        ) | (getWorkId() << WORKER_ID_LEFT_SHIFT_BITS) | seq;
    }

    @Override
    public WorkId parseId(Long id) {
        WorkId workIdInfo = new WorkId();

        // 时间戳
        long generateTime = (id >> TIMESTAMP_LEFT_SHIFT_BITS) + START_TIME_MILLIS;
        workIdInfo.setLockTime(new Timestamp(generateTime));

        // 机器号
        Long workId = (id >> SEQUENCE_BITS) & WORK_ID_MASK;
        workIdInfo.setWorkId(workId);

        // 机器ip
        workIdInfo.setWorkIpAddr(parseWorkerIp(workId));

        // 序列号
        workIdInfo.setSequence(id & SEQUENCE_MASK);

        return workIdInfo;
    }

    private long waitUntilNextMillis(long fromMills) {
        long nextMills = System.currentTimeMillis();
        while (nextMills <= fromMills) {
            nextMills = System.currentTimeMillis();
        }
        return nextMills;
    }
}
