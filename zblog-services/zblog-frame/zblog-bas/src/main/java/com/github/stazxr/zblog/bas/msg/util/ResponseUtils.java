package com.github.stazxr.zblog.bas.msg.util;

import com.github.stazxr.zblog.bas.msg.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ResponseUtils 提供工具方法用于向客户端发送 JSON 响应。
 *
 * @author SunTao
 * @since 2024-11-12
 */
@Slf4j
public final class ResponseUtils {
    private ResponseUtils() {
    }

    /**
     * 将 {@link Result} 对象以 JSON 格式写入 {@link HttpServletResponse}。
     * <p>使用默认的 HTTP 状态码 {@link HttpStatus#OK}。</p>
     *
     * @param response HttpServletResponse 实例
     * @param result   Result 对象，表示响应数据
     * @throws IOException 如果在写入响应时发生 I/O 错误
     */
    public static void responseJsonWriter(HttpServletResponse response, Result result) throws IOException {
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
    public static void responseJsonWriter(HttpServletResponse response, Result result, HttpStatus httpStatus) throws IOException {
        // 防止重复提交响应
        if (response.isCommitted()) {
            log.warn("resubmit error!");
            return;
        }

        ServletServerHttpResponse serverHttpResponse = new ServletServerHttpResponse(response);
        serverHttpResponse.setStatusCode(httpStatus);
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.write(result, MediaType.APPLICATION_JSON, serverHttpResponse);
    }
}
