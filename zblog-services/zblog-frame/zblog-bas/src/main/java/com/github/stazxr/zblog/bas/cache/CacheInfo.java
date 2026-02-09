package com.github.stazxr.zblog.bas.cache;

/**
 * 缓存对象信息
 *
 * @author SunTao
 * @since 2026-02-10
 */
public class CacheInfo {
    private String key;
    private Object value;
    private Long ttl;
    private String cachePool;
    private boolean showValue = false;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public String getCachePool() {
        return cachePool;
    }

    public void setCachePool(String cachePool) {
        this.cachePool = cachePool;
    }

    public boolean isShowValue() {
        return showValue;
    }

    public void setShowValue(boolean showValue) {
        this.showValue = showValue;
    }
}
