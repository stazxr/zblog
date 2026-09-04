package com.github.stazxr.zblog.portal.service;

/**
 * 评论对象管理业务层
 *
 * @author SunTao
 * @since 2026-09-05
 */
public interface CommentObjectService {
    /**
     * 检查评论对象是否存在
     *
     * @param type     评论对象类型
     * @param objectId 评论对象ID
     */
    void checkExists(Integer type, String objectId);
}
