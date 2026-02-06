package com.github.stazxr.zblog.bas.exception.code;

import java.util.regex.Pattern;

/**
 * 错误码正则规则
 *
 * <p>支持可选类别位（第2-3位），模块标识4位，分组字母1位，序号3位</p>
 *
 * @author SunTao
 * @since 2026-01-25
 */
public final class ErrorCodeRule {
    /**
     * 正则格式：
     *  E/S    → 第1位：异常类型
     *  (?:[A-Z\d]{2})? → 第2-3位：异常类别（可选）
     *  [A-Z]{4} → 模块标识
     *  [A-Z] → 分组字母
     *  \d{3} → 序号
     */
    private static final String CODE_PATTERN = "^[ES](?:[A-Z\\d]{2})?[A-Z]{4}[A-Z]\\d{3}$";

    /** 校验 Pattern */
    public static final Pattern ROLE = Pattern.compile(CODE_PATTERN);

    private ErrorCodeRule() {
        // utility class
    }
}
