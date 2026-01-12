package com.github.stazxr.zblog.bas.file.path;

/**
 * 按「文件名」生成文件路径的策略实现。
 *
 * @author SunTao
 * @since 2026-01-10
 */
public class NoneFilePathStrategy implements FilePathStrategy {
    /**
     * 构建上传相对路径
     *
     * @param ctx 文件路径上下文
     * @return xxx.jpg
     */
    @Override
    public String buildPath(FilePathContext ctx) {
        return ctx.getFilename();
    }
}
