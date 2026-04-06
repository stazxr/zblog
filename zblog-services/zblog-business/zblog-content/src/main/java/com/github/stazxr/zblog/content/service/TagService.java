package com.github.stazxr.zblog.content.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.content.domain.dto.TagDto;
import com.github.stazxr.zblog.content.domain.dto.query.TagQueryDto;
import com.github.stazxr.zblog.content.domain.entity.Tag;
import com.github.stazxr.zblog.content.domain.vo.TagVo;

/**
 * 文章标签服务层
 *
 * @author SunTao
 * @since 2021-01-17
 */
public interface TagService extends IService<Tag> {
    /**
     * 分页查询标签列表
     *
     * @param queryDto 查询参数
     * @return IPage<TagVo>
     */
    IPage<TagVo> queryTagListByPage(TagQueryDto queryDto);

    /**
     * 查询标签详情
     *
     * @param tagId 标签ID
     * @return TagVo
     */
    TagVo queryTagDetail(Long tagId);

    /**
     * 新增标签
     *
     * @param tagDto 标签信息
     */
    void addTag(TagDto tagDto);

    /**
     * 编辑标签
     *
     * @param tagDto 标签信息
     */
    void editTag(TagDto tagDto);

    /**
     * 删除标签
     *
     * @param tagId 标签id
     */
    void deleteTag(Long tagId);
}
