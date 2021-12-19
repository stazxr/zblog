//package com.github.stazxr.zblog.base.controller;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.github.stazxr.zblog.base.service.RoleService;
//import com.github.stazxr.zblog.base.service.UserService;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cache.CacheManager;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.*;
//
///**
// * 用户管理
// *
// * @author SunTao
// * @since 2020-12-10
// */
//@Slf4j
//@Controller
//@RequestMapping("/api/users")
//@AllArgsConstructor
//public class UserController {
//    private static final String DEFAULT_USER_PWD = "Changeme_123";
//
//    private final UserService userService;
//
//    private final RoleService roleService;
//
//    private final CacheManager cacheManager;
//
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    private final SecurityManager securityManager;
//
//    /**
//     * 用户管理页面
//     *
//     * @param model Model
//     * @return admin/user.html
//     */
//    @GetMapping("/page")
//    @RouteInfo(desc = "用户管理页面", returnType = RouteReturnType.HTML)
//    public String userPage(Model model) {
//        model.addAttribute(OperationConst.PAGE, securityManager.hasPerm(PermConst.seeUser));
//        model.addAttribute(OperationConst.LIST, securityManager.hasPerm(PermConst.listUser));
//        model.addAttribute(OperationConst.ADD, securityManager.hasPerm(PermConst.addUser));
//        model.addAttribute(OperationConst.EDIT, securityManager.hasPerm(PermConst.editUser));
//        model.addAttribute(OperationConst.ONLINE, securityManager.hasPerm(PermConst.onlineUser));
//        model.addAttribute(OperationConst.OFFLINE, securityManager.hasPerm(PermConst.offlineUser));
//        model.addAttribute(OperationConst.DELETE, securityManager.hasPerm(PermConst.deleteUser));
//        return "admin/user";
//    }
//
//    /**
//     * 获取用户列表
//     *
//     * @param param 查询参数
//     * @return 用户列表
//     */
//    @GetMapping("/list")
//    @ResponseBody
//    @RouteInfo(desc = "查询用户列表")
//    public Result listUser(@RequestParam Map<String, String> param) {
//        QueryModel<User> query = QueryUtil.buildPageQuery(param);
//        Page<User> page = userService.page(query.getPages(), query.getWrapper().lambda().orderByDesc(User::getLastLoginTime));
//
//        // 设置用户对应的角色列表
//        List<User> records = page.getRecords();
//        for (User user : records) {
//            List<Role> roleList = roleService.selectRolesByUserId(user.getId());
//            user.setRoles(roleList);
//        }
//        page.setRecords(records);
//
//        // 返回查询信息
//        PageResult<User> pageResult = PageUtil.layuiTable(page);
//        return Result.success().data(pageResult);
//    }
//
//    /**
//     * 添加用户页面
//     *
//     * @return admin/userForm.html
//     */
//    @GetMapping("/add")
//    @RouteInfo(desc = "添加用户页面", returnType = RouteReturnType.HTML)
//    public String addUserPage(Model model) {
//        User user = new User();
//        user.setActive(Boolean.TRUE);
//        model.addAttribute("user", user);
//
//        // 获取角色列表
//        List<XmSelectVo> roles = getXmSelectDataByUserId(null);
//        model.addAttribute("roles", roles);
//        return "admin/userForm";
//    }
//
//    /**
//     * 添加用户
//     *
//     * @param user 用户信息
//     * @param roleIds 角色ID列表
//     * @return Result
//     */
//    @PostMapping("/add")
//    @ResponseBody
//    @RouteInfo(desc = "添加用户")
//    @OperateLog(name = "添加用户", module = "用户管理",
//        type = OperateType.ADD, level = OperateLevel.NOTICE
//    )
//    public Result addUser(User user, @RequestParam(value = "roleIds[]", required = false) Long[] roleIds) {
//        String username = user.getUsername().trim().toUpperCase();
//        if (null != userService.selectUserByUsername(username)) {
//            return Result.failure("用户名已存在");
//        }
//
//        String email = user.getEmail();
//        if (StringUtil.isEmpty(email)) {
//            user.setEmail(null);
//            user.setChangePwd(Boolean.TRUE);
//        } else {
//            if (!RegexUtil.match(user.getEmail(), RegexUtil.EMAIL_REGEX)) {
//                return Result.failure("邮箱格式不正确");
//            }
//
//            if (null != userService.selectUserByEmail(user.getEmail())) {
//                return Result.failure("邮箱已存在");
//            }
//            user.setChangePwd(Boolean.FALSE);
//        }
//
//        user.setUsername(username);
//        user.setNickname(username);
//        user.setPassword(passwordEncoder.encode(DEFAULT_USER_PWD));
//        user.setHeadPortrait(GlobalConst.DEFAULT_USER_IMG);
//        user.setAllowLogin(Boolean.TRUE);
//        user.setBuildIn(Boolean.FALSE);
//        user.setGender(Gender.HIDE);
//        user.setLastChangePwdTime(DateUtil.formatNow());
//        if (userService.saveUser(user, roleIds)) {
//            return Result.success();
//        }
//
//        return Result.failure();
//    }
//
//    /**
//     * 编辑用户页面
//     *
//     * @param userId 用户Id
//     * @return admin/userForm.html
//     */
//    @GetMapping("/edit")
//    @RouteInfo(desc = "编辑用户页面", returnType = RouteReturnType.HTML)
//    public String editUserPage(Model model, Long userId) {
//        User user = userService.getById(userId);
//        model.addAttribute("user", user);
//
//        // 获取角色列表
//        List<XmSelectVo> roles = getXmSelectDataByUserId(userId);
//        model.addAttribute("roles", roles);
//        return "admin/userForm";
//    }
//
//    /**
//     * 编辑用户
//     *
//     * @param user     用户信息
//     * @param roleIds  角色ID列表
//     * @return Result
//     */
//    @PostMapping("/edit")
//    @ResponseBody
//    @RouteInfo(desc = "编辑用户")
//    @OperateLog(name = "编辑用户", module = "用户管理",
//        type = OperateType.UPDATE, level = OperateLevel.WARN
//    )
//    public Result editUser(User user, @RequestParam(value = "roleIds[]", required = false) Long[] roleIds) {
//        String email = user.getEmail();
//        if (StringUtil.isEmpty(email)) {
//            user.setEmail(null);
//        } else {
//            if (!RegexUtil.match(user.getEmail(), RegexUtil.EMAIL_REGEX)) {
//                return Result.failure("邮箱格式不正确");
//            }
//
//            User dbUser = userService.selectUserByEmail(user.getEmail());
//            if (dbUser != null && !dbUser.getId().equals(user.getId())) {
//                return Result.failure("邮箱已存在");
//            }
//        }
//
//        // 用户编辑处，不允许修改用户名
//        user.setUsername(null);
//        if (userService.updateUser(user, roleIds)) {
//            cacheManager.refreshPerms(Collections.singletonList(user.getUsername()));
//            return Result.success();
//        }
//
//        return Result.failure();
//    }
//
//    /**
//     * 禁用用户
//     *
//     * @param userId 用户ID
//     * @return Result
//     */
//    @PostMapping("/offline")
//    @ResponseBody
//    @RouteInfo(desc = "禁用用户")
//    @OperateLog(name = "禁用用户", module = "用户管理",
//        type = OperateType.UPDATE, level = OperateLevel.WARN
//    )
//    public Result offlineUser(@RequestParam Long userId) {
//        User user = userService.getById(userId);
//        String username = user.getUsername();
//        if (GlobalConst.USER_ADMIN.equals(username) || GlobalConst.USER_SYSTEM.equals(username)) {
//            return Result.failure("用户" + username + "不允许被禁用");
//        }
//
//        boolean flag = userService.lambdaUpdate().eq(User::getId, userId).set(User::getActive, false).update();
//        if (flag) {
//            return Result.success();
//        }
//        return Result.failure();
//    }
//
//    /**
//     * 激活用户
//     *
//     * @param userId 用户ID
//     * @return Result
//     */
//    @PostMapping("/online")
//    @ResponseBody
//    @RouteInfo(desc = "激活用户")
//    @OperateLog(name = "激活用户", module = "用户管理",
//            type = OperateType.UPDATE, level = OperateLevel.WARN
//    )
//    public Result onlineUser(@RequestParam Long userId) {
//        boolean flag = userService.lambdaUpdate().eq(User::getId, userId).set(User::getActive, true).update();
//        if (flag) {
//            return Result.success();
//        }
//        return Result.failure();
//    }
//
//    /**
//     * 删除用户
//     *
//     * @param userId 用户Id
//     * @return Result
//     */
//    @PostMapping("/delete")
//    @ResponseBody
//    @RouteInfo(desc = "删除用户")
//    @OperateLog(name = "删除用户", module = "用户管理",
//        type = OperateType.DELETE, level = OperateLevel.RISK
//    )
//    public Result deleteUser(@RequestParam Long userId) {
//        User user = userService.getById(userId);
//        if (user == null) {
//            return Result.success();
//        }
//
//        String username = user.getUsername();
//        if (user.getBuildIn()) {
//            return Result.failure("用户" + username + "不允许删除");
//        }
//
//        if (username.equals(securityManager.getLoginUsername())) {
//            return Result.failure("无法删除自己");
//        }
//
//        if (userService.removeById(userId)) {
//            return Result.success("用户" + username + "删除成功");
//        }
//
//        return Result.failure();
//    }
//
//    private List<XmSelectVo> getXmSelectDataByUserId(Long userId) {
//        // 获取所有非内置的角色列表
//        List<Role> roleList = roleService.lambdaQuery().eq(Role::getBuildIn, false).list();
//
//        // 获取用户对应的角色列表
//        Set<Long> userRoleIds = new HashSet<>();
//        roleService.selectRolesByUserId(userId).forEach(dd -> userRoleIds.add(dd.getId()));
//
//        List<XmSelectVo> result = new ArrayList<>();
//        for (Role role : roleList) {
//            XmSelectVo vo = new XmSelectVo();
//            vo.setName(role.getRoleCode());
//            vo.setValue(String.valueOf(role.getId()));
//            if (userRoleIds.contains(role.getId())) {
//                vo.setSelected(true);
//            }
//            result.add(vo);
//        }
//
//        return result;
//    }
//}
