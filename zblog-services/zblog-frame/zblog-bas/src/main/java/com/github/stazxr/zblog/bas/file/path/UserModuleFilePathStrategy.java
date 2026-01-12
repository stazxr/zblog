package com.github.stazxr.zblog.bas.file.path;

import com.github.stazxr.zblog.util.StringUtils;

/**
 * 按「业务模块 + 用户 ID」生成文件路径的策略实现。
 *
 * @author SunTao
 * @since 2026-01-10
 */
public class UserModuleFilePathStrategy implements FilePathStrategy {
    /**
     * 构建上传相对路径
     *
     * @param ctx 文件路径上下文
     * @return avatar/10001/xxx.png, article/10001/xxx.jpg
     */
    @Override
    public String buildPath(FilePathContext ctx) {
        StringBuilder path = new StringBuilder();

        if (StringUtils.isNotBlank(ctx.getModule())) {
            path.append(ctx.getModule()).append(PATH_SEPARATOR);
        }

        if (ctx.getUserId() != null) {
            path.append(ctx.getUserId()).append(PATH_SEPARATOR);
        }

        path.append(ctx.getFilename());
        return path.toString();
    }
}
