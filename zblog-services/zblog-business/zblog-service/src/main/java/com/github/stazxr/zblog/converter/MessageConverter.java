package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.MessageDto;
import com.github.stazxr.zblog.domain.entity.Message;
import com.github.stazxr.zblog.domain.vo.MessageVo;
import org.springframework.stereotype.Component;

/**
 * MessageConverter
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Component
public class MessageConverter implements BaseConverter<Message, MessageDto, MessageVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<Message> getEntityClass() {
        return Message.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<MessageVo> getVoClass() {
        return MessageVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<MessageDto> getDtoClass() {
        return MessageDto.class;
    }
}
