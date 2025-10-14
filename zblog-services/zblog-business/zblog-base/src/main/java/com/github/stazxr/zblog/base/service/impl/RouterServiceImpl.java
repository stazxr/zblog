package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.base.converter.DictConverter;
import com.github.stazxr.zblog.base.domain.dto.DictDto;
import com.github.stazxr.zblog.base.domain.dto.RouterDto;
import com.github.stazxr.zblog.base.domain.dto.query.RouterQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Dict;
import com.github.stazxr.zblog.base.domain.entity.Router;
import com.github.stazxr.zblog.base.domain.enums.DictType;
import com.github.stazxr.zblog.base.domain.vo.DictVo;
import com.github.stazxr.zblog.base.domain.vo.RouterVo;
import com.github.stazxr.zblog.base.mapper.DictMapper;
import com.github.stazxr.zblog.base.mapper.InterfaceMapper;
import com.github.stazxr.zblog.base.mapper.RoleMapper;
import com.github.stazxr.zblog.base.mapper.RouterMapper;
import com.github.stazxr.zblog.base.service.RouterService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.stazxr.zblog.base.util.Constants.SecurityRole.*;

/**
 * 路由业务实现层
 *
 * @author SunTao
 * @since 2021-07-10
 */
@Service
@RequiredArgsConstructor
public class RouterServiceImpl extends ServiceImpl<RouterMapper, Router> implements RouterService {
    private final RouterMapper routerMapper;

    private final InterfaceMapper interfaceMapper;

    private final DictMapper dictMapper;

    private final DictConverter dictConverter;

    /**
     * 移除所有的路由信息
     */
    @Override
    public void clearRouter() {
        routerMapper.clearRouter();
    }

    /**
     * 计算接口的访问级别
     *
     * @param requestUri    请求地址
     * @param requestMethod 请求方式
     * @return 访问级别
     */
    @Override
    @Deprecated
    public int calculateInterfaceLevel(String requestUri, String requestMethod) {
        // 处理请求地址和请求方式
        final String whLabel = "?";
        requestUri = requestUri.contains(whLabel) ? requestUri.substring(0, requestUri.indexOf(whLabel)) : requestUri;
        requestMethod = requestMethod.toUpperCase(Locale.ROOT);

        // 从缓存中读取接口访问级别
        BaseConst.GlobalCacheKey intLevelKey = BaseConst.GlobalCacheKey.interfaceLevel;
        String intLevelCacheKey = String.format(intLevelKey.cacheKey(), requestUri, requestMethod, Locale.ROOT);
        String interfaceLevel = GlobalCache.get(intLevelCacheKey);
        if (StringUtils.isNotBlank(interfaceLevel)) {
            return Integer.parseInt(interfaceLevel);
        }

        // 查询接口的权限编码
        String permCode = interfaceMapper.selectCodeByUriAndMethod(requestUri, requestMethod);
        if (permCode == null) {
            // 未配置 @Router 注解，接口默认登录可访问
            return RouterLevel.OPEN;
        }

        // 根据权限编码查询路由信息
        int level;
        RouterVo routerVo = routerMapper.selectRouterVoByCode(permCode);
        if (routerVo == null) {
            level = BaseConst.PermLevelExtend.NULL;
        } else {
            if (routerVo.getPermEnabled() != null && !routerVo.getPermEnabled()) {
                // 接口被禁用了
                level = BaseConst.PermLevelExtend.FORBIDDEN;
            } else {
                // 返回真实的接口访问级别
                level = routerVo.getPermLevel();
            }
        }

        // 缓存数据
        GlobalCache.put(intLevelCacheKey, String.valueOf(level), intLevelKey.duration());
        return level;
    }

    /**
     * 根据请求地址查询允许访问的角色列表
     *
     * @param requestUri    请求地址
     * @param requestMethod 请求方式
     * @return allowed roles
     */
    @Override
    @Deprecated
    public Set<String> findRoles(String requestUri, String requestMethod) {
        return null;
    }

    /**
     * 根据权限编码查询路由信息
     *
     * @param code 权限编码
     * @return routerVo
     */
    @Override
    public RouterVo queryRouterByCode(String code) {
        Assert.isTrue(StringUtils.isBlank(code), "参数【code】不能为空");
        return routerMapper.selectRouterVoByCode(code);
    }

