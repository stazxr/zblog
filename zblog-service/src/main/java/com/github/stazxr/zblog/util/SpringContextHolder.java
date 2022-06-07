package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.core.util.SpringContextUtils;

/**
 * Spring上下文工具类
 *  重写 util 模块的 @code{SpringContextHolder} 类，方便一些非必须依赖 Core 的模块也可以使用 Spring 上下文工具类，
 *  see {@code com.github.stazxr.zblog.base.id.IdGenerator}
 *
 * @author SunTao
 * @since 2021-12-12
 */
public class SpringContextHolder extends SpringContextUtils {
}
