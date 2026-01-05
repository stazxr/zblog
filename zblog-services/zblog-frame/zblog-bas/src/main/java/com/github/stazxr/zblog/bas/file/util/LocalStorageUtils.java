package com.github.stazxr.zblog.bas.file.util;

import com.github.stazxr.zblog.bas.file.FileHandlerEnum;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 本地文件上传工具类
 *
 * @author SunTao
 * @since 2021-02-19
 */
@Slf4j
public class LocalStorageUtils {
    /**
     * 本地文件上传
     *
     * @param fileInfo   文件信息
     * @param baseUrl    访问地址
     * @param uploadPath 上传路径
     * @return 文件id
     * @throws IOException 文件上传失败
     */
    public static Long uploadFile(FileInfo fileInfo, String baseUrl, String uploadPath) throws IOException {
        // 获取文件路径 ${uploadPath}/yyyy-MM/dd/
        Long fileId = SequenceUtils.getId();
        String filename = String.valueOf(fileId);
        String fileSuffix = fileInfo.getFileSuffix();
        if (StringUtils.isNotBlank(fileSuffix)) {
            filename = filename + "." + fileSuffix;
        }
        String datePath = DateUtils.formatNow("yyyy-MM/dd/");
        uploadPath = uploadPath.endsWith("/") ? uploadPath : uploadPath.concat("/");
        String relativePath = datePath + filename;
        String filepath = uploadPath + relativePath;

        // 补充文件信息
        fileInfo.setFileId(fileId);
        fileInfo.setFilename(filename);
        fileInfo.setFileAbsolutePath(filepath);
        fileInfo.setFileRelativePath(relativePath);
        fileInfo.setDownloadUrl(filepath.replace(uploadPath, baseUrl));
        fileInfo.setUploadType(FileHandlerEnum.LOCAL.getType());

        // 创建目录
        File dest = new File(filepath).getCanonicalFile();
        if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) {
            throw new IOException("上传目录创建失败，" + dest.getParentFile().getAbsolutePath());
        }

        // 上传文件
        try (InputStream in = fileInfo.getInputStream(); OutputStream out = Files.newOutputStream(Paths.get(filepath))) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }
        return fileId;
    }
}
