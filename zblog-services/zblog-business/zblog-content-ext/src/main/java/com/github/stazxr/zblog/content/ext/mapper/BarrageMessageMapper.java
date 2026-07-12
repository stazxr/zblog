package com.github.stazxr.zblog.content.ext.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.ext.domain.dto.query.BarrageMessageQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.BarrageMessage;
import com.github.stazxr.zblog.content.ext.domain.vo.BarrageMessageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 弹幕管理数据层
 *
 * @author SunTao
 * @since 2026-07-07
 */
public interface BarrageMessageMapper extends BaseMapper<BarrageMessage> {
    /**
     * 分页查询弹幕列表
     *
     * @param page 分页参数
     * @param queryDto 查询参数
     * @return IPage<BarrageMessageVo>
     */
    IPage<BarrageMessageVo> selectBarrageMessageList(@Param("page") Page<BarrageMessageVo> page, @Param("query") BarrageMessageQueryDto queryDto);

    /**
     * 查询弹幕详情
     *
     * @param barrageMessageId 弹幕id
     * @return BarrageMessageVo
     */
    BarrageMessageVo selectBarrageMessageDetail(@Param("barrageMessageId") Long barrageMessageId);

    /**
     * 查询最新弹幕列表
     *
     * @param limit 查询数据量
     * @return List<BarrageMessageVo>
     */
    List<BarrageMessageVo> selectLastedBarrageMessageList(@Param("limit") int limit);
}
