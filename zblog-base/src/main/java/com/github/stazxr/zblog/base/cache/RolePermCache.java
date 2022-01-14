package com.github.stazxr.zblog.base.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 角色 - 权限信息缓存器
 *
 * @author SunTao
 * @since 2020-12-28
 */
@Slf4j
public class RolePermCache {
    /**
     * Cache role - perms.
     */
    private static final Map<Long, Set<Long>> userRoleCache = new ConcurrentHashMap<>();
}
