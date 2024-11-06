package com.github.stazxr.zblog.base.component.router;

import com.github.stazxr.zblog.bas.context.util.SpringContextUtil;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.base.domain.bo.RouterInterface;
import com.github.stazxr.zblog.base.domain.entity.Interface;
import com.github.stazxr.zblog.base.service.InterfaceService;
import com.github.stazxr.zblog.base.service.RouterService;
import com.github.stazxr.zblog.base.domain.entity.Router;
import com.github.stazxr.zblog.util.collection.ArrayUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 路由管理
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RouterManager {
    private static final String ALL_METHOD = "*";

    private static final int BATCH_COUNT = 30;

    private final RouterService routerService;

    private final InterfaceService interfaceService;

    /**
     * 系统启动时，初始化路由信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void initRouter() {
        // parse router
        RouterInterface routerInterface = parseRouter();

        // check
        List<Router> routerList = routerInterface.getRouters();
        checkRouterCodeUnique(routerList);
        List<Interface> interfaceList = routerInterface.getInterfaces();
        checkInterfaceCodeUnique(interfaceList);

        // save router
        routerService.clearRouter();
        int batchCount = (routerList.size() % BATCH_COUNT) > 0 ? (routerList.size() / BATCH_COUNT) + 1 : routerList.size() / BATCH_COUNT;
        List<List<Router>> routers = ArrayUtils.averageAssign(routerList, batchCount);
        routers.forEach(routerService::saveBatch);

        // save interface
        interfaceService.clearInterface();
        batchCount = (interfaceList.size() % BATCH_COUNT) > 0 ? (interfaceList.size() / BATCH_COUNT) + 1 : interfaceList.size() / BATCH_COUNT;
        List<List<Interface>> interfaces = ArrayUtils.averageAssign(interfaceList, batchCount);
        interfaces.forEach(interfaceService::saveBatch);
    }

    /**
     * 解析所有的路由信息
     *
     * @return 路由列表
     */
    private RouterInterface parseRouter() {
        // 获取并遍历url与类和方法的对应信息
        List<Router> routers = new ArrayList<>();
        List<Interface> interfaces = new ArrayList<>();
        RequestMappingHandlerMapping requestMapping = SpringContextUtil.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> handlerMethodEntry : handlerMethodMap.entrySet()) {
            RequestMappingInfo info = handlerMethodEntry.getKey();
            HandlerMethod handlerMethod = handlerMethodEntry.getValue();
            Method method = handlerMethod.getMethod();
            String className = method.getDeclaringClass().getName();
            String methodName = method.getName();

            // 获取请求地址
            Set<String> requestUris;
            PatternsRequestCondition prc = info.getPatternsCondition();
            if (prc == null || prc.getPatterns().isEmpty()) {
                log.warn("方法 {}:{} URL Patterns 为空", className, methodName);
                continue;
            } else {
                requestUris = new HashSet<>(prc.getPatterns());
            }

            // 获取请求方式
            Set<String> requestMethods = new HashSet<>();
            RequestMethodsRequestCondition mrc = info.getMethodsCondition();
            if (mrc.getMethods().size() == 0) {
                requestMethods.add(ALL_METHOD.toUpperCase(Locale.ROOT));
            } else {
                mrc.getMethods().forEach(requestMethod -> requestMethods.add(requestMethod.name().toUpperCase(Locale.ROOT)));
            }

            // 获取 Router 注解，如果接口未配置则跳过
            com.github.stazxr.zblog.core.annotation.Router routeInfo = method.getAnnotation(com.github.stazxr.zblog.core.annotation.Router.class);

            // 封装 interface
            for (String uri : requestUris) {
                checkCustomRule(uri);
                for (String requestMethod : requestMethods) {
                    Interface anInterface = new Interface();
                    anInterface.setId(SequenceUtils.getId());
                    anInterface.setUri(uri);
                    anInterface.setMethod(requestMethod);
                    if (routeInfo == null) {
                        anInterface.setCode(null);
                    } else {
                        anInterface.setCode(routeInfo.code());
                    }
                    interfaces.add(anInterface);
                }
            }

            // 封装 router
            if (routeInfo != null) {
                com.github.stazxr.zblog.base.domain.entity.Router router = new com.github.stazxr.zblog.base.domain.entity.Router(routeInfo);
                router.setId(SequenceUtils.getId());
                routers.add(router);
            }
        }

        // return
        return new RouterInterface(routers, interfaces);
    }

    /**
     * zblog 对路由的自定义规范检查
     *
     * @param uri URI
     */
    protected void checkCustomRule(String uri) {
        // 禁止path风格的路由配置，后续想办法解决
        String[] pathVariableLabel = {"{", "}"};
        if (uri.contains(pathVariableLabel[0]) && uri.contains(pathVariableLabel[1])) {
            throw new IllegalStateException("禁止使用PathVariable型URI > " + uri);
        }
    }

    /**
     * 检查路由编码的唯一性
     *
     * @param routers 路由列表
     */
    private void checkRouterCodeUnique(List<Router> routers) {
        routers.stream().collect(Collectors.groupingBy(Router::getCode, Collectors.counting())).forEach((code, count) -> {
            if (count > 1) {
                throw new RuntimeException("路由列表检查失败，权限编码配置重复：" + code);
            }
        });
    }

    /**
     * 检查接口编码配置是否冲突（一个接口只能配置一个权限编码）
     *
     * @param interfaceList 接口列表
     */
    private void checkInterfaceCodeUnique(List<Interface> interfaceList) {
        interfaceList.stream().filter(inter -> inter.getCode() != null)
                .collect(Collectors.groupingBy(Interface::getCode, Collectors.counting())).forEach((code, count) -> {
            if (count > 1) {
                throw new RuntimeException("接口列表检查失败，权限编码配置重复：" + code);
            }
        });
    }
}
