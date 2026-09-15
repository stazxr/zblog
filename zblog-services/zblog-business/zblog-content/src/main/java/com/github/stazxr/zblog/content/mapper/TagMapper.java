package com.github.stazxr.zblog.content.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.domain.dto.query.TagQueryDto;
import com.github.stazxr.zblog.content.domain.entity.Tag;
import com.github.stazxr.zblog.content.domain.vo.TagVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签管理数据层
 *
 * @author SunTao
 * @since 2021-01-17
 */
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 分页查询标签列表
     *
     * @param page     分页参数
     * @param queryDto 查询参数
     * @return IPage<TagVo>
     */
    IPage<TagVo> selectTagList(@Param("page") Page<TagVo> page, @Param("query") TagQueryDto queryDto);

    /**
     * 查询标签列表（公共）
     *
     * @param keyword 查询参数（标签名称）
     * @return List<TagVo>
     */
    List<TagVo> selectAllTagList(@Param("keyword") String keyword);

    /**
     * 查询标签详情
     *
     * @param tagId 标签ID
     * @return TagVo
     */
    TagVo selectTagDetail(@Param("tagId") Long tagId);
}
