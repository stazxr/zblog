//package com.github.stazxr.zblog.base.controller;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * 角色管理
// *
// * @author SunTao
// * @since 2020-11-19
// */
//@Slf4j
//@Controller
//@AllArgsConstructor
//@RequestMapping("/api/roles")
//public class RoleController {
//    /**
//     * 角色编码前缀
//     */
//    private static final String ROLE_PREFIX = "ROLE_";
//
//    private final UserService userService;
//
//    private final RoleService roleService;
//
//    private final PermissionService permissionService;
//
//    private final SecurityManager securityManager;
//
//    private final CacheManager cacheManager;
//
//    /**
//     * 角色管理页面
//     *
//     * @param model Model
//     * @return admin/role.html
//     */
//    @GetMapping("/page")
//    @RouteInfo(desc = "角色管理页面", returnType = RouteReturnType.HTML)
//    public String rolePage(Model model) {
//        model.addAttribute(OperationConst.PAGE, securityManager.hasPerm(PermConst.seeRole));
//        model.addAttribute(OperationConst.LIST, securityManager.hasPerm(PermConst.listRole));
//        model.addAttribute(OperationConst.ADD, securityManager.hasPerm(PermConst.addRole));
//        model.addAttribute(OperationConst.EDIT, securityManager.hasPerm(PermConst.editRole));
//        model.addAttribute(OperationConst.DELETE, securityManager.hasPerm(PermConst.deleteRole));
//        return "admin/role";
//    }
//
//    /**
//     * 获取角色列表
//     *
//     * @param param 查询参数
//     * @return 角色列表
//     */
//    @GetMapping("/list")
//    @ResponseBody
//    @RouteInfo(desc = "查询角色列表")
//    public Result listRole(@RequestParam Map<String, String> param) {
//        QueryModel<Role> query = QueryUtil.buildPageQuery(param);
//        Page<Role> page = roleService.page(query.getPages(), query.getWrapper());
//        PageResult<Role> pageResult = PageUtil.layuiTable(page);
//        return Result.success().data(pageResult);
//    }
//
//    /**
//     * 添加角色页面
//     *
//     * @return admin/roleForm.html
//     */
//    @GetMapping("/add")
//    @RouteInfo(desc = "添加角色页面", returnType = RouteReturnType.HTML)
//    public String addRolePage(Model model) {
//        Role role = new Role();
//        role.setActive(true);
//        model.addAttribute("role", role);
//
//        List<Tree> permissionTree = getPermissionTreeByRoleId(0L);
//        model.addAttribute("permissionTree", permissionTree);
//        model.addAttribute("permissionIds", "");
//        return "admin/roleForm";
//    }
//
//    /**
//     * 添加角色
//     *
//     * @param role 角色信息
//     * @param permissionIds 权限列表
//     * @return Result
//     */
//    @PostMapping("/add")
//    @ResponseBody
//    @RouteInfo(desc = "添加角色")
//    @OperateLog(name = "添加角色", module = "角色管理",
//        type = OperateType.ADD, level = OperateLevel.NOTICE
//    )
//    public Result addRole(Role role, @RequestParam(value = "permissionIds[]", required = false) Long[] permissionIds) {
//        if (!role.getRoleCode().startsWith(ROLE_PREFIX)) {
//            role.setRoleCode(ROLE_PREFIX.concat(role.getRoleCode()));
//        }
//        role.setRoleCode(role.getRoleCode().toUpperCase());
//        if (roleService.selectRoleByCode(role.getRoleCode()) != null) {
//            return Result.failure("角色编码已存在");
//        }
//
//        role.setBuildIn(false);
//        if (roleService.saveRole(role, permissionIds)) {
//            refreshPerms(role.getId());
//            return Result.success();
//        }
//
//        return Result.failure();
//    }
//
//    /**
//     * 编辑角色页面
//     *
//     * @param roleId 角色ID
//     * @return admin/roleForm.html
//     */
//    @GetMapping("/edit")
//    @RouteInfo(desc = "编辑角色页面", returnType = RouteReturnType.HTML)
//    public String editRolePage(Model model, @RequestParam Long roleId) {
//        Role role = roleService.getById(roleId);
//        model.addAttribute("role", role);
//
//        List<Tree> permissionTree = getPermissionTreeByRoleId(roleId);
//        model.addAttribute("permissionTree", permissionTree);
//        String permissionIds = getPermissionCheckIds(permissionTree);
//        model.addAttribute("permissionIds", permissionIds);
//        return "admin/roleForm";
//    }
//
//    /**
//     * 编辑角色
//     *
//     * @param role 角色信息
//     * @param permissionIds 权限列表
//     * @return Result
//     */
//    @PostMapping("/edit")
//    @ResponseBody
//    @RouteInfo(desc = "编辑角色")
//    @OperateLog(name = "编辑角色", module = "角色管理",
//        type = OperateType.UPDATE, level = OperateLevel.WARN
//    )
//    public Result editRole(Role role, @RequestParam(value = "permissionIds[]", required = false) Long[] permissionIds) {
//        if (roleService.getById(role.getId()).getBuildIn()) {
//            return Result.failure("内置角色不允许修改");
//        }
//
//        if (!role.getRoleCode().startsWith(ROLE_PREFIX)) {
//            role.setRoleCode(ROLE_PREFIX.concat(role.getRoleCode()));
//        }
//        role.setRoleCode(role.getRoleCode().toUpperCase());
//        Role dbRole = roleService.selectRoleByCode(role.getRoleCode());
//        if (dbRole != null && !dbRole.getId().equals(role.getId())) {
//            return Result.failure("角色编码已存在");
//        }
//
//        if (roleService.updateRole(role, permissionIds)) {
//            refreshPerms(role.getId());
//            return Result.success();
//        }
//
//        return Result.failure();
//    }
//
//    /**
//     * 删除角色
//     *
//     * @param roleId 角色ID
//     * @return Result
//     */
//    @PostMapping("/delete")
//    @ResponseBody
//    @RouteInfo(desc = "删除角色")
//    @OperateLog(name = "删除角色", module = "角色管理",
//        type = OperateType.UPDATE, level = OperateLevel.WARN
//    )
//    public Result deleteRole(@RequestParam Long roleId) {
//        Role dbRole = roleService.getById(roleId);
//        if (dbRole == null) {
//            return Result.success();
//        }
//
//        if (dbRole.getBuildIn()) {
//            return Result.failure("删除失败，内置角色不允许删除");
//        }
//
//        if (roleService.removeById(roleId)) {
//            refreshPerms(roleId);
//            return Result.success();
//        }
//
//        return Result.failure();
//    }
//
//    private List<Tree> getPermissionTreeByRoleId(Long roleId) {
//        // 获取权限列表
//        List<Permission> allPermissions = permissionService.list();
//
//        // 获取角色对应的权限
//        List<Permission> hasPermissions = permissionService.lambdaQuery()
//                .select(Permission::getId)
//                .inSql(Permission::getId,
//                        "SELECT PERMISSION_ID FROM sys_role_permission_tbl WHERE ROLE_ID=" + roleId
//                ).list();
//
//        // 封装权限树
//        List<Tree> result = new ArrayList<>();
//        for (Permission permission : allPermissions) {
//            Tree tree = new Tree();
//            tree.setId(permission.getId());
//            tree.setName(permission.getName());
//            tree.setpId(permission.getParentId());
//            tree.setOpen(true);
//            tree.setChkDisabled(!permission.getActive());
//
//            for (Permission hasPermission : hasPermissions) {
//                //有权限则选中
//                if (hasPermission.getId().equals(permission.getId())) {
//                    tree.setChecked(true);
//                    break;
//                }
//            }
//            result.add(tree);
//        }
//
//        return result;
//    }
//
//    private void refreshPerms(Long roleId) {
//        // 重新加载角色下所有用户权限
//        List<User> userList = userService.lambdaQuery().select(User::getId)
//                .inSql(User::getId,
//                        "SELECT `USER_ID` FROM sys_user_role_tbl WHERE `ROLE_ID`=" + roleId
//                ).list();
//
//        if (!userList.isEmpty()) {
//            List<String> usernames = new ArrayList<>();
//            for (User user : userList) {
//                usernames.add(user.getUsername());
//            }
//            cacheManager.refreshPerms(usernames);
//        }
//    }
//
//    private String getPermissionCheckIds(List<Tree> permissionTree) {
//        String permissionIds = "";
//        for (Tree tree : permissionTree) {
//            if (tree.isChecked()) {
//                permissionIds = permissionIds.concat(",").concat(String.valueOf(tree.getId()));
//            }
//        }
//
//        if (permissionIds.length() > 0) {
//            permissionIds = permissionIds.substring(1);
//        }
//
//        return permissionIds;
//    }
//}
