package com.github.stazxr.zblog.core.util;

import com.github.stazxr.zblog.core.model.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ResponseUtils
 *
 * @author SunTao
 * @since 2020-12-12
 */
public final class ResponseUtils extends com.github.stazxr.zblog.util.servlet.ResponseUtils {
    private ResponseUtils() {
    }

    /**
     * response Json
     *
     * @param response 响应
     * @param result 响应内容
     * @throws IOException catch eor
     */
    public static void responseJsonWriter(HttpServletResponse response, Result result) throws IOException {
        responseJsonWriter(response, result, HttpStatus.OK);
    }

    /**
     * response Json
     *
     * @param response 响应
     * @param result 响应内容
     * @throws IOException catch eor
     */
    public static void responseJsonWriter(HttpServletResponse response, Result result, HttpStatus httpStatus) throws IOException {
        if (response.isCommitted()) {
            return;
        }

        ServletServerHttpResponse serverHttpResponse = new ServletServerHttpResponse(response);
        serverHttpResponse.setStatusCode(httpStatus);
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.write(result, MediaType.APPLICATION_JSON, serverHttpResponse);
    }
}
