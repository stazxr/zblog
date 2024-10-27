package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.bo.Ssh;
import com.github.stazxr.zblog.base.domain.dto.NodeDto;
import com.github.stazxr.zblog.base.domain.dto.query.NodeQueryDto;
import com.github.stazxr.zblog.base.service.ServerNodeService;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 节点管理
 *
 * @author SunTao
 * @since 2022-11-09
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/node")
@Api(value = "ServerNodeController", tags = { "节点控制器" })
public class ServerNodeController {
    private final ServerNodeService serverNodeService;

    /**
     * 分页查询节点列表
     *
     * @param queryDto 查询参数
     * @return nodeList
     */
    @GetMapping(value = "/pageList")
    @ApiOperation("分页查询节点列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询节点列表", code = "queryNodeListByPage")
    public Result queryNodeListByPage(NodeQueryDto queryDto) {
        return Result.success().data(serverNodeService.queryNodeListByPage(queryDto));
    }

    /**
     * 查询节点详情
     *
     * @param nodeId 节点序列
     * @return NodeVo
     */
    @GetMapping(value = "/queryNodeDetail")
    @ApiOperation("分页查询节点列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "nodeId", value = "节点id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "查询节点详情", code = "queryNodeDetail", level = BaseConst.PermLevel.PUBLIC)
    public Result queryNodeDetail(@RequestParam Long nodeId) {
        return Result.success().data(serverNodeService.queryNodeDetail(nodeId));
    }

    /**
     * 新增节点
     *
     * @param nodeDto 节点信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/addNode")
    @ApiOperation("新增节点")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "新增节点", code = "addNode")
    public Result addNode(@RequestBody NodeDto nodeDto) {
        serverNodeService.addNode(nodeDto);
        return Result.success();
    }

    /**
     * 编辑节点
     *
     * @param nodeDto 节点信息
     * @return Result
     */
    @Log
    @PostMapping(value = "/editNode")
    @ApiOperation("编辑节点")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "编辑节点", code = "editNode")
    public Result editNode(@RequestBody NodeDto nodeDto) {
        serverNodeService.editNode(nodeDto);
        return Result.success();
    }

    /**
     * 删除节点
     *
     * @param nodeId 节点序列
     * @return Result
     */
    @Log
    @PostMapping(value = "/deleteNode")
    @ApiOperation("删除节点")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "nodeId", value = "节点id", required = true, dataTypeClass = Long.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除节点", code = "deleteNode")
    public Result deleteNode(@RequestParam Long nodeId) {
        serverNodeService.deleteNode(nodeId);
        return Result.success();
    }

    /**
     * 测试 SSH 连通性
     *
     * @param ssh 节点信息
     * @return Result
     */
    @PostMapping(value = "/sshTest")
    @ApiOperation("测试节点 SSH 连通性")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "测试节点 SSH 连通性", code = "sshTest", level = BaseConst.PermLevel.PUBLIC)
    public Result sshTest(@RequestBody Ssh ssh) {
        boolean flag = serverNodeService.sshTest(ssh);
        return flag ? Result.success("连接成功") : Result.failure("连接失败");
    }
}
