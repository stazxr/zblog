package com.github.stazxr.zblog.bas.file.model;

/**
 * 文件存储定位信息模型。
 *
 * <p>
 * 该对象是 {@code storageLocation} 的结构化表示，用于统一描述文件在不同存储介质中的定位方式。
 * </p>
 *
 * @author SunTao
 * @since 2026-01-12
 */
public final class StorageLocation {
    /**
     * 是否为 bucket:key 形式
     */
    private final boolean hasBucket;

    /**
     * 桶名称（本地存储为 null）
     */
    private final String bucket;

    /**
     * 对象 key / 文件路径
     */
    private final String key;

    /**
     * 原始 storageLocation
     */
    private final String raw;

    public StorageLocation(boolean hasBucket, String bucket, String key, String raw) {
        this.hasBucket = hasBucket;
        this.bucket = bucket;
        this.key = key;
        this.raw = raw;
    }

    public boolean hasBucket() {
        return hasBucket;
    }

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public String getRaw() {
        return raw;
    }

    @Override
    public String toString() {
        return raw;
    }
}
