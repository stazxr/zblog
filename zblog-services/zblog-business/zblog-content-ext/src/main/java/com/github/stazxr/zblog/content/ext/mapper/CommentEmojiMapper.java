package com.github.stazxr.zblog.content.ext.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.content.ext.domain.entity.CommentEmoji;
import com.github.stazxr.zblog.content.ext.domain.vo.CommentEmojiVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}