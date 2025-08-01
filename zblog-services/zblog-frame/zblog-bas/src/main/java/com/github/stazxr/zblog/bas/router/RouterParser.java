package com.github.stazxr.zblog.bas.router;

import com.github.stazxr.zblog.bas.context.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * RouterParser 负责从 Spring MVC 控制器方法中提取路由信息并将其转化为资源列表
 *
 * @author SunTao
 * @since 2024-11-24
 */
@Slf4j
public class RouterParser {
    /**
     * ANY Method
     */
    public static final String ALL_METHOD = "*";

    /**
     * Error Controller
     */
    private static String errorController = "org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController";

    /**
     * 解析 Spring 请求映射，并返回一个资源列表，每个资源表示一个路由、HTTP 方法及其相关元数据。
     *
     * @return 资源列表
     */
    public static List<Resource> execute() {
        List<Resource> resources = new ArrayList<>();

        // 从 Spring 容器中获取 RequestMappingHandlerMapping bean
        RequestMappingHandlerMapping requestMapping = SpringContextUtil.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);

        // 获取所有的请求处理方法
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMapping.getHandlerMethods();

        // 遍历每个请求处理方法
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();

            // 获取类名和方法名
            Method method = handlerMethod.getMethod();
            String className = method.getDeclaringClass().getName();
            String methodName = method.getName();

            // 错误接口不记录到资源表
            if (errorController.equals(className)) {
                continue;
            }

            // 提取请求 URI
            Set<String> requestUris = extractRequestUris(requestMappingInfo);
            if (requestUris.isEmpty()) {
                log.warn("方法 {}:{} 没有配置 URL Patterns", className, methodName);
                continue;
            }

            // 提取请求方法（如 GET、POST 等）
            Set<String> requestMethods = extractRequestMethods(requestMappingInfo);

            // 获取 Router 注解
            Router router = method.getAnnotation(Router.class);

            // 为每个 URI 创建一个 Resource 对象
            for (String uri : requestUris) {
                checkCustomRule(uri);
                for (String requestMethod : requestMethods) {
                    Resource resource = new Resource();
                    resource.setResourceUri(uri);
                    resource.setResourceMethod(requestMethod);
                    if (router != null) {
                        // 如果有 Router 注解，设置相关字段
                        resource.setResourceName(router.name());
                        resource.setResourceCode(router.code());
                        resource.setResourceLevel(router.level());
                    } else {
                        // 如果没有 Router 注解，设置默认值，默认资源允许登录访问
                        resource.setResourceName(null);
                        resource.setResourceCode(null);
                        resource.setResourceLevel(RouterLevel.PUBLIC);
                    }

                    resources.add(resource);
                }
            }
        }

        // 检查资源编码是否重复
        checkResourceCodeUnique(resources);

        // 返回资源列表
        return resources;
    }

    /**
     * 提取请求的 URI 列表
     *
     * @param requestMappingInfo 请求映射信息
     * @return URI 列表
     */
    private static Set<String> extractRequestUris(RequestMappingInfo requestMappingInfo) {
        Set<String> requestUris = new HashSet<>();
        PatternsRequestCondition prc = requestMappingInfo.getPatternsCondition();
        if (prc != null && !prc.getPatterns().isEmpty()) {
            requestUris.addAll(prc.getPatterns());
        }
        return requestUris;
    }

    /**
     * 提取请求方法（GET、POST 等）
     *
     * @param requestMappingInfo 请求映射信息
     * @return 请求方法集合
     */
    private static Set<String> extractRequestMethods(RequestMappingInfo requestMappingInfo) {
        Set<String> requestMethods = new HashSet<>();
        RequestMethodsRequestCondition mrc = requestMappingInfo.getMethodsCondition();
        if (mrc.getMethods().isEmpty()) {
            requestMethods.add(ALL_METHOD.toUpperCase(Locale.ROOT));
        } else {
            mrc.getMethods().forEach(method -> requestMethods.add(method.name().toUpperCase(Locale.ROOT)));
        }
        return requestMethods;
    }

    /**
     * 检查 URI 是否符合自定义规则
     * 禁止使用路径风格的路由（即包含 PathVariable 的 URI）
     *
     * @param uri URI
     * @throws IllegalStateException 如果 URI 包含路径变量，则抛出异常
     */
    protected static void checkCustomRule(String uri) {
        String[] pathVariableLabel = {"{", "}"};
        if (uri.contains(pathVariableLabel[0]) && uri.contains(pathVariableLabel[1])) {
            throw new IllegalStateException("禁止使用路径变量型 URI > " + uri);
        }
    }

    /**
     * 检查资源代码是否唯一
     *
     * @param resources 资源列表
     * @throws RuntimeException 如果存在重复的资源代码，则抛出异常
     */
    private static void checkResourceCodeUnique(List<Resource> resources) {
        resources.stream().filter(resource -> resource.getResourceCode() != null)
            .collect(Collectors.groupingBy(Resource::getResourceCode, Collectors.counting()))
            .forEach((code, count) -> {
                if (count > 1) {
                    throw new RuntimeException("资源编码重复: " + code);
                }
            });
    }
}
