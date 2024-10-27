package com.github.stazxr.zblog.converter;

import com.github.stazxr.zblog.core.base.BaseConverter;
import com.github.stazxr.zblog.domain.dto.MessageDto;
import com.github.stazxr.zblog.domain.entity.Message;
import com.github.stazxr.zblog.domain.vo.MessageVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * MessageConverter
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageConverter extends BaseConverter<Message, MessageDto, MessageVo> {

}
