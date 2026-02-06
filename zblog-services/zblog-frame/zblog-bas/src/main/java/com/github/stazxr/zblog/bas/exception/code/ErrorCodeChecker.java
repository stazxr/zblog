package com.github.stazxr.zblog.bas.exception.code;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * 错误码检查器
 *
 * <p>扫描指定包下所有实现了 {@link ErrorCode} 的枚举，并校验：</p>
 * <ul>
 *     <li>错误码格式是否符合规范</li>
 *     <li>错误码是否重复</li>
 * </ul>
 *
 * @author SunTao
 * @since 2026-01-25
 */
public class ErrorCodeChecker {
    private static final Logger log = LoggerFactory.getLogger(ErrorCodeChecker.class);

    /**
     * 扫描并检查错误码
     *
     * @param basePackage 扫描起始包
     */
    public static void check(String basePackage) {
        log.info("开始扫描 ErrorCode 枚举：{}", basePackage);

        // 扫描指定包下所有实现了 ErrorCode 的类
        Reflections reflections = new Reflections(basePackage);
        Set<Class<? extends ErrorCode>> classes = reflections.getSubTypesOf(ErrorCode.class);

        int totalCount = 0;
        Set<String> codes = new HashSet<>();

        for (Class<? extends ErrorCode> clazz : classes) {
            if (!clazz.isEnum()) continue;
            for (ErrorCode errorCode : clazz.getEnumConstants()) {
                // 序号加一
                totalCount++;
                // 获取错误码
                String code = errorCode.getCode();
                // 格式校验
                if (!ErrorCodeRule.ROLE.matcher(code).matches()) {
                    throw new IllegalStateException("ErrorCode 格式错误: " + clazz.getSimpleName() + "." + code);
                }
                // 重复性校验
                if (!codes.add(code)) {
                    throw new IllegalStateException("ErrorCode 重复: " + clazz.getSimpleName() + "." + code);
                }
            }
        }

        log.info("扫描完成，一共加载了 {} 个错误码", totalCount);
    }
}
