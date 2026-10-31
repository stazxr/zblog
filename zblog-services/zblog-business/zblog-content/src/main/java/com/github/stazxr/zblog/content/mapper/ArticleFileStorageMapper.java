package com.github.stazxr.zblog.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.content.domain.entity.ArticleFileStorage;
import org.apache.ibatis.annotations.Param;

/**
 * 文章内容上传数据层
 *
 * @author SunTao
 * @since 2026-11-01
 */
public interface ArticleFileStorageMapper extends BaseMapper<ArticleFileStorage> {
    /**
     * 根据 MD5 值查询物理文件存储信息
     *
     * @param fileMd5        文件MD5
     * @param fileUploadType 文件上传类型
     * @return ArticleFileStorage
     */
    ArticleFileStorage selectFileByMd5(@Param("fileMd5") String fileMd5, @Param("fileUploadType") int fileUploadType);
}
