package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.domain.vo.BlogWebVo;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 门户数据持久层
 *
 * @author SunTao
 * @since 2022-12-09
 */
public interface PortalMapper {
    /**
     * 查询博客前台信息
     *
     * @return BlogWebVo
     */
    BlogWebVo selectBlogWebInfo();

    /**
     * 查询评论点赞列表
     *
     * @param userId 用户ID
     * @return likes
     */
    Set<Long> selectCommentListSet(@Param("userId") Long userId);
}
