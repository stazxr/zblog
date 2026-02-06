package com.github.stazxr.zblog.bas.rest.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import com.github.stazxr.zblog.bas.exception.support.MessageSupportLoader;
import com.github.stazxr.zblog.bas.rest.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Response 工具类，用于向客户端输出 JSON 响应。
 *
 * @author SunTao
 * @since 2026-01-25
 */
public final class ResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ResponseUtils() {
        // utility class
    }

    /**
     * 将 {@link Result} 对象以 JSON 格式写入 {@link HttpServletResponse}。
     * <p>使用默认的 HTTP 状态码 {@link HttpStatus#OK}。</p>
     *
     * @param response HttpServletResponse 实例
     * @param result   Result 对象，表示响应数据
     * @throws IOException 如果在写入响应时发生 I/O 错误
     */
    public static <T> void responseJsonWriter(HttpServletResponse response, Result<T> result) throws IOException {
        responseJsonWriter(response, result, HttpStatus.OK);
    }

    /**
     * 将 {@link Result} 对象以 JSON 格式写入 {@link HttpServletResponse}，并设置指定的 HTTP 状态码。
     *
     * @param response   HttpServletResponse 实例
     * @param result     Result 对象，表示响应数据
     * @param httpStatus HTTP 状态码
     * @throws IOException 如果在写入响应时发生 I/O 错误
     */
    public static <T> void responseJsonWriter(HttpServletResponse response, Result<T> result, HttpStatus httpStatus) throws IOException {
        try {
            // 防止重复提交响应
            if (response.isCommitted()) {
                log.warn("resubmit error!");
                return;
            }

            ServletServerHttpResponse serverHttpResponse = new ServletServerHttpResponse(response);
            serverHttpResponse.setStatusCode(httpStatus);
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(OBJECT_MAPPER);
            converter.write(result, MediaType.APPLICATION_JSON, serverHttpResponse);
        } catch (Exception e) {
            log.error("Failed to write response JSON", e);
            if (!response.isCommitted()) {
                try {
                    // 兜底返回系统错误 JSON
                    ServletServerHttpResponse serverHttpResponse = new ServletServerHttpResponse(response);
                    serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    String errorCode = ErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
                    String errorMessage = MessageSupportLoader.getMessage("error.system.unknown");
                    Result<Void> fallback = Result.failure(errorCode, errorMessage);
                    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(OBJECT_MAPPER);
                    converter.write(fallback, MediaType.APPLICATION_JSON, serverHttpResponse);
                } catch (Exception ex) {
                    // 这里已经无法返回给客户端，只能记录日志
                    log.error("Fallback response failed", ex);
                }
            }
        }
    }
}
