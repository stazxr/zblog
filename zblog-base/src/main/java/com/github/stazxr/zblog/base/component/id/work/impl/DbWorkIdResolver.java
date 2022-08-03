package com.github.stazxr.zblog.base.component.id.work.impl;

import com.github.stazxr.zblog.base.component.id.impl.MultiIdGenerator;
import com.github.stazxr.zblog.base.component.id.work.model.WorkResult;
import com.github.stazxr.zblog.base.component.id.work.api.WorkIdResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 机器ID持有者实现
 *
 * @author SunTao
 * @since 2021-12-13
 */
@Slf4j
@Component("dbWorkIdResolver")
@ConditionalOnBean(MultiIdGenerator.class)
public class DbWorkIdResolver implements WorkIdResolver {
    private volatile Long workId;

    private volatile String workIp;

    @Resource
    private WorkIdService workIdService;

    /**
     * 最大支持 (1 << WORKER_ID_BITS) - 1 台机器
     *
     * @return workId
     */
    @Override
    public long resolveWorkId() {
        return workId;
    }

    @Override
    public String resolveWorkIp() {
        return workIp;
    }

    /**
     * 初始化获取机器信息失败
     *
     * @throws UnknownHostException failed get hostIp
     * @throws SocketException if an I/O error occurs
     */
    @PostConstruct
    private void init() throws UnknownHostException, SocketException {
        WorkResult workInfo = workIdService.generateWorkId();
        workId = workInfo.getWorkId();
        workIp = workInfo.getWorkIp();
        log.info("The work info {} - {}", workId, workIp);
    }
}
