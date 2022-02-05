package com.github.stazxr.zblog.base.security;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由黑白名单信息
 *
 * @author SunTao
 * @since 2022-02-05
 */
public class RouterBlackWhiteListCache {
    private static final Set<String> whiteList = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private static final Set<String> blackList = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static boolean containsWhite(String url) {
        return whiteList.contains(url);
    }

    public static boolean containsBlack(String url) {
        return blackList.contains(url);
    }

    protected void setWhiteList(Set<String> whiteList1) {
        whiteList.clear();
        whiteList.addAll(whiteList1);
    }

    protected void setBlackList(Set<String> blackList1) {
        blackList.clear();
        blackList.addAll(blackList1);
    }
}
