package com.github.stazxr.zblog.bas.file.handler;

import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.file.FileErrorCode;
import com.github.stazxr.zblog.bas.file.FileException;
import com.github.stazxr.zblog.bas.file.UploadContext;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import com.github.stazxr.zblog.bas.file.path.FilePathContext;
import com.github.stazxr.zblog.bas.file.path.FilePathStrategy;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * 文件存储抽象基类。
 *
 * <p>
 * 该类作为 {@link FileHandler} 的默认实现，封装了文件上传与删除的通用逻辑，
 * 不同存储介质（本地文件系统、OSS、COS、Kodo 等）的实现类只需继承该类，
 * 并实现具体的文件读写与删除逻辑即可。
 * </p>
 *
 * <p>
 * 子类职责：
 * <ul>
 *     <li>实现 {@link #uploadImpl(FileInfo, InputStream)}，完成文件实际存储</li>
 *     <li>实现 {@link #deleteImpl(String)}，完成文件实际删除</li>
 *     <li>实现 {@link #getFileStream(String)}，完成文件流的获取（下载）</li>
 * </ul>
 * </p>
 *
 * @author SunTao
 * @since 2022-07-27
 */
public abstract class AbstractFileHandler implements FileHandler {
    /**
     * 文件访问 URL 前缀
     * <p>用于拼接文件对外访问地址</p>
     */
    private final String fileAccessUrl;

    /**
     * 文件存储路径前缀（逻辑路径）
     * <p>用于生成文件在存储系统中的相对路径</p>
     */
    private final String storagePathPrefix;

    /**
     * 默认 IO 缓冲区大小（8KB）
     */
    protected static final int BUFFER_SIZE = 8192;

    /**
     * 统一路径分隔符，避免不同系统路径不一致问题
     */
    protected static final String PATH_SEPARATOR = FilePathStrategy.PATH_SEPARATOR;

    /**
     * 构造方法。
     *
     * @param fileAccessUrl 文件访问 URL 前缀
     * @param storagePathPrefix 文件存储路径前缀
     */
    public AbstractFileHandler(String fileAccessUrl, String storagePathPrefix) {
        this.fileAccessUrl = fileAccessUrl.endsWith(PATH_SEPARATOR) ? fileAccessUrl : fileAccessUrl + PATH_SEPARATOR;
        this.storagePathPrefix = storagePathPrefix.endsWith(PATH_SEPARATOR) ? storagePathPrefix : storagePathPrefix + PATH_SEPARATOR;
    }

    /**
     * 上传文件。
     *
     * @param context 上传上下文，包含文件内容、唯一性信息及路径规则
     * @return FileInfo 文件信息
     * @throws FileException 上传失败时抛出
     */
    @Override
    public FileInfo upload(UploadContext context) throws FileException {
        try {
            FileInfo fileInfo = buildFileInfo(context);
            try (InputStream in = context.openStream()) {
                uploadImpl(fileInfo, in);
            }
            return fileInfo;
        } catch (Exception e) {
            throw new FileException(FileErrorCode.SFILEA001, e);
        }
    }

    /**
     * 实际文件上传实现（由子类完成）。
     *
     * @param fileInfo 文件元数据
     * @param inputStream 文件输入流，模板方法已关闭流，实现不应该做关闭操作
     * @throws IOException IO 异常
     */
    protected abstract void uploadImpl(FileInfo fileInfo, InputStream inputStream) throws IOException;

    /**
     * 删除文件（模板方法）。
     *
     * @param storageLocation 文件在存储系统中的唯一定位标识
     */
    @Override
    public void delete(String storageLocation) {
        try {
            if (StringUtils.isBlank(storageLocation)) {
                throw new IllegalArgumentException("storageLocation must not be blank");
            }
            deleteImpl(storageLocation);
        } catch (Exception e) {
            throw new FileException(FileErrorCode.SFILEA003, e);
        }
    }

    /**
     * 实际文件删除实现（由子类完成）。
     *
     * @param storageLocation 文件存储系统中的唯一标识路径
     * @throws IOException IO 异常
     */
    protected abstract void deleteImpl(String storageLocation) throws IOException;

    /**
     * 下载文件。
     *
     * <p>默认实现通过 {@link #getFileStream(String)} 获取文件输入流，并写入 HttpServletResponse 输出流。</p>
     *
     * @param storageLocation 文件存储定位标识
     * @param response Http 响应对象
     * @throws FileException 下载失败时抛出
     */
    @Override
    public void download(String storageLocation, HttpServletResponse response) throws FileException {
        try (InputStream inputStream = getFileStream(storageLocation)) {
            StreamUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            throw new FileException(FileErrorCode.SFILEA002, e);
        }
    }

    /**
     * 获取文件输入流。
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
     * @return 文件输入流
     * @throws IOException IO 异常
     */
    protected InputStream getFileStream(String storageLocation) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * 构建文件相对路径。
     *
     * @param filename 文件名
     * @param pathContext 路径上下文
     * @return 相对路径
     */
    protected String buildRelativePath(String filename, FilePathContext pathContext) {
        pathContext = pathContext == null ? new FilePathContext() : pathContext;
        pathContext.setFilename(filename);
        return getFilePathStrategy().buildPath(pathContext);
    }

    /**
     * 构建文件在存储系统中的定位标识。
     *
     * <p>
     * 默认实现为：{@code storagePathPrefix + relativePath}，子类可根据具体存储介质（如云存储）重写该方法。
     * </p>
     *
     * @param relativePath 文件相对路径
     * @return 文件在存储系统中的定位标识
     */
    protected String buildStorageLocation(String relativePath) {
        return getStoragePathPrefix() + relativePath;
    }

    /**
     * 构建文件对外访问 URL，该 URL 通常用于前端访问或下载文件。
     *
     * <p>
     * 默认实现为：{@code fileAccessUrl + relativePath}，子类可重写该方法以支持：
     * <ul>
     *     <li>CDN 域名</li>
     *     <li>签名 URL</li>
     *     <li>私有读权限的临时访问地址</li>
     * </ul>
     * </p>
     *
     * @param relativePath 文件相对路径
     * @return 文件访问 URL
     */
    protected String buildFileAccessUrl(String relativePath) {
        return getFileAccessUrl() + relativePath;
    }

    protected String getFileAccessUrl() {
        return fileAccessUrl;
    }

    protected String getStoragePathPrefix() {
        return storagePathPrefix;
    }

    /**
     * 解析上传文件并构建 {@link FileInfo} 元数据对象。
     *
     * @param context 上传上下文，包含文件内容、唯一性信息及路径规则
     * @return FileInfo 文件信息
     */
    private FileInfo buildFileInfo(UploadContext context) {
        // 生成文件名
        Long fileId = SequenceUtils.getId();
        String fileSuffix = FileUtils.getExtensionName(context.getOriginalFilename());
        String filename = StringUtils.isNotBlank(fileSuffix) ? fileId + "." + fileSuffix : String.valueOf(fileId);

        // 设置文件信息
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileId(fileId);
        fileInfo.setFilename(filename);
        fileInfo.setOriginalFilename(context.getOriginalFilename());
        fileInfo.setFileSize(context.getSize());
        fileInfo.setFileMd5(context.getMd5());
        fileInfo.setFileType(context.getContentType());
        fileInfo.setFileSuffix(fileSuffix.toLowerCase(Locale.ROOT));
        fileInfo.setUploadType(getFileUploadType().getType());
        String relativePath = buildRelativePath(filename, context.getPathContext());
        fileInfo.setFileRelativePath(relativePath);
        fileInfo.setStorageLocation(buildStorageLocation(relativePath));
        fileInfo.setFileAccessUrl(buildFileAccessUrl(relativePath));
        return fileInfo;
    }
}
