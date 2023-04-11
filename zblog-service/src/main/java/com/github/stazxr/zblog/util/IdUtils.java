package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.base.util.GenerateIdUtils;

/**
 * ID生成工具类
 *
 * 介绍：
 *      重写 zblog-util 模块的 {@link com.github.stazxr.zblog.util.IdUtils } 类，
 *  便一些非必须依赖 Base 的模块（日志、消息、任务）也可以使用唯一 ID 生成器。
 *
 *  more see {@link com.github.stazxr.zblog.base.component.id.IdGenerator }
 *  more see {@link com.github.stazxr.zblog.base.util.GenerateIdUtils }
 *
 * @author SunTao
 * @since 2021-12-12
 */
public class IdUtils extends GenerateIdUtils {
}
