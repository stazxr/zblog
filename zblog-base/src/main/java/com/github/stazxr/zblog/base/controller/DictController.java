//package com.github.stazxr.zblog.base.controller;
//
//import com.github.stazxr.zblog.base.service.DictService;
//import io.swagger.annotations.Api;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
///**
// * 字典管理
// *
// * @author SunTao
// * @since 2021-02-20
// */
//@Slf4j
//@Validated
//@RestController
//@AllArgsConstructor
//@Api(tags = "系统：字典管理")
//@RequestMapping("/api/dict")
//public class DictController {
//    private final DictService dictService;
//    /**
//     * 查询字典列表
//     *
//     * @param param 查询参数
//     * @return 字典列表
//     */
//    @GetMapping("/list")
//    @ResponseBody
//    @RouteInfo(desc = "查询字典列表")
//    public Result listVariable(@RequestParam Map<String, String> param) {
//        QueryModel<Variable> query = QueryUtil.buildPageQuery(param);
//        Page<Variable> page = variableService.page(query.getPages(), query.getWrapper());
//        PageResult<Variable> pageResult = PageUtil.layuiTable(page);
//        return Result.success().data(pageResult);
//    }
//
//    /**
//     * 查询字典项列表
//     *
//     * @param pid 父ID
//     * @return 字典项列表
//     */
//    @GetMapping("/listItem")
//    @ResponseBody
//    @RouteInfo(desc = "查询字典项列表")
//    public Result listItemVariable(@RequestParam Long pid) {
//        return Result.success().data(
//            variableService.lambdaQuery().eq(Variable::getPid, pid).orderByAsc(Variable::getOrder).list()
//        );
//    }
//
//    /**
//     * 添加字典
//     *
//     * @param variable 字典信息
//     * @return Result
//     */
//    @PostMapping("/add")
//    @ResponseBody
//    @RouteInfo(desc = "添加字典")
//    @OperateLog(name = "添加字典", module = "字典管理",
//        type = OperateType.ADD, level = OperateLevel.NOTICE
//    )
//    public Result addVariable(Variable variable) {
//        if (StringUtil.hasBlank(variable.getName(), variable.getKey())) {
//            return Result.failure("Key或名称不能为空");
//        }
//
//        variable.setType(VariableType.ITEM.value());
//        if (variable.getPid() == null) {
//            // 组，需要校验Key是否存在
//            if (variableService.get(variable.getKey()) != null) {
//                return Result.failure("该组已存在");
//            }
//            variable.setType(VariableType.GROUP.value());
//            variable.setOrder(0);
//        }
//
//        variable.setName(variable.getName().trim());
//        variable.setKey(variable.getKey().toUpperCase().trim());
//        variable.setValue(variable.getValue().trim());
//        return variableService.save(variable) ? Result.success() : Result.failure();
//    }
//
//    /**
//     * 编辑字典
//     *
//     * @param variable 字典信息
//     * @return Result
//     */
//    @PostMapping("/edit")
//    @ResponseBody
//    @RouteInfo(desc = "编辑字典")
//    @OperateLog(name = "编辑字典", module = "字典管理",
//        type = OperateType.UPDATE, level = OperateLevel.RISK
//    )
//    public Result editVariable(Variable variable) {
//        if (StringUtil.hasBlank(variable.getName(), variable.getKey())) {
//            return Result.failure("Key或名称不能为空");
//        }
//
//        Variable dbVariable = variableService.getById(variable.getId());
//        if (!dbVariable.getEnableOperate()) {
//            return Result.failure("该字典值不允许编辑");
//        }
//
//        if (variable.getPid() == null) {
//            // 组，需要校验Key是否存在
//            dbVariable = variableService.get(variable.getKey());
//            if (dbVariable != null && !dbVariable.getId().equals(variable.getId())) {
//                return Result.failure("该组已存在");
//            }
//        }
//
//        variable.setName(variable.getName().trim());
//        variable.setKey(variable.getKey().toUpperCase().trim());
//        variable.setValue(variable.getValue().trim());
//        return variableService.updateById(variable) ? Result.success() : Result.failure();
//    }
//
//    /**
//     * 删除字典
//     *
//     * @param variableId 字典ID
//     * @return Result
//     */
//    @PostMapping("/delete")
//    @ResponseBody
//    @RouteInfo(desc = "删除字典")
//    @OperateLog(name = "删除字典", module = "字典管理",
//        type = OperateType.DELETE, level = OperateLevel.RISK
//    )
//    public Result deleteVariable(@RequestParam Long variableId) {
//        Variable dbVariable = variableService.getById(variableId);
//        if (!dbVariable.getEnableOperate()) {
//            return Result.failure("该字典不允许删除");
//        }
//
//        if (variableService.removeById(variableId)) {
//            if (VariableType.GROUP.value().equals(dbVariable.getType())) {
//                // 如果类型是组，则同时删除所有的字典项
//                variableService.lambdaUpdate().eq(Variable::getPid, variableId).remove();
//            }
//            return Result.success();
//        }
//
//        return Result.failure();
//    }
//}
