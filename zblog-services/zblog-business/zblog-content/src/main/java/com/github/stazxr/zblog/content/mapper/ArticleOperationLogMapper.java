package com.github.stazxr.zblog.content.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.domain.dto.query.ArticleOperationLogQueryDto;
import com.github.stazxr.zblog.content.domain.entity.ArticleOperationLog;
import com.github.stazxr.zblog.content.domain.vo.ArticleOperationLogVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文章操作日志管理数据层
 *
 * @author SunTao
 * @since 2026-10-27
 */
public interface ArticleOperationLogMapper extends BaseMapper<ArticleOperationLog> {
    /**
     * 分页查询文章操作日志列表
     *
     * @param page     分页参数
     * @param queryDto 查询参数
     * @return IPage<ArticleOperationLogVo>
     */
    IPage<ArticleOperationLogVo> selectLogList(@Param("page") Page<ArticleOperationLogVo> page, @Param("query") ArticleOperationLogQueryDto queryDto);
}
