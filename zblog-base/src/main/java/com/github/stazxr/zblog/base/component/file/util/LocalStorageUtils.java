package com.github.stazxr.zblog.base.component.file.util;

import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 本地文件上传工具类
 *
 * @author SunTao
 * @since 2021-02-19
 */
public class LocalStorageUtils {
    /**
     * 本地文件上传
     *
     * @param file 文件信息
     * @param uploadPathPrefix 上传路径
     * @return 文件的相对上传路径
     * @throws IOException 文件上传失败
     */
    public static String uploadLocal(MultipartFile file, String uploadPathPrefix) throws IOException {
        // 获取文件路径
        String originalFilename = file.getOriginalFilename();
        String suffix = FileUtils.getExtensionName(originalFilename);
        String fileName = String.valueOf(GenerateIdUtils.getId());
        if (StringUtils.isNotBlank(suffix)) {
            fileName = fileName + "." + suffix;
        }
        String datePath = DateUtils.formatNow("yyyy-MM/dd/");
        uploadPathPrefix = uploadPathPrefix.endsWith("/") ? uploadPathPrefix : uploadPathPrefix.concat("/");
        String relativePath = datePath.concat(FileUtils.getFileEnType(suffix)).concat("/").concat(fileName);
        String filePath = uploadPathPrefix + relativePath;

        // 创建目录
        File dest = new File(filePath).getCanonicalFile();
        if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) {
            throw new IOException("上传目录创建失败，" + dest.getParentFile().getAbsolutePath());
        }

        // 上传
        file.transferTo(dest);
        return relativePath;
    }
}
