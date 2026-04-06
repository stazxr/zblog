package com.github.stazxr.zblog.content.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.domain.dto.query.TagQueryDto;
import com.github.stazxr.zblog.content.domain.entity.Tag;
import com.github.stazxr.zblog.content.domain.vo.TagVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 标签管理数据层
 *
 * @author SunTao
 * @since 2021-01-17
 */
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 查询标签列表
     *
     * @param queryDto 查询参数
     * @return IPage<TagVo>
     */
    IPage<TagVo> selectTagList(@Param("page") Page<TagVo> page, @Param("query") TagQueryDto queryDto);

    /**
     * 查询标签详情
     *
     * @param tagId 标签ID
     * @return TagVo
     */
    TagVo selectTagDetail(@Param("tagId") Long tagId);




//
//    /**
//     * 根据标签名称查询标签信息
//     *
//     * @param name 标签名称
//     * @return ArticleTag
//     */
//    Tag selectByTagName(@Param("name") String name);
//
//    /**
//     * 查询标签对应的文章数
//     *
//     * @param tagId 标签序号
//     * @return 文章数
//     */
//    Long selectArticleCountByTag(@Param("tagId") Long tagId);
//
//    /**
//     * 查询前台标签列表
//     *
//     * @return TagList
//     */
//    List<TagVo> selectWebTagList();
//
//    /**
//     * 获取标签云数据
//     *
//     * @return CloudTagVos
//     */
//    List<CloudTagVo> queryBoardTagList();
}
