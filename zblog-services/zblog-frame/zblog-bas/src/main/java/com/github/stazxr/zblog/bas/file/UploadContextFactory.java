package com.github.stazxr.zblog.bas.file;

import com.github.stazxr.zblog.bas.encryption.util.Md5Utils;
import com.github.stazxr.zblog.bas.file.path.FilePathContext;
import com.github.stazxr.zblog.util.io.FileUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * UploadContext 工厂类。
 *
 * <p>
 * 负责将 {@link MultipartFile} 一次性读取为一个稳定的上传上下文 {@link UploadContext}，
 * 作为后续上传、秒传、持久化的唯一数据载体
 * </p>
 *
 * <p>
 * 注意：
 * <ul>
 *   <li>MD5 只在此处计算一次，后续流程不得重复计算</li>
 *   <li>调用方必须在使用完成后调用 {@link UploadContext#close()}，以释放临时文件资源。</li>
 * </ul>
 * </p>
 *
 * @author SunTao
 * @since 2026-01-11
 */
public final class UploadContextFactory {
    public static UploadContext from(MultipartFile multipartFile, FilePathContext pathContext) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            Path tempFile = Files.createTempFile("zblog-upload-", ".tmp");

            try (InputStream in = multipartFile.getInputStream();
                 DigestInputStream din = new DigestInputStream(in, md5);
                 OutputStream out = Files.newOutputStream(tempFile)) {
                // 边读边算 MD5，边写临时文件
                StreamUtils.copy(din, out);
            }

            String md5Hex = Md5Utils.toHex(md5.digest());
            String originalFilename = multipartFile.getOriginalFilename();
            String suffix = FileUtils.getExtensionName(originalFilename);

            return new UploadContext(md5Hex, multipartFile.getSize(), tempFile,
                originalFilename, multipartFile.getContentType(), suffix, pathContext);
        } catch (Exception e) {
            throw new IllegalStateException("Create UploadContext failed", e);
        }
    }

    private UploadContextFactory() {}
}
