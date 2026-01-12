package com.github.stazxr.zblog.bas.file.path;

/**
 * 文件存储路径生成策略接口。
 *
 * <p>
 * 用于根据 {@link FilePathContext} 中提供的上下文信息，构建文件在存储介质中的<strong>相对路径</strong>。
 * </p>
 *
 * <p>
 * 该接口的实现应只关注路径规则本身，不应包含存储桶名称、域名、磁盘绝对路径等信息。
 * </p>
 *
 * <p>
 * 常见实现策略包括：
 * <ul>
 *     <li>按日期分目录</li>
 *     <li>按用户 ID 分目录</li>
 *     <li>按业务模块分目录</li>
 *     <li>哈希散列目录</li>
 *     <li>扁平目录结构</li>
 * </ul>
 * </p>
 *
 * @author SunTao
 * @since 2026-01-10
 */
public interface FilePathStrategy {
    /**
     * 目录分隔符
     */
    String PATH_SEPARATOR = "/";

    /**
     * 根据路径上下文构建文件的相对存储路径。
     *
     * <p>
     * 返回的路径应为相对路径，
     * 示例：
     * </p>
     *
     * <pre>
     * avatar/10001/2026/01/10/xxx.png
     * article/xxx.jpg
     * 2026/01/10/xxx.png
     * </pre>
     *
     * @param context 文件路径上下文
     * @return 文件在存储系统中的相对路径
     */
    String buildPath(FilePathContext context);
}

