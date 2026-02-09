package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.cache.CacheInfo;
import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 缓存管理
 *
 * @author SunTao
 * @since 2026-02-10
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cache")
@Api(value = "CacheController", tags = { "缓存管理" })
public class CacheController {
    /**
     * 模糊查询缓存池数据
     *
     * @return List<CacheInfo>
     */
    @GetMapping("/scan")
    @ApiOperation(value = "模糊查询缓存池数据")
    @ApiVersion(BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "模糊查询缓存池数据", code = "CACHQ001")
    public List<CacheInfo> scan(@RequestParam(required = false) String pattern) {
        return GlobalCache.scan(pattern);
    }

    /**
     * 查看缓存详情
     *
     * @return Value
     */
    @GetMapping("/value")
    @ApiOperation(value = "查看缓存详情")
    @ApiVersion(BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "查看缓存详情", code = "CACHQ002")
    public Object value(@RequestParam String key) {
        return GlobalCache.get(key);
    }

    /**
     * 删除缓存
     */
    @GetMapping("/delete")
    @ApiOperation(value = "删除缓存")
    @ApiVersion(BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "删除缓存", code = "CACHD001")
    public void delete(@RequestParam String key) {
        GlobalCache.remove(key);
    }
}
