package com.github.stazxr.zblog.bas.file.path;

import com.github.stazxr.zblog.util.time.DateUtils;

/**
 * 按「日期」生成文件路径的策略实现。
 *
 * @author SunTao
 * @since 2026-01-10
 */
public class DateFilePathStrategy implements FilePathStrategy {
    /**
     * 自定义路径日期格式的KEY
     */
    private static final String DATE_PATTERN_KEY = "datePattern";
    /**
     * 默认路径日期格式
     */
    private static final String DEFAULT_DATE_PATTERN = "yyyy/MM/dd/";

    /**
     * 构建上传相对路径
     *
     * @param ctx 文件路径上下文
     * @return 2026/01/01/xxx.jpg
     */
    @Override
    public String buildPath(FilePathContext ctx) {
        String filename = ctx.getFilename();
        String datePattern = (String) ctx.getAttribute(DATE_PATTERN_KEY, DEFAULT_DATE_PATTERN);
        if (!datePattern.endsWith(PATH_SEPARATOR)) {
            datePattern = datePattern + PATH_SEPARATOR;
        }
        return DateUtils.formatNow(datePattern) + filename;
    }
}
