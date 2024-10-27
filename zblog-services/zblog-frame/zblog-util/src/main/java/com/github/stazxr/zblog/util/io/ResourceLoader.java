package com.github.stazxr.zblog.util.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

/**
 * 资源文件加载工具类
 *
 * @author SunTao
 * @since 2022-07-29
 */
public class ResourceLoader {
    private static final String WIN_DIR_SPLIT = "\\";
    private static final String UNIX_DIR_SPLIT = "/";

    /**
     * 获取 Resource 目录下的文件的绝对路径
     *
     * @param resourceName 资源名称 eg: conf/dnc.conf  dnc.conf
     * @return 资源绝对路径
     */
    public static String getResourceAbsolutePath(String resourceName) {
        try {
            String resource = resourceName;
            if (resource.contains(WIN_DIR_SPLIT)) {
                resource = resourceName.replace(WIN_DIR_SPLIT, UNIX_DIR_SPLIT);
            }
            ClassLoader classLoader = ResourceLoader.class.getClassLoader();
            URL resourceUrl = classLoader.getResource(resource);
            if (resourceUrl == null) {
                throw new FileNotFoundException("Resource not found: " + resourceName);
            }
            return new File(resourceUrl.getFile()).getAbsolutePath();
        } catch (Exception e) {
            throw new IllegalStateException("资源【" + resourceName + "】加载失败", e);
        }
    }
}
