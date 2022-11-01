package com.github.stazxr.zblog.base.component.file;

import com.github.stazxr.zblog.base.component.file.model.FileInfo;
import com.github.stazxr.zblog.core.exception.ServiceException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
     * @param files 文件列表
     * @return 上传的文件列表
     */
    default List<FileInfo> uploadFile(MultipartFile[] files) {
        throw new ServiceException("未实现的存储逻辑");
    }

    /**
     * 删除文件
     *
     * @param filepath 文件路径
     */
    default void deleteFile(String filepath) {}

    /**
     * 批量删除文件
     *
     * @param filepathList 文件路径列表
     */
    default void deleteFiles(List<String> filepathList) {}

    /**
     * 下载文件
     *
     * @param filepath 文件路径
     * @param response 响应对象
     */
    default void downloadFile(String filepath, HttpServletResponse response) {}
}
