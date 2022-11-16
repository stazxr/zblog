package com.github.stazxr.zblog.base.converter;

import com.github.stazxr.zblog.base.domain.dto.NodeDto;
import com.github.stazxr.zblog.base.domain.entity.ServerNode;
import com.github.stazxr.zblog.base.domain.vo.NodeVo;
import com.github.stazxr.zblog.core.base.BaseConverter;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * ServerNodeConverter
 *
 * @author SunTao
 * @since 2022-11-09
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServerNodeConverter extends BaseConverter<ServerNode, NodeDto, NodeVo> {

}