    /**
     * 分页查询路由列表
     *
     * @param queryDto 查询参数
     * @return routerList
     */
    @Override
    public PageInfo<RouterVo> queryRouterListByPage(RouterQueryDto queryDto) {
        queryDto.checkPage();
        try (Page<RouterVo> page = PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize())) {
            return page.doSelectPageInfo(() -> routerMapper.selectRouterList(queryDto));
        }
    }

    /**
     * 分页查询黑白名单列表
     *
     * @param queryDto 查询参数
     * @return blackOrWhiteList
     */
    @Override
    public PageInfo<DictVo> pageBlackOrWhiteList(RouterQueryDto queryDto) {
        Assert.notNull(queryDto.getDictKey(), "参数【dictKey】不能为空");
        queryDto.checkPage();
        try (Page<DictVo> page = PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize())) {
            return page.doSelectPageInfo(() -> routerMapper.selectBlackOrWhiteList(queryDto));
        }
    }

    /**
     * 更新接口的日志展示状态
     *
     * @param routerDto 路由信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLogShowStatus(RouterDto routerDto) {
        Assert.notNull(routerDto.getUri(), "参数【uri】不能为空");
        Assert.notNull(routerDto.getMethod(), "参数【method】不能为空");
        Assert.notNull(routerDto.getLogShowed(), "参数【logShowed】不能为空");

        String uri = routerDto.getUri();
        String method = routerDto.getMethod();
        boolean logShowed = routerDto.getLogShowed();
        String username = SecurityUtils.getLoginUsername();
        Boolean flag = routerMapper.selectLogShowFlag(uri, method);
        int i;
        if (flag == null) {
            i = routerMapper.insertLogShowFlag(uri, method, logShowed, username, DateUtils.formatNow());
        } else {
            i = routerMapper.updateLogShowFlag(uri, method, logShowed, username, DateUtils.formatNow());
        }

        Assert.isTrue(i != 1, "操作失败");
    }

    /**
     * 新增黑白名单
     *
     * @param dictDto 字典信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBlackOrWhiteRouter(DictDto dictDto) {
        Dict dict = dictConverter.dtoToEntity(dictDto);
        checkDict(dict);

        dict.setId(SequenceUtils.getId());
        dict.setPid(BaseConst.DictKey.ROUTER_WHITE_LIST.equals(dict.getKey()) ? 2L : 3L);
        dict.setType(DictType.ITEM.getValue());
        dict.setEnabled(true);
        dict.setLocked(true);
        Assert.isTrue(dictMapper.insert(dict) != 1, "新增失败");
        removeCache(dictDto.getValue());
    }

    /**
     * 编辑黑白名单
     *
     * @param dictDto 字典信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editBlackOrWhiteRouter(DictDto dictDto) {
        Dict dict = dictConverter.dtoToEntity(dictDto);
        checkDict(dict);
        Assert.isTrue(dictMapper.updateById(dict) != 1, "编辑失败");
        removeCache(dictDto.getValue());
    }

    /**
     * 修改黑白名单状态
     *
     * @param dictDto 字典信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeBlackOrWhiteRouterStatus(DictDto dictDto) {
        Assert.notNull(dictDto.getDictId(), "参数【dictId】不能为空");
        Assert.notNull(dictDto.getEnabled(), "参数【enabled】不能为空");
        Assert.isTrue(dictMapper.updateDictStatus(dictDto.getDictId(), dictDto.getEnabled()) != 1, "修改失败");
        Dict dict = dictMapper.selectById(dictDto.getDictId());
        removeCache(dict.getValue());
    }

    /**
     * 删除黑白名单
     *
     * @param dictId 字典序列
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBlackOrWhiteRouter(Long dictId) {
        Assert.notNull(dictId, "参数【dictId】不能为空");
        Dict dbDict = dictMapper.selectById(dictId);
        int i = dictMapper.deleteById(dictId);
        Assert.isTrue(i != 1, "删除失败");
        removeCache(dbDict.getValue());
    }

    private void checkDict(Dict dict) {
        Assert.notNull(dict.getName(), "接口名称不能为空");
        Assert.notNull(dict.getKey(), "接口类型不能为空");
        Assert.notNull(dict.getValue(), "接口地址不能为空");
        Assert.notNull(dict.getSort(), "接口排序不能为空");

        boolean typeFlag = !BaseConst.DictKey.ROUTER_WHITE_LIST.equals(dict.getKey()) && !BaseConst.DictKey.ROUTER_BLACK_LIST.equals(dict.getKey());
        Assert.isTrue(typeFlag, "接口类型不正确：" + dict.getKey());
    }

    private void removeCache(String url) {
        Constants.SysCacheKey interfaceLevelKey = Constants.SysCacheKey.interfaceLevel;
        String getKey = String.format(interfaceLevelKey.cacheKey(), url.concat("_GET"), Locale.ROOT);
        String postKey = String.format(interfaceLevelKey.cacheKey(), url.concat("_POST"), Locale.ROOT);
        String putKey = String.format(interfaceLevelKey.cacheKey(), url.concat("_PUT"), Locale.ROOT);
        String deleteKey = String.format(interfaceLevelKey.cacheKey(), url.concat("_DELETE"), Locale.ROOT);
        GlobalCache.remove(getKey, postKey, putKey, deleteKey);
    }
}
