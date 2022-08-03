package com.github.stazxr.zblog.base.component.id.work.api;

/**
 * 本机workId持有者
 *
 * @author SunTao
 * @since 2021-12-12
 */
public interface WorkIdResolver {
    /**
     * 返回机器ID
     *
     * @return workId
     */
    long resolveWorkId();

    /**
     * 返回机器IP
     *
     * @return workIp
     */
    String resolveWorkIp();
}
