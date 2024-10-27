package com.github.stazxr.zblog.bas.reqsinglepost;

import com.alibaba.fastjson.JSONObject;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

/**
 * RequestPostSingleParam 解析器
 *
 * <p>代码示例
 * <pre>
 *     public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
 *         resolvers.add(new SingleParamHandlerMethodArgumentResolver());
 *         WebMvcConfigurer.super.addArgumentResolvers(resolvers);
 *     }
 * </pre>
 * </p>
 *
 * @author SunTao
 * @since 2022-07-25
 */
@Slf4j
public class SingleParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String POST = "post";

    private static final String APPLICATION_JSON = "application/json";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(RequestPostSingleParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer modelAndViewContainer,
                NativeWebRequest webRequest, @Nullable WebDataBinderFactory webDataBinderFactory) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String contentType = Objects.requireNonNull(servletRequest).getContentType();
        if (contentType == null || !contentType.contains(APPLICATION_JSON)) {
            log.error("《RequestPostSingleParam》 contentType需为【{}】", APPLICATION_JSON);
            throw new RuntimeException("《RequestPostSingleParam》 contentType需为application/json");
        }

        if (!POST.equalsIgnoreCase(servletRequest.getMethod())) {
            log.error("《RequestPostSingleParam》 请求类型必须为post");
            throw new RuntimeException("《RequestPostSingleParam》 请求类型必须为post");
        }

        return this.bindRequestParams(parameter, servletRequest);
    }

    private Object bindRequestParams(MethodParameter parameter, HttpServletRequest servletRequest) {
        RequestPostSingleParam requestPostSingleParam = parameter.getParameterAnnotation(RequestPostSingleParam.class);
        if (requestPostSingleParam == null) {
            throw new RuntimeException("《RequestPostSingleParam》 缺失注解RequestPostSingleParam!");
        }

        Class<?> parameterType = parameter.getParameterType();
        String requestBody = getRequestBody(servletRequest);
        JSONObject paramObj = JSONObject.parseObject(requestBody, JSONObject.class);
        if (paramObj == null) {
            paramObj = new JSONObject();
        }

        String parameterName = StringUtils.isBlank(requestPostSingleParam.value()) ? parameter.getParameterName()
                : requestPostSingleParam.value();

        Object value = paramObj.get(parameterName);
        if (requestPostSingleParam.required()) {
            if (value == null) {
                log.error("《RequestPostSingleParam》 require=true,参数【{}】不能为空!", parameterName);
                throw new RuntimeException("《RequestPostSingleParam》 " + parameterName + "不能为空!");
            }
        }

        return ConvertUtils.convert(value, parameterType);
    }

    private String getRequestBody(HttpServletRequest servletRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = servletRequest.getReader()) {
            char[] buf = new char[1024];
            int length;
            while ((length = reader.read(buf)) != -1) {
                stringBuilder.append(buf, 0, length);
            }
        } catch (IOException e) {
            log.error("《RequestPostSingleParam》 读取流异常", e);
            throw new RuntimeException("《RequestPostSingleParam》 读取流异常");
        }
        return stringBuilder.toString();
    }
}
