package com.github.stazxr.zblog.base.domain.bo;

import lombok.Data;

import java.util.List;

/**
 * 服务器信息
 *
 * @author SunTao
 * @since 2021-09-05
 */
@Data
public class ServerData {
    /**
     * 系统信息
     */
    private Sys sys;

    /**
     * 处理器信息
     */
    private Cpu cpu;

    /**
     * 内存信息
     */
    private Memory memory;

    /**
     * JVM信息
     */
    private Jvm jvm;

    /**
     * 交换区信息
     */
    private Swap swap;

    /**
     * 文件系统
     */
    private List<SysFile> sysFiles;

    /**
     * 数据产生时间
     */
    private String time;

    @Data
    public static class Sys {
        /**
         * 操作系统
         */
        private String os;

        /**
         * 运行时间
         */
        private String runtime;

        /**
         * 服务器IP
         */
        private String serverIp;
    }

    @Data
    public static class Cpu {
        /**
         * 名称
         */
        private String name;

        /**
         * 物理CPU
         */
        private int packageNum;

        /**
         * 物理核
         */
        private int coreNum;

        /**
         * 逻辑CPU
         */
        private int logicNum;

        /**
         * CPU使用率
         */
        private String used;

        /**
         * CPU空闲率
         */
        private String free;
    }

    @Data
    public static class Memory {
        /**
         * 内存总量
         */
        private String total;

        /**
         * 剩余内存
         */
        private String available;

        /**
         * 已用内存
         */
        private String used;

        /**
         * 使用率
         */
        private String usageRate;
    }

    @Data
    public static class Jvm {
        /**
         * 最大内存
         */
        private String max;

        /**
         * 内存总量
         */
        private String total;

        /**
         * 剩余内存
         */
        private String available;

        /**
         * 已用内存
         */
        private String used;

        /**
         * 使用率
         */
        private String usageRate;
    }

    @Data
    public static class Swap {
        /**
         * 内存总量
         */
        private String total;

        /**
         * 剩余内存
         */
        private String available;

        /**
         * 已用内存
         */
        private String used;

        /**
         * 使用率
         */
        private String usageRate;
    }

    @Data
    public static class SysFile {
        /**
         * 盘符路径
         */
        private String dirName;

        /**
         * 盘符类型
         */
        private String sysTypeName;

        /**
         * 文件类型
         */
        private String typeName;

        /**
         * 总大小
         */
        private String total;

        /**
         * 剩余大小
         */
        private String free;

        /**
         * 已经使用量
         */
        private String used;

        /**
         * 资源的使用率
         */
        private String usage;
    }
}
