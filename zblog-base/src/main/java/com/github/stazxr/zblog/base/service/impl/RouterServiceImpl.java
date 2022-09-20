package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.dto.query.RouterQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Interface;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.entity.Router;
import com.github.stazxr.zblog.base.domain.enums.InterfaceType;
import com.github.stazxr.zblog.base.domain.vo.RouterVo;
import com.github.stazxr.zblog.base.mapper.InterfaceMapper;
import com.github.stazxr.zblog.base.mapper.RoleMapper;
import com.github.stazxr.zblog.base.mapper.RouterMapper;
import com.github.stazxr.zblog.base.component.security.RouterBlackWhiteListCache;
import com.github.stazxr.zblog.base.service.RouterService;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.util.CacheUtils;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.Constants;
import com.github.stazxr.zblog.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.stazxr.zblog.base.util.Constants.CacheKey.interfaceLevel;
import static com.github.stazxr.zblog.base.util.Constants.SecurityRole.*;

/**
 * 路由业务实现层
 *
 * @author SunTao
 * @since 2021-07-10
 */
@Service
public class RouterServiceImpl extends ServiceImpl<RouterMapper, Router> implements RouterService {
    private static final Set<String> ROLE_OPEN_SET = new HashSet<String>() {{add(OPEN);}};

    private static final Set<String> ROLE_PUBLIC_SET = new HashSet<String>() {{add(PUBLIC);}};

    private static final Set<String> ROLE_FORBIDDEN_SET = new HashSet<String>() {{add(FORBIDDEN);}};

    private static final Set<String> ROLE_NULL_SET = new HashSet<String>() {{add(NULL);}};

    private static final Set<String> ROLE_NONE_SET = new HashSet<String>() {{add(NONE);}};

    @Resource
    private RouterMapper routerMapper;

    @Resource
    private InterfaceMapper interfaceMapper;

    @Resource
    private RoleMapper roleMapper;

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
    public int calculateInterfaceLevel(String requestUri, String requestMethod) {
        requestUri = requestUri.contains(Constants.URL_SPLIT_LABEL) ? requestUri.substring(0, requestUri.indexOf("?")) : requestUri;
        if (RouterBlackWhiteListCache.containsWhite(requestUri)) {
            // 白名单代表允许访问
            return BaseConst.PermLevel.OPEN;
        }

        if (RouterBlackWhiteListCache.containsBlack(requestUri)) {
            // 黑名单代表禁止访问
            return BaseConst.PermLevelExtend.FORBIDDEN;
        }

        // 读取缓存
        String key = interfaceLevel.cacheKey().concat(":").concat(requestUri).concat("_").concat(requestMethod);
        String cacheValue = CacheUtils.get(key);
        if (StringUtils.isNotBlank(cacheValue)) {
            return Integer.parseInt(cacheValue);
        }

        Interface anInterface = interfaceMapper.selectOneByRequest(requestUri, requestMethod.toUpperCase(Locale.ROOT));
        if (anInterface == null) {
            // 访问的接口在系统中不存在，返回匿名，后续校验会自动抛出404
            return BaseConst.PermLevel.OPEN;
        }

        // 判断访问级别
        int level;
        Integer type = anInterface.getType();
        String code = anInterface.getCode();
        if (InterfaceType.NULL.getType().equals(type) || StringUtils.isBlank(code)) {
            // 未配置@Router注解，接口不对外，不允许访问
            level = BaseConst.PermLevelExtend.NULL;
        } else {
            // 根据权限编码查询接口的访问级别
            RouterVo routerVo = routerMapper.selectRouterVoByCode(code);
            if (routerVo.getPermEnabled() != null && !routerVo.getPermEnabled()) {
                // 接口被禁用了
                level = BaseConst.PermLevelExtend.FORBIDDEN;
            } else {
                // 返回真实的接口访问级别
                level = routerVo.getPermLevel();
            }
        }

        // 缓存数据
        CacheUtils.put(key, String.valueOf(level), interfaceLevel.duration());
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
    public Set<String> findRoles(String requestUri, String requestMethod) {
        // 获取访问接口访问级别，返回允许访问的角色集合,这里的目的只是为了减少查询库的次数
        int level = calculateInterfaceLevel(requestUri, requestMethod);
        if (BaseConst.PermLevel.OPEN == level) {
            return ROLE_OPEN_SET;
        } else if (BaseConst.PermLevel.PUBLIC == level) {
            return ROLE_PUBLIC_SET;
        } else if (BaseConst.PermLevelExtend.FORBIDDEN == level) {
            return ROLE_FORBIDDEN_SET;
        } else if (BaseConst.PermLevelExtend.NULL == level) {
            return ROLE_NULL_SET;
        } else {
            // 接口访问级别为BaseConst.PermLevel.PERM，需要查询允许访问该接口的角色列表
            requestUri = requestUri.contains(Constants.URL_SPLIT_LABEL) ? requestUri.substring(0, requestUri.indexOf("?")) : requestUri;
            List<Role> roles = roleMapper.selectRolesByUriAndMethod(requestUri, requestMethod.toUpperCase(Locale.ROOT));
            roles = roles.stream().filter(Role::getEnabled).collect(Collectors.toList());
            if (roles.isEmpty()) {
                return ROLE_NONE_SET;
            }
            Set<String> attributes = new HashSet<>();
            roles.forEach(role -> attributes.add(role.getRoleCode()));
            return attributes;
        }
    }

    /**
     * 根据权限编码查询路由信息
     *
     * @param code 权限编码
     * @return routerVo
     */
    @Override
    public RouterVo queryRouterByCode(String code) {
        Assert.notNull(code, "权限编码不能为空");
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

        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(routerMapper.selectRouterList(queryDto));
    }
}
