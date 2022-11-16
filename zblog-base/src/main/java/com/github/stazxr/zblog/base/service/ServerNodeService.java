package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.bo.Ssh;
import com.github.stazxr.zblog.base.domain.dto.NodeDto;
import com.github.stazxr.zblog.base.domain.dto.query.NodeQueryDto;
import com.github.stazxr.zblog.base.domain.entity.ServerNode;
import com.github.stazxr.zblog.base.domain.vo.NodeVo;

/**
 * 节点管理业务层
 *
 * @author SunTao
 * @since 2022-11-09
 */
public interface ServerNodeService extends IService<ServerNode> {
    /**
     * 分页查询节点列表
     *
     * @param queryDto 查询参数
     * @return nodeList
     */
    PageInfo<NodeVo> queryNodeListByPage(NodeQueryDto queryDto);

    /**
     * 查询节点详情
     *
     * @param nodeId 节点序列
     * @return NodeVo
     */
    NodeVo queryNodeDetail(Long nodeId);

    /**
     * 新增节点
     *
     * @param nodeDto 节点信息
     */
    void addNode(NodeDto nodeDto);

    /**
     * 编辑节点
     *
     * @param nodeDto 节点信息
     */
    void editNode(NodeDto nodeDto);

    /**
     * 删除节点
     *
     * @param nodeId 节点序列
     */
    void deleteNode(Long nodeId);

    /**
     * 测试 SSH 连通性
     *
     * @param ssh 节点信息
     * @return Boolean
     */
    boolean sshTest(Ssh ssh);
}
