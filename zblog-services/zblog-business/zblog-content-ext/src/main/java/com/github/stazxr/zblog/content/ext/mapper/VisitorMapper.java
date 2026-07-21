package com.github.stazxr.zblog.content.ext.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.ext.domain.dto.query.VisitorQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.Visitor;
import com.github.stazxr.zblog.content.ext.domain.vo.VisitorVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 访客管理数据层
 *
 * @author SunTao
 * @since 2026-07-19
 */
public interface VisitorMapper extends BaseMapper<Visitor> {
    /**
     * 分页查询访客列表
     *
     * @param page 分页参数
     * @param queryDto 查询参数
     * @return IPage<VisitorVo>
     */
    IPage<VisitorVo> selectVisitorList(@Param("page") Page<VisitorVo> page, @Param("query") VisitorQueryDto queryDto);

    /**
     * 查询访客详情
     *
     * @param visitorId 访客id
     * @return VisitorVo
     */
    VisitorVo selectVisitorDetail(@Param("visitorId") String visitorId);
}
