package com.github.stazxr.zblog.util;

import java.util.List;

/**
 * ID生成工具类，只声明方法，不进行实现，供给非必须依赖模块调用
 *
 * @author SunTao
 * @since 2021-12-12
 */
public class IdUtils {
    /**
     * 生成唯一ID
     *
     * @return Long id
     */
    public static Long getId() {
        // ToDo: 由依赖模块重写该类
        return null;
    }

    /**
     * 生成唯一ID列表
     *
     * @param count 生成ID的个数
     * @return List<Long> id列表
     */
    public static List<Long> getIdList(Integer count) {
        // ToDo: 由依赖模块重写该类
        return null;
    }
}
