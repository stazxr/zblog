package com.github.stazxr.zblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.domain.dto.query.MessageQueryDto;
import com.github.stazxr.zblog.domain.entity.Message;
import com.github.stazxr.zblog.domain.vo.MessageVo;
import com.github.stazxr.zblog.mapper.MessageMapper;
import com.github.stazxr.zblog.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 留言服务实现层
 *
 * @author SunTao
 * @since 2023-02-03
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    /**
     * 分页查询后台留言列表
     *
     * @param queryDto 查询参数
     * @return MessageVoList
     */
    @Override
    public PageInfo<MessageVo> pageMessageList(MessageQueryDto queryDto) {
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(baseMapper.selectMessageList(queryDto));
    }

    /**
     * 删除留言
     *
     * @param messageIds 留言列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessage(List<Long> messageIds) {
        if (messageIds != null && messageIds.size() > 0) {
            baseMapper.deleteBatchIds(messageIds);
        }
    }

    /**
     * 审核留言
     *
     * @param messageIds 留言列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditMessage(List<Long> messageIds) {
        if (messageIds != null && messageIds.size() > 0) {
            baseMapper.auditMessage(messageIds);
        }
    }
}
