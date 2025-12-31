package com.github.stazxr.zblog.base.service.impl;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.github.stazxr.zblog.base.domain.bo.HostData;
import com.github.stazxr.zblog.base.service.HostMonitorService;
import com.github.stazxr.zblog.util.io.FileUtils;
import com.github.stazxr.zblog.util.net.LocalHostUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 主机监控业务实现
 *
 * @author SunTao
 * @since 2021-09-05
 */
@Slf4j
@Service
public class HostMonitorServiceImpl implements HostMonitorService {
    /**
     * 百分比格式
     */
    private static final ThreadLocal<DecimalFormat> RATE_FORMAT = ThreadLocal.withInitial(() -> new DecimalFormat("0.00"));

    /**
     * 查询主机信息
     *
     * @return ServerData
     */
    @Override
    public HostData queryHostData() {
        HostData hostData = new HostData();

        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem os = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hal = systemInfo.getHardware();

        hostData.setSys(buildSysInfo(os));
        hostData.setCpu(buildCpuInfo(hal.getProcessor()));
        hostData.setMemory(buildMemoryInfo(hal.getMemory()));
        hostData.setSwap(buildSwapInfo(hal.getMemory()));
        hostData.setJvm(buildJvmInfo(Runtime.getRuntime()));
        hostData.setSysFiles(buildSysFiles(os.getFileSystem()));
        hostData.setTime(DateUtils.formatNow("HH:mm:ss"));

        return hostData;
    }

    private HostData.Sys buildSysInfo(OperatingSystem os) {
        HostData.Sys sys = new HostData.Sys();
        sys.setOs(os.toString());

        long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        sys.setRuntime(DateUtil.formatBetween(new Date(startTime), new Date(), BetweenFormatter.Level.HOUR));

        try {
            sys.setServerIp(LocalHostUtils.getLocalIp());
        } catch (Exception e) {
            log.error("获取服务器 IP 失败", e);
            sys.setServerIp("未知");
        }
        return sys;
    }

    private HostData.Cpu buildCpuInfo(CentralProcessor processor) {
        HostData.Cpu cpu = new HostData.Cpu();
        cpu.setName(processor.getName());
        cpu.setPackageNum(processor.getPhysicalPackageCount());
        cpu.setCoreNum(processor.getPhysicalProcessorCount());
        cpu.setLogicNum(processor.getLogicalProcessorCount());

        double cpuLoad = Math.max(processor.getSystemCpuLoadBetweenTicks(), 0d);

        double used = cpuLoad * 100;
        double free = 100d - used;

        cpu.setUsed(RATE_FORMAT.get().format(used));
        cpu.setFree(RATE_FORMAT.get().format(free));
        return cpu;
    }

    private HostData.Memory buildMemoryInfo(GlobalMemory memory) {
        HostData.Memory mem = new HostData.Memory();
        long total = memory.getTotal();
        long available = memory.getAvailable();
        long used = total - available;

        mem.setTotal(FormatUtil.formatBytes(total));
        mem.setUsed(FormatUtil.formatBytes(used));
        mem.setAvailable(FormatUtil.formatBytes(available));
        mem.setUsageRate(percent(used, total));
        return mem;
    }

    private HostData.Swap buildSwapInfo(GlobalMemory memory) {
        HostData.Swap swap = new HostData.Swap();
        long total = memory.getSwapTotal();
        long used = memory.getSwapUsed();

        swap.setTotal(FormatUtil.formatBytes(total));
        swap.setUsed(FormatUtil.formatBytes(used));
        swap.setAvailable(FormatUtil.formatBytes(total - used));
        swap.setUsageRate(percent(used, total));
        return swap;
    }

    private HostData.Jvm buildJvmInfo(Runtime runtime) {
        HostData.Jvm jvm = new HostData.Jvm();
        long total = runtime.totalMemory();
        long free = runtime.freeMemory();
        long used = total - free;

        jvm.setMax(FormatUtil.formatBytes(runtime.maxMemory()));
        jvm.setTotal(FormatUtil.formatBytes(total));
        jvm.setAvailable(FormatUtil.formatBytes(free));
        jvm.setUsed(FormatUtil.formatBytes(used));
        jvm.setUsageRate(percent(used, total));
        return jvm;
    }

    private List<HostData.SysFile> buildSysFiles(FileSystem fileSystem) {
        List<HostData.SysFile> list = new ArrayList<>();

        for (OSFileStore fs : fileSystem.getFileStores()) {
            long total = fs.getTotalSpace();
            long free = fs.getUsableSpace();
            long used = total - free;
            if (total <= 0 || fs.getMount().startsWith("/snap")) {
                // 文件系统过滤（Linux），避免显示无意义挂载点
                continue;
            }

            HostData.SysFile file = new HostData.SysFile();
            file.setDirName(fs.getMount());
            file.setSysTypeName(fs.getType());
            file.setTypeName(fs.getName());
            file.setTotal(FileUtils.convertFileSize(total));
            file.setFree(FileUtils.convertFileSize(free));
            file.setUsed(FileUtils.convertFileSize(used));
            file.setUsage(percent(used, total));

            list.add(file);
        }

        list.sort(Comparator.comparing(HostData.SysFile::getDirName));
        return list;
    }

    private String percent(long used, long total) {
        if (total <= 0) {
            return "0.00";
        }
        return RATE_FORMAT.get().format(used * 100d / total);
    }
}
