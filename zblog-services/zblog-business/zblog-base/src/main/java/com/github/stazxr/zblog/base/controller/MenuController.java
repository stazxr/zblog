package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.base.domain.vo.MenuVo;
import com.github.stazxr.zblog.base.service.MenuService;
import com.github.stazxr.zblog.core.base.BaseConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单管理
 *
 * @author SunTao
 * @since 2024-12-01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
@Api(value = "MenuController", tags = { "菜单管理" })
public class MenuController {
    private final MenuService menuService;

    /**
     * 查询用户菜单列表（树）
     *
     * @return {@link List<MenuVo>} 返回构建后的用户菜单树
     * @see MenuService#queryUserMenuTree() 用于构建用户菜单树的业务逻辑方法
     */
    @GetMapping(value = "/queryUserMenuTree")
    @ApiOperation(value = "查询用户菜单列表（树）")
    @ApiVersion(BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查询用户菜单列表（树）", code = "MENUQ001", level = RouterLevel.PUBLIC)
    public List<MenuVo> queryUserMenuTree() {
        return menuService.queryUserMenuTree();
    }
}
