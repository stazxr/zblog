package com.github.stazxr.zblog.util.wechat;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.stazxr.zblog.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 微信公众号工具类
 *
 * @author Sun Tao
 * @since 2026-10-31
 */
public class WeChatUtils {
    /**
     * 微信获取 AccessToken 接口
     */
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/stable_token";

    /**
     * 连接超时时间
     */
    private static final int CONNECT_TIMEOUT = 5000;

    /**
     * 读取超时时间
     */
    private static final int READ_TIMEOUT = 10000;

    /**
     * 私有构造方法
     */
    private WeChatUtils() {
    }

    /**
     * 验证微信公众号 AppID 和 AppSecret
     *
     * @param appId     公众号 AppID
     * @param appSecret 公众号 AppSecret
     * @return 验证结果
     */
    public static VerifyResult verifyAccount(String appId, String appSecret) {
        if (StringUtils.isBlank(appId)) {
            return VerifyResult.fail("AppID不能为空");
        }

        if (StringUtils.isBlank(appSecret)) {
            return VerifyResult.fail("AppSecret不能为空");
        }

        JSONObject response;
        try {
            response = requestAccessToken(appId, appSecret);
        } catch (Exception e) {
            return VerifyResult.fail("微信公众号接口请求失败，请检查网络或稍后重试");
        }

        if (response == null) {
            return VerifyResult.fail("微信公众号接口返回数据为空");
        }

        String accessToken = response.getString("access_token");
        if (!StringUtils.isBlank(accessToken)) {
            return VerifyResult.success(accessToken, response.getInteger("expires_in"));
        }

        Integer errCode = response.getInteger("errcode");
        String errMsg = response.getString("errmsg");
        return VerifyResult.fail(errCode, getErrorMessage(errCode, errMsg));
    }

    /**
     * 请求微信 AccessToken 接口
     *
     * @param appId     公众号 AppID
     * @param appSecret 公众号 AppSecret
     * @return 微信接口响应
     */
    public static VerifyResult getAccessToken(String appId, String appSecret) {
        return verifyAccount(appId, appSecret);
    }

    /**
     * 请求微信 AccessToken 接口
     *
     * @param appId     公众号 AppID
     * @param appSecret 公众号 AppSecret
     * @return 微信接口响应
     * @throws Exception 请求异常
     */
    private static JSONObject requestAccessToken(String appId, String appSecret) throws Exception {
        String requestUrl = ACCESS_TOKEN_URL
                + "?grant_type=client_credential"
                + "&appid=" + encode(appId)
                + "&secret=" + encode(appSecret);

        HttpURLConnection connection = null;
        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setUseCaches(false);

            int responseCode = connection.getResponseCode();
            InputStream inputStream = responseCode >= 200 && responseCode < 300
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            if (inputStream == null) {
                throw new IllegalStateException("微信接口HTTP请求失败，状态码：" + responseCode);
            }

            String responseText = readResponse(inputStream);
            return JSON.parseObject(responseText);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 读取接口响应
     *
     * @param inputStream 响应流
     * @return 响应内容
     * @throws Exception 读取异常
     */
    private static String readResponse(InputStream inputStream) throws Exception {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        return result.toString();
    }

    /**
     * URL 参数编码
     *
     * @param value 参数值
     * @return 编码后的参数
     * @throws Exception 编码异常
     */
    private static String encode(String value) throws Exception {
        return java.net.URLEncoder.encode(value, "UTF-8");
    }

    /**
     * 获取微信接口错误信息
     *
     * @param errCode 错误码
     * @param errMsg  微信返回错误信息
     * @return 错误描述
     */
    private static String getErrorMessage(Integer errCode, String errMsg) {
        if (errCode == null) {
            return "微信公众号配置验证失败：" + safeMessage(errMsg);
        }

        switch (errCode) {
            case 40013:
                return "AppID无效";
            case 40001:
            case 40125:
                return "AppSecret无效或与AppID不匹配";
            case 40164:
                return "服务器出口IP不在微信公众号接口IP白名单中";
            case 45009:
                return "微信公众号接口调用次数超过限制";
            default:
                return "微信公众号配置验证失败：" + safeMessage(errMsg);
        }
    }

    /**
     * 避免空错误信息
     *
     * @param message 原始错误信息
     * @return 错误信息
     */
    private static String safeMessage(String message) {
        return StringUtils.isBlank(message) ? "未知错误" : message;
    }

    /**
     * 微信公众号配置验证结果
     */
    public static class VerifyResult {
        /**
         * 是否验证成功
         */
        private boolean success;

        /**
         * 错误码
         */
        private Integer errCode;

        /**
         * 提示信息
         */
        private String message;

        /**
         * AccessToken
         */
        private String accessToken;

        /**
         * Token有效时间，单位秒
         */
        private Integer expiresIn;

        public static VerifyResult success(String accessToken, Integer expiresIn) {
            VerifyResult result = new VerifyResult();
            result.success = true;
            result.message = "微信公众号配置验证成功";
            result.accessToken = accessToken;
            result.expiresIn = expiresIn;
            return result;
        }

        public static VerifyResult fail(String message) {
            VerifyResult result = new VerifyResult();
            result.success = false;
            result.message = message;
            return result;
        }

        public static VerifyResult fail(Integer errCode, String message) {
            VerifyResult result = fail(message);
            result.errCode = errCode;
            return result;
        }

        public boolean isSuccess() {
            return success;
        }

        public Integer getErrCode() {
            return errCode;
        }

        public String getMessage() {
            return message;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public Integer getExpiresIn() {
            return expiresIn;
        }
    }
}