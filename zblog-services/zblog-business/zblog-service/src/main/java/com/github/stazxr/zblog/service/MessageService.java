package com.github.stazxr.zblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.domain.dto.query.MessageQueryDto;
import com.github.stazxr.zblog.domain.entity.Message;
import com.github.stazxr.zblog.domain.vo.MessageVo;

import java.util.List;

/**
 * 留言服务层
 *
 * @author SunTao
 * @since 2023-02-03
 */
public interface MessageService extends IService<Message> {
    /**
     * 分页查询后台留言列表
     *
     * @param queryDto 查询参数
     * @return MessageVoList
     */
    PageInfo<MessageVo> pageMessageList(MessageQueryDto queryDto);

    /**
     * 删除留言
     *
     * @param messageIds 留言列表
     */
    void deleteMessage(List<Long> messageIds);

    /**
     * 审核留言
     *
     * @param messageIds 留言列表
     */
    void auditMessage(List<Long> messageIds);
}
