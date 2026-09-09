package com.github.stazxr.zblog.content.ext.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.content.ext.domain.entity.CommentEmoji;
import com.github.stazxr.zblog.content.ext.domain.vo.CommentEmojiVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 评论表情管理数据层
 *
 * @author SunTao
 * @since 2026-08-31
 */
public interface CommentEmojiMapper extends BaseMapper<CommentEmoji> {
    /**
     * 查询平路表情包
     *
     * @return List<CommentEmojiVo>
     */
    List<CommentEmojiVo> selectCommentEmojis();

    /**
     * 删除所有的表情包
     */
    void deleteAll();

    /**
     * 新增表情包
     *
     * @param commentEmojiList 表情包
     */
    void insertBatch(@Param("list") List<CommentEmoji> commentEmojiList);

    /**
     * 通过 name 列表查询表情包
     *
     * @param names 表情名称列表
     * @return List<CommentEmojiVo>
     */
    List<CommentEmojiVo> selectCommentEmojisNames(@Param("names") Set<String> names);
}