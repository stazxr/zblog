package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.base.domain.entity.FileStorage;
import org.apache.ibatis.annotations.Param;

/**
 * 物理文件数据层
 *
 * @author SunTao
 * @since 2026-01-02
 */
public interface FileStorageMapper extends BaseMapper<FileStorage> {
    /**
     * 根据 MD5 值查询物理文件存储信息
     *
     * @param fileMd5 文件MD5
     * @return FileStorage
     */
    FileStorage selectFileByMd5(@Param("fileMd5") String fileMd5);
}
