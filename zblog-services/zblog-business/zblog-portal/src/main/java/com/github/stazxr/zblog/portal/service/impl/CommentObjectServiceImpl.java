package com.github.stazxr.zblog.portal.service.impl;

import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.content.ext.domain.enums.CommentType;
import com.github.stazxr.zblog.portal.domain.error.PortalErrorCode;
import com.github.stazxr.zblog.portal.service.CommentObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 评论对象管理业务层
 *
 * @author SunTao
 * @since 2026-09-05
 */
@Service
@RequiredArgsConstructor
public class CommentObjectServiceImpl implements CommentObjectService {
    /**
     * 检查评论对象是否存在
     *
     * @param type     评论对象类型
     * @param objectId 评论对象ID
     */
    @Override
    public void checkExists(Integer type, String objectId) {
        if (CommentType.MESSAGE.getValue().equals(type)) {
            // 留言只校验 objectId 是否为 0
            ThrowUtils.throwIf(!"0".equals(objectId), PortalErrorCode.EPORTA002);
        }
        if (CommentType.ARTICLE.getValue().equals(type)) {
            // TODO
        }
    }
}
