package com.github.stazxr.zblog.bas.file;

import com.github.stazxr.zblog.bas.file.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理方式定义
 *
 * @author SunTao
 * @since 2022-07-27
 */
public interface FileHandler {
    /**
     * 文件上传
     *
     * @param file 文件列表
     * @return 上传的文件列表
     */
    FileInfo uploadFile(MultipartFile file);

    /**
     * 文件上传
     *
     * @param files 文件列表
     * @return 上传的文件列表
     */
    default List<FileInfo> uploadFile(MultipartFile[] files) {
        List<FileInfo> fileInfos = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                FileInfo fileInfo = uploadFile(file);
                fileInfos.add(fileInfo);
            }
        }
        return fileInfos;
    }

    /**
     * 下载文件
     *
     * @param filepath 文件路径
     * @param response 响应对象
     */
    void downloadFile(String filepath, HttpServletResponse response);

    /**
     * 删除文件
     *
     * @param filepath 文件路径
     */
    void deleteFile(String filepath);

    /**
     * 批量删除文件
     *
     * @param filepathList 文件路径列表
     */
    default void deleteFiles(List<String> filepathList) {
        if (filepathList != null && filepathList.size() > 0) {
            for (String filepath : filepathList) {
                deleteFile(filepath);
            }
        }
    }
}
