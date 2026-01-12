package com.github.stazxr.zblog.bas.file.path;

/**
 * 按「哈希值」生成文件路径的策略实现，防止单目录过多文件。
 *
 * @author SunTao
 * @since 2026-01-10
 */
public class HashFilePathStrategy implements FilePathStrategy {
    /**
     * 构建上传相对路径
     *
     * @param ctx 文件路径上下文
     * @return xxx/xxx.jpg
     */
    @Override
    public String buildPath(FilePathContext ctx) {
        int hash = Math.abs(ctx.getFilename().hashCode());
        return (hash % 100) + PATH_SEPARATOR + ctx.getFilename();
    }
}
