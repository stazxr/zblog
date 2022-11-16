package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.dto.query.NodeQueryDto;
import com.github.stazxr.zblog.base.domain.entity.ServerNode;
import com.github.stazxr.zblog.base.domain.vo.NodeVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点管理数据持久层
 *
 * @author SunTao
 * @since 2022-11-09
 */
public interface ServerNodeMapper extends BaseMapper<ServerNode> {
    /**
     * 查询节点列表
     *
     * @param queryDto 查询参数
     * @return nodeList
     */
    List<NodeVo> selectNodeList(NodeQueryDto queryDto);

    /**
     * 查询节点详情
     *
     * @param nodeId 节点序列
     * @return NodeVo
     */
    NodeVo selectNodeDetail(@Param("nodeId") Long nodeId);

    /**
     * 根据IP和用户查询节点信息
     *
     * @param ip IP地址
     * @param sshUser 用户信息
     * @return ServerNode
     */
    ServerNode selectByHostAndUser(@Param("ip") String ip, @Param("sshUser") String sshUser);
}
