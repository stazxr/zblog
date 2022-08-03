package com.github.stazxr.zblog.base.component.security;

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
    private static final Set<String> WHITE_LIST = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private static final Set<String> BLACK_LIST = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static boolean containsWhite(String url) {
        return WHITE_LIST.contains(url);
    }

    public static boolean containsBlack(String url) {
        return BLACK_LIST.contains(url);
    }

    protected void setWhiteList(Set<String> whiteList1) {
        WHITE_LIST.clear();
        WHITE_LIST.addAll(whiteList1);
    }

    protected void setBlackList(Set<String> blackList1) {
        BLACK_LIST.clear();
        BLACK_LIST.addAll(blackList1);
    }
}
