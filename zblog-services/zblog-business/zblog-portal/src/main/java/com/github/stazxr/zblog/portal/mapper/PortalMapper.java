package com.github.stazxr.zblog.portal.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.portal.domain.dto.query.PortalCommentQueryDto;
import com.github.stazxr.zblog.portal.domain.vo.PortalCommentVo;
import org.apache.ibatis.annotations.Param;

/**
 * 门户管理数据持久层
 *
 * @author SunTao
 * @since 2026-09-07
 */
public interface PortalMapper {
    /**
     * 查询前台评论总数
     *
     * @param queryDto 查询参数
     * @return Long 评论总数
     */
    Long selectCommentTotal(@Param("query") PortalCommentQueryDto queryDto);

    /**
     * 查询前台评论列表
     *
     * @param page     分页参数
     * @param queryDto 查询参数
     * @return IPage<PortalCommentVo>
     */
    IPage<PortalCommentVo> selectCommentList(@Param("page") Page<PortalCommentVo> page, @Param("query") PortalCommentQueryDto queryDto);

    /**
     * 查询前台评论回复列表
     *
     * @param page     分页参数
     * @param queryDto 查询参数
     * @return IPage<PortalCommentVo>
     */
    IPage<PortalCommentVo> selectReplyCommentList(@Param("page") Page<PortalCommentVo> page, @Param("query") PortalCommentQueryDto queryDto);
}
