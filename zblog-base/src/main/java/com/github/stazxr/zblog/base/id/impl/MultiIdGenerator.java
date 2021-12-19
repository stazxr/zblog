package com.github.stazxr.zblog.base.id.impl;

import com.github.stazxr.zblog.base.id.work.impl.DbWorkIdResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 单机部署全局唯一ID生成器
 *
 * @author SunTao
 * @since 2021-12-12
 */
@Service("IdGeneratorService")
@ConditionalOnProperty(name = "system.deploy-type", havingValue = "multi")
public class MultiIdGenerator extends BaseWorkIdIdGeneratorImpl {
    private final DbWorkIdResolver dbWorkIdResolver;

    public MultiIdGenerator(DbWorkIdResolver dbWorkIdResolver) {
        this.dbWorkIdResolver = dbWorkIdResolver;
    }

    /**
     * 根据机器ID获取机器IP，单机部署，返回本地ID就可以
     *
     * @param workId 机器Id
     * @return WorkIp
     */
    @Override
    protected String parseWorkerIp(Long workId) {
        return dbWorkIdResolver.resolveWorkIp();
    }

    /**
     * 获取机器ID，单机部署，直接返回0L
     *
     * @return Long workId
     */
    @Override
    protected Long getWorkId() {
        return dbWorkIdResolver.resolveWorkId();
    }
}
