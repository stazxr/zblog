package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.base.util.GenerateIdUtils;

import java.util.UUID;

/**
 * ID生成工具类
 *  重写 util 模块的类，方便一些非必须依赖的模块也可以使用唯一ID生成器，
 *  see {@code com.github.stazxr.zblog.base.id.IdGenerator}
 *
 * @author SunTao
 * @since 2021-12-12
 */
public class IdUtils extends GenerateIdUtils {
    /**
     * 生成一个随机字符串ID
     *
     * @return String
     */
    public static String getStringSeq() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "").toUpperCase();
    }
}
