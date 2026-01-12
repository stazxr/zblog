package com.github.stazxr.zblog.bas.file.util;

import com.github.stazxr.zblog.bas.file.model.StorageLocation;
import com.github.stazxr.zblog.util.StringUtils;

/**
 * 文件存储定位标识解析工具。
 *
 * <p>
 * 用于将 {@code storageLocation} 统一解析为 {@link StorageLocation} 对象，
 * 以屏蔽不同存储介质（本地文件系统、云存储等）对文件定位方式的差异。
 * </p>
 *
 * <h3>支持的格式</h3>
 * <ul>
 *     <li>
 *         <b>云存储（bucket:key）</b>：
 *         <pre>bucket-name:path/to/file.jpg</pre>
 *     </li>
 *     <li>
 *         <b>云存储（key-only）或本地存储路径</b>：
 *         <pre>path/to/file.jpg</pre>
 *         <pre>/home/zblog/file/upload/2026-01/06/xxx.png</pre>
 *     </li>
 * </ul>
 *
 * @author SunTao
 * @since 2026-01-12
 */
public final class StorageLocationParser {
    /**
     * bucket 与 key 的分隔符
     */
    private static final String BUCKET_SEPARATOR = ":";

    private StorageLocationParser() {}

    /**
     * 解析文件存储定位标识。
     *
     * @param storageLocation 文件在存储系统中的唯一定位标识
     * @return 解析后的 {@link StorageLocation} 对象
     * @throws IllegalArgumentException 当参数为空或格式非法时抛出
     */
    public static StorageLocation parse(String storageLocation) {
        if (StringUtils.isBlank(storageLocation)) {
            throw new IllegalArgumentException("storageLocation must not be blank");
        }

        // bucket:key 形式
        int idx = storageLocation.indexOf(BUCKET_SEPARATOR);
        if (idx > 0) {
            String bucket = storageLocation.substring(0, idx);
            String key = storageLocation.substring(idx + 1);
            if (StringUtils.isBlank(key)) {
                throw new IllegalArgumentException("Invalid storageLocation: " + storageLocation);
            }
            return new StorageLocation(true, bucket, key, storageLocation);
        }

        // key-only 或本地路径
        return new StorageLocation(false, null, storageLocation, storageLocation);
    }
}
