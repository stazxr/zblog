package com.github.stazxr.zblog.bas.file.handler;

import com.github.stazxr.zblog.bas.file.FileException;
import com.github.stazxr.zblog.bas.file.UploadContext;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import com.github.stazxr.zblog.bas.file.path.DateFilePathStrategy;
import com.github.stazxr.zblog.bas.file.path.FilePathStrategy;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件处理统一接口。
 *
 * <p>
 * 用于抽象不同存储介质（本地文件系统、阿里云 OSS、腾讯云 COS、七牛云 Kodo等）的文件上传、下载与删除能力。
 * </p>
 *
 * <p>
 * 该接口的实现类通常基于 {@link AbstractFileHandler}，
 * 并通过不同的存储策略实现具体的文件读写逻辑。
 * </p>
 *
 * <p>
 * 支持通过 {@link FilePathStrategy} 自定义文件存储路径规则。
 * </p>
 *
 * @author SunTao
 * @since 2022-07-27
 */
public interface FileHandler {
    /**
     * 上传文件。
     *
     * @param context 上传上下文，包含文件内容、唯一性信息及路径规则
     * @return FileInfo 文件信息
     * @throws FileException 上传失败时抛出
     */
    FileInfo upload(UploadContext context) throws FileException;

    /**
     * 删除单个文件。
     *
     * <p>
     * 参数 {@code storageLocation} 表示文件在存储系统中的唯一定位标识，不同存储实现对该值的解析规则不同：
     *
     * <ul>
     *   <li><b>本地存储</b>：文件系统中的绝对路径，如：
     *     <pre>/home/zblog/file/upload/2026-01/06/xxx.png</pre>
     *   </li>
     *   <li><b>云存储（bucket:key）</b>：
     *     <pre>zblog-2026-01:upload/2026/01/09/xxx.jpg</pre>
     *   </li>
     *   <li><b>云存储（key）</b>：
     *     <pre>upload/2026/01/09/xxx.jpg</pre>
     *   </li>
     * </ul>
     * 具体解析规则由子类实现决定。
     * </p>
     *
     * @param storageLocation 文件在存储系统中的唯一定位标识
     * @throws FileException 删除失败时抛出
     */
    void delete(String storageLocation) throws FileException;

    /**
     * 下载文件。
     *
     * <p>
     * 参数 {@code storageLocation} 表示文件在存储系统中的唯一定位标识，不同存储实现对该值的解析规则不同：
     *
     * <ul>
     *   <li><b>本地存储</b>：文件系统中的绝对路径，如：
     *     <pre>/home/zblog/file/upload/2026-01/06/xxx.png</pre>
     *   </li>
     *   <li><b>云存储（bucket:key）</b>：
     *     <pre>zblog-2026-01:upload/2026/01/09/xxx.jpg</pre>
     *   </li>
     *   <li><b>云存储（key）</b>：
     *     <pre>upload/2026/01/09/xxx.jpg</pre>
     *   </li>
     * </ul>
     * 具体解析规则由子类实现决定。
     * </p>
     *
     * @param storageLocation 文件存储定位标识
     * @param response Http 响应对象
     * @throws FileException 下载失败时抛出
     */
    void download(String storageLocation, HttpServletResponse response) throws FileException;

    /**
     * 获取文件上传类型
     *
     * @return 上传类型
     */
    FileHandlerEnum getFileUploadType();

    /**
     * 获取文件路径策略。
     * <p>默认返回 {@link DateFilePathStrategy}，按日期生成存储路径。</p>
     * <p>实现类可重写以使用自定义路径策略。</p>
     *
     * @return 文件路径策略实例
     */
    default FilePathStrategy getFilePathStrategy() {
        return new DateFilePathStrategy();
    }
}
