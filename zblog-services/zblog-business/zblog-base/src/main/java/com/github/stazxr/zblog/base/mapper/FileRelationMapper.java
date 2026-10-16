package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.base.domain.entity.FileRelation;
import com.github.stazxr.zblog.base.domain.vo.UploadFileVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件业务关联数据层
 *
 * @author SunTao
 * @since 2022-10-21
 */
public interface FileRelationMapper extends BaseMapper<FileRelation> {
    /**
     * 根据业务信息查询数据
     *
     * @param businessId   业务id
     * @param businessType 业务类型
     */
    List<UploadFileVo> selectByBusinessInfo(@Param("businessId") Long businessId, @Param("businessType") Integer businessType);

    /**
     * 根据业务信息删除数据
     *
     * @param businessId   业务id
     * @param businessType 业务类型
     */
    void deleteByBusinessInfo(@Param("businessId") Long businessId, @Param("businessType") Integer businessType);
}
