package com.github.stazxr.zblog.base.manager;

import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.entity.Router;
import com.github.stazxr.zblog.base.service.PermissionService;
import com.github.stazxr.zblog.base.service.RouterService;
import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.util.collection.ArrayUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 路由管理，系统启动时加载一次
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RouterManager {
    /**
     * 路由未纳管
     */
    private static final String STATUS_NONE = "none";

    /**
     * 路由被禁用，只有管理员可以访问
     */
    private static final String STATUS_DISABLED = "disabled";

    /**
     * 路由状态正常
     */
    private static final String STATUS_OK = "ok";

    private final WebApplicationContext applicationContext;

    private final PermissionService permissionService;

    private final RouterService routerService;

    /**
     * 系统启动时，初始化路由信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void initRouter() {
        log.info("start init router...");
        List<Router> routeList = parseRouter();
        log.info("router list: {}", routeList);

        routerService.clearRouter(); // 删除所有
        if (routeList.size() < 100) {
            routerService.saveBatch(routeList); // 批量插入
        } else {
            List<List<Router>> routers = ArrayUtils.averageAssign(routeList);
            routers.forEach(routerService::saveBatch);
        }
        log.info("init router finish...");
    }

    // 解析所有的路由信息
    private List<Router> parseRouter() {
        List<Router> routes = new ArrayList<>();

        // 获取路由白名单
        Map<String, String> whiteList = routerService.getRouterWhiteList();

        // 获取url与类和方法的对应信息
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            HandlerMethod handlerMethod = m.getValue();
            PatternsRequestCondition p = info.getPatternsCondition();
            if (p == null) {
                continue;
            }

            // 获取HTTP URI
            String uri = "";
            for (String pattern : p.getPatterns()) {
                // 默认只有一个URL
                uri = pattern;
            }

            // 禁止path风格的路由配置
            if (uri.contains("{") && uri.contains("}")) {
                throw new IllegalStateException("禁止使用PathVariable型URI > " + uri);
            }

            // 白名单检查
            if (whiteList.containsValue(uri)) {
                log.info("uri [{}] is config in white list", uri);
                continue;
            }

            Method method = handlerMethod.getMethod();
            RequestMapping isReqMapper = method.getAnnotation(RequestMapping.class);
            if (isReqMapper != null) {
                throw new IllegalStateException("系统禁止使用RequestMapping注解 > " + uri);
            }

            // 获取Router注解
            com.github.stazxr.zblog.core.annotation.Router routeInfo = method.getAnnotation(
                com.github.stazxr.zblog.core.annotation.Router.class
            );

            Router router;
            if (routeInfo == null) {
                router = new Router();
                router.setName(method.getName());
                router.setCode(null);
                router.setLevel(BaseConst.PermLevel.PUBLIC); // 登录即可访问
                router.setRemark("系统识别，接口未配置Router注解");
            } else {
                router = new Router(routeInfo);
            }

            // HttpUrl and HttpMethod
            router.setUrl(uri);
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                // 默认一个方法只有一个HttpMethod
                router.setMethod(requestMethod.toString());
                break;
            }

            // 设置路由状态
            Permission permission = permissionService.selectPermByPath(router.getUrl());
            if (permission == null) {
                router.setStatus(STATUS_NONE);
            } else if (!permission.getEnabled()) {
                router.setStatus(STATUS_DISABLED);
                router.setLevel(permission.getLevel());
            } else {
                router.setStatus(STATUS_OK);
                router.setLevel(permission.getLevel());
            }

            router.setId(GenerateIdUtils.getId());
            routes.add(router);
        }
        return routes;
    }
}
