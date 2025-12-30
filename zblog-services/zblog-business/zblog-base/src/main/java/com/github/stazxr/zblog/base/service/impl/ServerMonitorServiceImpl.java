package com.github.stazxr.zblog.base.service.impl;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.github.stazxr.zblog.base.domain.bo.HostData;
import com.github.stazxr.zblog.base.service.ServerMonitorService;
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
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 服务监控业务实现层
 *
 * @author SunTao
 * @since 2021-09-05
 */
@Slf4j
@Service
public class ServerMonitorServiceImpl implements ServerMonitorService {
    /**
     * 查询服务器信息
     *
     * @return ServerInfo
     */
    @Override
    public HostData queryServerData() {
        HostData hostData = new HostData();

        // init SystemInfo
        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        HardwareAbstractionLayer hal = si.getHardware();

        // init serverData
        hostData.setSys(getSysInfo(os));
        hostData.setCpu(getCpuInfo(hal.getProcessor()));
        hostData.setMemory(getMemoryInfo(hal.getMemory()));
        hostData.setJvm(getJvmInfo(Runtime.getRuntime()));
        hostData.setSwap(getSwapInfo(hal.getMemory()));
        hostData.setSysFiles(getSysFiles(os.getFileSystem()));
        hostData.setTime(DateUtils.formatNow("HH:mm:ss"));
        return hostData;
    }

    private List<HostData.SysFile> getSysFiles(FileSystem fileSystem) {
        List<HostData.SysFile> sysFiles = new ArrayList<>();
        OSFileStore[] fsArray = fileSystem.getFileStores();
        DecimalFormat df = new DecimalFormat("0.00");
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            HostData.SysFile sysFile = new HostData.SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(FileUtils.convertFileSize(total));
            sysFile.setFree(FileUtils.convertFileSize(free));
            sysFile.setUsed(FileUtils.convertFileSize(used));
            sysFile.setUsage(df.format(used / (double) total * 100));
            sysFiles.add(sysFile);
        }

        // 排序
        sysFiles.sort(Comparator.comparing(HostData.SysFile::getDirName));
        return sysFiles;
    }

    private HostData.Swap getSwapInfo(GlobalMemory memory) {
        HostData.Swap swap = new HostData.Swap();
        DecimalFormat df = new DecimalFormat("0.00");
        swap.setTotal(FormatUtil.formatBytes(memory.getSwapTotal()));
        swap.setAvailable(FormatUtil.formatBytes(memory.getSwapTotal() - memory.getSwapUsed()));
        swap.setUsed(FormatUtil.formatBytes(memory.getSwapUsed()));
        swap.setUsageRate(df.format(memory.getSwapUsed() / (double) memory.getSwapTotal() * 100));
        return swap;
    }

    private HostData.Jvm getJvmInfo(Runtime runtime) {
        HostData.Jvm jvm = new HostData.Jvm();
        DecimalFormat df = new DecimalFormat("0.00");
        jvm.setMax(FormatUtil.formatBytes(runtime.maxMemory()));
        jvm.setTotal(FormatUtil.formatBytes(runtime.totalMemory()));
        jvm.setAvailable(FormatUtil.formatBytes(runtime.freeMemory()));
        jvm.setUsed(FormatUtil.formatBytes(runtime.totalMemory() - runtime.freeMemory()));
        jvm.setUsageRate(df.format((runtime.totalMemory() - runtime.freeMemory()) / (double) runtime.totalMemory() * 100));
        return jvm;
    }

    private HostData.Memory getMemoryInfo(GlobalMemory memory) {
        HostData.Memory mem = new HostData.Memory();
        DecimalFormat df = new DecimalFormat("0.00");
        mem.setTotal(FormatUtil.formatBytes(memory.getTotal()));
        mem.setAvailable(FormatUtil.formatBytes(memory.getAvailable()));
        mem.setUsed(FormatUtil.formatBytes(memory.getTotal() - memory.getAvailable()));
        mem.setUsageRate(df.format((memory.getTotal() - memory.getAvailable()) / (double) memory.getTotal() * 100));
        return mem;
    }

    private HostData.Cpu getCpuInfo(CentralProcessor processor) {
        HostData.Cpu cpu = new HostData.Cpu();
        cpu.setName(processor.getName());
        cpu.setPackageNum(processor.getPhysicalPackageCount());
        cpu.setCoreNum(processor.getPhysicalProcessorCount());
        cpu.setLogicNum(processor.getLogicalProcessorCount());

        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + sys + idle + ioWait + irq + softIrq + steal;

        DecimalFormat df = new DecimalFormat("0.00");
        cpu.setUsed(df.format(100d * user / totalCpu + 100d * sys / totalCpu));
        cpu.setFree(df.format(100d * idle / totalCpu));
        return cpu;
    }

    private HostData.Sys getSysInfo(OperatingSystem os) {
        HostData.Sys sys = new HostData.Sys();
        sys.setOs(os.toString());

        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        String runtime = DateUtil.formatBetween(new Date(time), new Date(), BetweenFormatter.Level.HOUR);
        sys.setRuntime(runtime);

        try {
            // 服务器IP
            sys.setServerIp(LocalHostUtils.getLocalIp());
        } catch (Exception e) {
            log.error("get server ip failed", e);
            sys.setServerIp("系统异常");
        }

        return sys;
    }
}
