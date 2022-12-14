package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.converter.ServerNodeConverter;
import com.github.stazxr.zblog.base.domain.bo.Ssh;
import com.github.stazxr.zblog.base.domain.dto.NodeDto;
import com.github.stazxr.zblog.base.domain.dto.query.NodeQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.entity.ServerNode;
import com.github.stazxr.zblog.base.domain.vo.NodeVo;
import com.github.stazxr.zblog.base.mapper.ServerNodeMapper;
import com.github.stazxr.zblog.base.service.ServerNodeService;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.jsch.JschUtils;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * 节点管理业务实现层
 *
 * @author SunTao
 * @since 2022-11-09
 */
@Service
@RequiredArgsConstructor
public class ServerNodeServiceImpl extends ServiceImpl<ServerNodeMapper, ServerNode> implements ServerNodeService {
    private final ServerNodeMapper serverNodeMapper;

    private final ServerNodeConverter serverNodeConverter;

    /**
     * 分页查询节点列表
     *
     * @param queryDto 查询参数
     * @return nodeList
     */
    @Override
    public PageInfo<NodeVo> queryNodeListByPage(NodeQueryDto queryDto) {
        queryDto.checkPage();

        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(serverNodeMapper.selectNodeList(queryDto));
    }

    /**
     * 查询节点详情
     *
     * @param nodeId 节点序列
     * @return NodeVo
     */
    @Override
    public NodeVo queryNodeDetail(Long nodeId) {
        Assert.notNull(nodeId, "参数【nodeId】不能为空");
        NodeVo nodeVo = serverNodeMapper.selectNodeDetail(nodeId);
        Assert.notNull(nodeVo, "查询节点信息失败，节点【" + nodeId + "】不存在");
        return nodeVo;
    }

    /**
     * 新增节点
     *
     * @param nodeDto 节点信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNode(NodeDto nodeDto) {
        nodeDto.setId(null);
        ServerNode serverNode = serverNodeConverter.dtoToEntity(nodeDto);
        checkNode(serverNode);
        Assert.isTrue(serverNodeMapper.insert(serverNode) != 1, "新增失败");
    }

    /**
     * 编辑节点
     *
     * @param nodeDto 节点信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editNode(NodeDto nodeDto) {
        Assert.notNull(nodeDto.getId(), "参数【id】不能为空");
        ServerNode serverNode = serverNodeConverter.dtoToEntity(nodeDto);
        checkNode(serverNode);
        Assert.isTrue(serverNodeMapper.updateById(serverNode) != 1, "修改失败");
    }

    /**
     * 删除节点
     *
     * @param nodeId 节点序列
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNode(Long nodeId) {
        Assert.notNull(nodeId, "参数【nodeId】不能为空");
        Assert.isTrue(serverNodeMapper.deleteById(nodeId) != 1, "删除失败");
    }

    /**
     * 测试 SSH 连通性
     *
     * @param ssh 节点信息
     * @return Boolean
     */
    @Override
    public boolean sshTest(Ssh ssh) {
        Session session = null;
        try {
            session = JschUtils.getSession(ssh.getIp(), ssh.getPort(), ssh.getUser(), ssh.getPassword());
            return session.isConnected();
        } catch (JSchException e) {
            return false;
        } finally {
            JschUtils.closeSession(session);
        }
    }

    private void checkNode(ServerNode node) {
        Assert.isTrue(StringUtils.isBlank(node.getName()), "节点名称不能为空");
        Assert.isTrue(StringUtils.isBlank(node.getIp()), "IP地址不能为空");
        Assert.notNull(node.getPort(), "端口不能为空");
        Assert.isTrue(StringUtils.isBlank(node.getSshUser()), "登录用户不能为空");
        Assert.isTrue(StringUtils.isBlank(node.getSshPwd()), "登录密码不能为空");

        // 检查节点信息是否存在
        ServerNode dbNode = serverNodeMapper.selectByHostAndUser(node.getIp(), node.getSshUser());
        DataValidated.isTrue(dbNode != null && !dbNode.getId().equals(node.getId()), "节点信息已存在");
    }
}
