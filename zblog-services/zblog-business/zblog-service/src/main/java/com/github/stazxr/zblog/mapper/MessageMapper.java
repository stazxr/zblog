package com.github.stazxr.zblog.mapper;

import com.github.stazxr.zblog.core.base.BaseMapper;
import com.github.stazxr.zblog.domain.dto.query.MessageQueryDto;
import com.github.stazxr.zblog.domain.entity.Message;
import com.github.stazxr.zblog.domain.vo.MessageVo;

import java.util.List;

/**
 * 留言数据持久层
 *
 * @author SunTao
 * @since 2023-02-03
 */
public interface MessageMapper extends BaseMapper<Message> {
    /**
     * 查询后台留言列表
     *
     * @param queryDto 查询参数
     * @return MessageVoList
     */
    List<MessageVo> selectMessageList(MessageQueryDto queryDto);

    /**
     * 审核留言
     *
     * @param messageIds 留言列表
     */
    void auditMessage(List<Long> messageIds);

    /**
     * 查询前台弹幕列表
     *
     * @return MessageVo
     */
    List<MessageVo> selectWebMessageList();
}
