package com.github.stazxr.zblog.util.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.TimeValue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * HttpUtils
 *
 *  testURL: http://httpbin.org/get
 *
 * @author SunTao
 * @since 2022-09-06
 */
@Slf4j
public class HttpUtils {
    static CloseableHttpClient httpClient;

    private HttpUtils() {
        throw new IllegalStateException("Utility class");
    }

    static {
        // registry
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        // connectionManager
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(200);
        SocketConfig socket = SocketConfig.custom().setSoTimeout(30, TimeUnit.SECONDS).setTcpNoDelay(true).build();
        connectionManager.setDefaultSocketConfig(socket);
        connectionManager.setValidateAfterInactivity(TimeValue.ofSeconds(30));

        // httpClient
        httpClient = HttpClients.custom().setConnectionManager(connectionManager).disableAutomaticRetries().build();
    }

    public static String get(String url, Map<String, Object> paramMap, Map<String, String> headerMap) {
        String param = paramMap.entrySet().stream().map(n -> n.getKey() + "=" + n.getValue()).collect(Collectors.joining("&"));
        String fullUrl = url + "?" + param;
        final HttpGet httpGet = new HttpGet(fullUrl);
        if (Objects.nonNull(headerMap) && headerMap.size() > 0) {
            headerMap.forEach(httpGet::addHeader);
        }

        int respCode = -1;
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            String strResult = EntityUtils.toString(response.getEntity());
            respCode = response.getCode();
            if (HttpStatus.SC_OK == respCode) {
                return strResult;
            }

            log.error("HTTP get 返回状态非200[resp={}]", strResult);
        } catch (IOException | ParseException e) {
            log.error("HTTP get 异常", e);
        }

        throw new RuntimeException("调用HTTP[GET]请求失败，响应码为：" + respCode);
    }

    public static String post(String url, Map<String, Object> paramMap, Map<String, String> headerMap, String data) {
        String param = paramMap.entrySet().stream().map(n -> n.getKey() + "=" + n.getValue()).collect(Collectors.joining("&"));
        String fullUrl = url + "?" + param;
        final HttpPost httpPost = new HttpPost(fullUrl);
        StringEntity httpEntity = new StringEntity(data, StandardCharsets.UTF_8);
        httpPost.setEntity(httpEntity);
        if (Objects.nonNull(headerMap) && headerMap.size() > 0) {
            headerMap.forEach(httpPost::addHeader);
        }

        int respCode = -1;
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String strResult = EntityUtils.toString(response.getEntity());
            respCode = response.getCode();
            if (HttpStatus.SC_OK == respCode) {
                return strResult;
            }

            log.error("HTTP post 返回状态非200[resp={}]", strResult);
        } catch (IOException | ParseException e) {
            log.error("HTTP post 异常", e);
        }

        throw new RuntimeException("调用HTTP[POST]请求失败，响应码为：" + respCode);
    }
}
