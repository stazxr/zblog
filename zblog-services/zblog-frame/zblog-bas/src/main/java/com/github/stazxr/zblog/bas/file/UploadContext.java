package com.github.stazxr.zblog.bas.file;

import com.github.stazxr.zblog.bas.file.path.FilePathContext;
import com.github.stazxr.zblog.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 文件上传上下文。
 *
 * <p>
 * UploadContext 是一次文件上传流程中的核心数据载体，封装了：
 * </p>
 *
 * <ul>
 *   <li>文件唯一性信息（MD5、size）</li>
 *   <li>文件内容（临时文件）</li>
 *   <li>文件元信息快照（文件名、类型、后缀）</li>
 *   <li>路径/存储上下文（FilePathContext）</li>
 * </ul>
 *
 * <p>
 * 设计原则：
 * <ul>
 *   <li>MD5 只计算一次</li>
 *   <li>文件内容可多次读取（每次 openStream 返回新流）</li>
 *   <li>不依赖 MultipartFile，便于跨模块传递</li>
 * </ul>
 * </p>
 *
 * <p>
 * 生命周期说明：
 * <ul>
 *   <li>UploadContext 持有临时文件的生命周期</li>
 *   <li>使用完成后必须调用 {@link #close()} 释放资源</li>
 *   <li>close 后再次调用 {@link #openStream()} 将抛出异常</li>
 * </ul>
 * </p>
 *
 * @author SunTao
 * @since 2026-01-11
 */
public class UploadContext implements AutoCloseable {
    /* ========= 文件唯一性 ========= */
    private final String md5;
    private final long size;

    /* ========= 文件内容载体 ========= */
    private final Path tempFile;

    /* ========= 元信息（快照） ========= */
    private final String originalFilename;
    private final String contentType;
    private final String fileSuffix;

    /* ========= 路径上下文 ========= */
    private final FilePathContext pathContext;

    private volatile boolean closed = false;

    public UploadContext(String md5, long size, Path tempFile, String originalFilename,
            String contentType, String fileSuffix, FilePathContext pathContext) {
        this.md5 = md5;
        this.size = size;
        this.tempFile = tempFile;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.fileSuffix = fileSuffix;
        this.pathContext = pathContext;
    }

    public String getMd5() {
        return md5;
    }

    public long getSize() {
        return size;
    }

    public String getOriginalFilename() {
        if (StringUtils.isBlank(originalFilename)) {
            return "unknown";
        }
        return originalFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public FilePathContext getPathContext() {
        return pathContext;
    }

    /**
     * 打开文件内容流。
     *
     * @return 每次调用都会返回一个全新的 InputStream
     * @throws IllegalStateException 如果 UploadContext 已关闭
     */
    public InputStream openStream() throws IOException {
        if (closed) {
            throw new IllegalStateException("UploadContext already closed");
        }
        return Files.newInputStream(tempFile);
    }

    /**
     * 释放临时文件资源。
     */
    @Override
    public void close() throws IOException {
        if (!closed) {
            closed = true;
            Files.deleteIfExists(tempFile);
        }
    }
}
