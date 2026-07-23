package com.github.stazxr.zblog.content.ext.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.ext.domain.dto.query.SensitiveWordQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.SensitiveWord;
import com.github.stazxr.zblog.content.ext.domain.vo.SensitiveWordVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 敏感词管理数据层
 *
 * @author SunTao
 * @since 2026-07-23
 */
@Mapper
public interface SensitiveWordExtMapper extends BaseMapper<SensitiveWord> {
    /**
     * 分页查询敏感词列表
     *
     * @param queryDto 查询参数
     * @return IPage<SensitiveWordVo>
     */
    IPage<SensitiveWordVo> selectSensitiveWordList(@Param("page") Page<SensitiveWordVo> page, @Param("query") SensitiveWordQueryDto queryDto);
}
