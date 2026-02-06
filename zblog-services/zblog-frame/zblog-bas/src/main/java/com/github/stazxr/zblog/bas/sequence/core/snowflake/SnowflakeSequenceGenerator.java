package com.github.stazxr.zblog.bas.sequence.core.snowflake;

import com.github.stazxr.zblog.bas.sequence.SequenceErrorCode;
import com.github.stazxr.zblog.bas.sequence.SequenceException;
import com.github.stazxr.zblog.bas.sequence.core.BaseWorkSequenceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  基于 Twitter 的 snowflake 算法
 *    0 - 0000000000 0000000000 0000000000 0000000000 0 - 000 - 0000000 - 000000000000
 *
 *  1: 1位符号位，由于 long 基本类型在 Java 中是带符号的，最高位是符号位，正数是 0，负数是 1，由于 id 一般是正数，最高位是 0
 *  2-42: 41位时间戳，毫秒数（当前时间戳 - 开始时间戳），41 位的时间戳可以使用 69 年，公式位 (1L<<41)/(1000L*60*60*24*365)
 *  43-52: 10位机器位，包括 5 位 datacenterId 和 5 位 machineId（也可以是其他组合）
 *  53-64: 12位序列号
 *
 *  加起来刚好是 64 位，是一个 Java Long 型数字
 *
 *  SnowFlake 的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生 ID 碰撞(由数据中心 ID 和机器 ID 作区分)，
 *  并且效率较高，经测试，SnowFlake 每秒能够产生 26 万 ID 左右。
 *
 *  最大峰值型 vs 最小粒度型
 *    通过调短时间戳长度和调长序列号长度，可以解决峰值压力大的情况。
 *    最大峰值型能够承受更大的峰值压力，但是粗略有序的粒度有点大；最小粒度型有较细致的粒度，但是没毫秒能承受的峰值较小。
 *
 * @author SunTao
 * @since 2021-12-19
 */
public class SnowflakeSequenceGenerator extends BaseWorkSequenceGenerator {
    private static final Logger log = LoggerFactory.getLogger(SnowflakeSequenceGenerator.class);

    /**
     * 开始时间戳
     */
    private static final long START_TIME_MILLIS;

    /**
     * 数据中心所占的位数，最多支持 8 个数据中心
     */
    public static final long DATACENTER_ID_BITS = 3L;

    /**
     * 数据中心位掩码
     */
    private static final long DATACENTER_MASK = (1 << DATACENTER_ID_BITS) - 1;

    /**
     * 机器所占的位数，每个数据中心最多支持 128 个机器
     */
    public static final long MACHINE_ID_BITS = 7L;

    /**
     * 机器位掩码
     */
    private static final long MACHINE_MASK = (1 << MACHINE_ID_BITS) - 1;

    /**
     * 工作节点标识位，由数据中心位和机器位组成
     */
    private static final long WORKER_ID_BITS = DATACENTER_ID_BITS + MACHINE_ID_BITS;

    /**
     * 序列所占的位数
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 机器偏移位
     */
    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 数据中心偏移位
     */
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;

    /**
     * 时间戳偏移位
     */
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 序列位掩码：(2 的 SEQUENCE_BITS 次方 - 1) = 4095
     */
    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;

    /**
     * 单位毫秒内的序列 [0, SEQUENCE_MASK]
     */
    private long sequence;

    /**
     * 上次生成的时间戳
     */
    private long lastTime;

    /**
     * Lock when get nextId
     */
    private final Lock lock = new ReentrantLock();

    /**
     * 数据中心ID
     */
    private final long datacenterId;

    /**
     * 机器ID
     */
    private final long machineId;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1995, Calendar.APRIL, 10);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        START_TIME_MILLIS = calendar.getTimeInMillis();
    }

    public SnowflakeSequenceGenerator(long datacenterId, long machineId) {
        if (datacenterId > DATACENTER_MASK || datacenterId < 0) {
            String msg = String.format("worker datacenterId can't be greater than %d or less than 0", DATACENTER_MASK);
            throw new IllegalArgumentException(msg);
        }

        if (machineId > MACHINE_MASK || machineId < 0) {
            String msg = String.format("worker machineId can't be greater than %d or less than 0", MACHINE_MASK);
            throw new IllegalArgumentException(msg);
        }

        this.datacenterId = datacenterId;
        this.machineId = machineId;
        log.info("Create snowflake sequence generator: datacenterId={}, machineId={}", this.datacenterId, this.machineId);
    }

    /**
     * 基于雪花算法生成唯一序号
     *
     * @return 唯一序号
     */
    @Override
    public String nextId() {
        long currentTime, seq;
        lock.lock();
        try {
            currentTime = timeMillsGen();
            if (currentTime < lastTime) {
                // 时钟被回拨，直接拒绝服务
                throw new SequenceException(SequenceErrorCode.SSEQGA000);
            }

            if (currentTime == lastTime) {
                if (0L == (sequence = ++sequence & SEQUENCE_MASK)) {
                    lastTime = waitUntilNextMillis(currentTime);
                }
            } else {
                lastTime = currentTime;
                sequence = 0L;
            }

            currentTime = lastTime;
            seq = sequence;
        } finally {
            lock.unlock();
        }

        return String.valueOf(((currentTime - START_TIME_MILLIS) << TIMESTAMP_LEFT_SHIFT_BITS)
                | (getDatacenterId() << DATACENTER_ID_SHIFT)
                | (getMachineId() << MACHINE_ID_SHIFT) | seq);
    }

    private long waitUntilNextMillis(long fromMills) {
        long nextMills = timeMillsGen();
        while (nextMills <= fromMills) {
            nextMills = timeMillsGen();
        }
        return nextMills;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间的毫秒数
     */
    protected long timeMillsGen() {
        return System.currentTimeMillis();
    }

    /**
     * 获取数据中心ID
     *
     * @return Long datacenterId
     */
    @Override
    protected Long getDatacenterId() {
        return datacenterId;
    }

    /**
     * 获取机器ID
     *
     * @return Long machineId
     */
    @Override
    protected Long getMachineId() {
        return machineId;
    }
}
