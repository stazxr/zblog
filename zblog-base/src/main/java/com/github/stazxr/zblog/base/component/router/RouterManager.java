package com.github.stazxr.zblog.base.component.router;

import com.github.stazxr.zblog.base.domain.bo.RouterInterface;
import com.github.stazxr.zblog.base.domain.entity.Interface;
import com.github.stazxr.zblog.base.domain.enums.InterfaceType;
import com.github.stazxr.zblog.base.service.InterfaceService;
import com.github.stazxr.zblog.base.service.RouterService;
import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.base.domain.entity.Router;
import com.github.stazxr.zblog.core.config.properties.ZblogProperties;
import com.github.stazxr.zblog.util.collection.ArrayUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

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
    private static final String ALL_METHOD = "ANY";

    private static final int BATCH_COUNT = 30;

    private final WebApplicationContext applicationContext;

    private final RouterService routerService;

    private final InterfaceService interfaceService;

    private final ZblogProperties zblogProperties;

    /**
     * 系统启动时，初始化路由信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void initRouter() {
        RouterInterface routerInterface = parseRouter();

        // save router
        List<Router> routerList = routerInterface.getRouters();
        checkRouterCodeUnique(routerList);
        routerService.clearRouter();
        int batchCount = (routerList.size() % BATCH_COUNT) > 0 ? (routerList.size() / BATCH_COUNT) + 1 : routerList.size() / BATCH_COUNT;
        List<List<Router>> routers = ArrayUtils.averageAssign(routerList, batchCount);
        routers.forEach(routerService::saveBatch);

        // save interface
        List<Interface> interfaceList = routerInterface.getPermInterfaces();
        interfaceService.clearInterface();
        interfaceList.addAll(routerInterface.getNullInterfaces());
        batchCount = (interfaceList.size() % BATCH_COUNT) > 0 ? (interfaceList.size() / BATCH_COUNT) + 1 : interfaceList.size() / BATCH_COUNT;
        List<List<Interface>> interfaces = ArrayUtils.averageAssign(interfaceList, batchCount);
        interfaces.forEach(interfaceService::saveBatch);
    }

    /**
     * 检查路由编码的唯一性
     *
     * @param routers 路由列表
     */
    private void checkRouterCodeUnique(List<Router> routers) {
        routers.stream().collect(Collectors.groupingBy(Router::getCode, Collectors.counting())).forEach((code, count) -> {
            if (count > 1) {
                log.error("权限编码重复：{}", code);
            }
        });
    }

    /**
     * 解析所有的路由信息
     *
     * @return 路由列表
     */
    private RouterInterface parseRouter() {
        // 获取并遍历url与类和方法的对应信息
        List<Router> routers = new ArrayList<>();
        List<Interface> permInterfaces = new ArrayList<>();
        List<Interface> nullInterfaces = new ArrayList<>();
        RequestMappingHandlerMapping mapping = applicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            HandlerMethod handlerMethod = m.getValue();
            Method method = handlerMethod.getMethod();
            String className = method.getDeclaringClass().getName();
            if (!className.startsWith(zblogProperties.getBasePackage())) {
                // 非系统本身接口，跳过
                continue;
            }

            // 获取请求地址
            Set<String> requestUris;
            PatternsRequestCondition prc = info.getPatternsCondition();
            if (prc == null || prc.getPatterns().isEmpty()) {
                log.warn("方法 {}.{}() URL Patterns 为空", className, method.getName());
                continue;
            } else {
                requestUris = new HashSet<>(prc.getPatterns());
            }

            // 获取请求方式
            Set<String> requestMethods = new HashSet<>();
            RequestMethodsRequestCondition mrc = info.getMethodsCondition();
            if (mrc.getMethods().size() == 0) {
                // ALL METHOD
                requestMethods.add(ALL_METHOD.toUpperCase(Locale.ROOT));
            } else {
                mrc.getMethods().forEach(requestMethod -> requestMethods.add(requestMethod.toString().toUpperCase(Locale.ROOT)));
            }

            // 获取 Router 注解，如果接口未配置则跳过
            com.github.stazxr.zblog.core.annotation.Router routeInfo = method.getAnnotation(com.github.stazxr.zblog.core.annotation.Router.class);
            if (routeInfo == null) {
                log.warn("方法 {}.{}() 未声明 @Router 注解", className, method.getName());
                pushNullInterface(nullInterfaces, requestUris, requestMethods);
                continue;
            }

            // 封装 router
            com.github.stazxr.zblog.base.domain.entity.Router router = new com.github.stazxr.zblog.base.domain.entity.Router(routeInfo);
            router.setId(GenerateIdUtils.getId());
            routers.add(router);

            // 封装 interface
            for (String uri : requestUris) {
                checkCustomRule(uri);
                for (String requestMethod : requestMethods) {
                    Interface anInterface = new Interface();
                    anInterface.setId(GenerateIdUtils.getId());
                    anInterface.setCode(routeInfo.code());
                    anInterface.setUri(uri);
                    anInterface.setMethod(requestMethod);
                    anInterface.setType(InterfaceType.PERM.getType());
                    permInterfaces.add(anInterface);
                }
            }
        }

        // return
        RouterInterface routerInterface = new RouterInterface();
        routerInterface.setRouters(routers);
        routerInterface.setPermInterfaces(permInterfaces);
        routerInterface.setNullInterfaces(nullInterfaces);
        return routerInterface;
    }

    /**
     * zblog 对路由的自定义规范检查
     *
     * @param uri URI
     */
    private void checkCustomRule(String uri) {
        // 禁止path风格的路由配置，后续想办法解决
        String[] pathVariableLabel = {"{", "}"};
        if (uri.contains(pathVariableLabel[0]) && uri.contains(pathVariableLabel[1])) {
            throw new IllegalStateException("禁止使用PathVariable型URI > " + uri);
        }
    }

    private void pushNullInterface(List<Interface> interfaces, Set<String> uris, Set<String> methods) {
        for (String uri : uris) {
            checkCustomRule(uri);
            for (String method : methods) {
                Interface anInterface = new Interface();
                anInterface.setId(GenerateIdUtils.getId());
                anInterface.setCode(null);
                anInterface.setUri(uri);
                anInterface.setMethod(method);
                anInterface.setType(InterfaceType.NULL.getType());
                interfaces.add(anInterface);
            }
        }
    }
}
