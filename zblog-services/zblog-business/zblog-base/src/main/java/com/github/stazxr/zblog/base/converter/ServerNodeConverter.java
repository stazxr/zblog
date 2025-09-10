package com.github.stazxr.zblog.base.converter;

import com.github.stazxr.zblog.base.domain.dto.NodeDto;
import com.github.stazxr.zblog.base.domain.entity.ServerNode;
import com.github.stazxr.zblog.base.domain.vo.NodeVo;
import com.github.stazxr.zblog.core.base.BaseConverter;
import org.springframework.stereotype.Component;

/**
 * ServerNodeConverter
 *
 * @author SunTao
 * @since 2022-11-09
 */
@Component
public class ServerNodeConverter implements BaseConverter<ServerNode, NodeDto, NodeVo> {
    /**
     * 获取实体对象类型
     *
     * @return 实体对象类型
     */
    @Override
    public Class<ServerNode> getEntityClass() {
        return ServerNode.class;
    }

    /**
     * 获取视图对象类型
     *
     * @return 视图对象类型
     */
    @Override
    public Class<NodeVo> getVoClass() {
        return NodeVo.class;
    }

    /**
     * 获取数据对象类型
     *
     * @return 数据对象类型
     */
    @Override
    public Class<NodeDto> getDtoClass() {
        return NodeDto.class;
    }
}
