package com.github.stazxr.zblog.util.wechat;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.stazxr.zblog.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

/**
 * 微信公众号工具类
 *
 * @author Sun Tao
 * @since 2026-11-01
 */
public final class WeChatUtils {
    private static final Logger log = LoggerFactory.getLogger(WeChatUtils.class);

    /**
     * 微信获取 AccessToken 接口
     */
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/stable_token";

    /**
     * 微信上传永久素材接口
     */
    private static final String ADD_MATERIAL_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material";

    /**
     * 微信上传临时素材接口
     */
    private static final String UPLOAD_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/media/upload";

    /**
     * 微信上传图文正文图片接口
     */
    private static final String UPLOAD_IMAGE_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadimg";

    /**
     * 微信新增草稿接口
     */
    private static final String ADD_DRAFT_URL = "https://api.weixin.qq.com/cgi-bin/draft/add";

    /**
     * 连接超时时间
     */
    private static final int CONNECT_TIMEOUT = 5000;

    /**
     * 读取超时时间
     */
    private static final int READ_TIMEOUT = 30000;

    /**
     * 每次读取文件的缓冲区大小
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * Multipart 边界前缀
     */
    private static final String BOUNDARY_PREFIX = "----ZBlogBoundary";

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

        try {
            JSONObject response = requestAccessToken(appId, appSecret);
            String accessToken = response.getString("access_token");
            if (!StringUtils.isBlank(accessToken)) {
                return VerifyResult.success(accessToken, response.getInteger("expires_in"));
            }

            Integer errCode = response.getInteger("errcode");
            String errMsg = response.getString("errmsg");
            return VerifyResult.fail(errCode, getErrorMessage(errCode, errMsg));
        } catch (Exception e) {
            log.error("verify wechat account" + e.getMessage());
            return VerifyResult.fail("微信公众号接口请求失败，请检查网络或稍后重试");
        }
    }

    /**
     * 获取微信公众号 AccessToken
     *
     * @param appId     公众号 AppID
     * @param appSecret 公众号 AppSecret
     * @return 验证结果
     */
    public static VerifyResult getAccessToken(String appId, String appSecret) {
        return verifyAccount(appId, appSecret);
    }

    /**
     * 请求微信 stable_token 接口
     *
     * @param appId     公众号 AppID
     * @param appSecret 公众号 AppSecret
     * @return 微信接口响应
     * @throws Exception 请求异常
     */
    private static JSONObject requestAccessToken(String appId, String appSecret) throws Exception {
        JSONObject request = new JSONObject();
        request.put("grant_type", "client_credential");
        request.put("appid", appId);
        request.put("secret", appSecret);
        request.put("force_refresh", false);
        return requestJson(ACCESS_TOKEN_URL, request);
    }

    /**
     * 上传微信公众号永久素材
     *
     * 适用于图片、语音、视频、缩略图等永久素材。
     *
     * @param accessToken 请求令牌
     * @param type        素材类型：image、voice、video、thumb
     * @param file        上传文件
     * @return 微信接口响应，图片素材包含 media_id、url
     * @throws Exception 上传异常
     */
    public static JSONObject addMaterial(String accessToken, String type, File file) throws Exception {
        checkUploadParams(accessToken, type, file);
        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            return addMaterial(accessToken, type, file.getName(), getContentType(file), inputStream);
        }
    }

    /**
     * 上传微信公众号永久素材
     *
     * @param accessToken 请求令牌
     * @param type        素材类型
     * @param fileName    文件名称
     * @param contentType 文件类型
     * @param inputStream 文件流
     * @return 微信接口响应
     * @throws Exception 上传异常
     */
    public static JSONObject addMaterial(String accessToken, String type, String fileName, String contentType, InputStream inputStream) throws Exception {
        checkUploadParams(accessToken, type, inputStream);
        String requestUrl = ADD_MATERIAL_URL + "?access_token=" + encode(accessToken) + "&type=" + encode(type);
        return requestMultipart(requestUrl, fileName, contentType, inputStream);
    }

    /**
     * 上传微信公众号临时素材
     *
     * 临时素材有效期为3天。
     *
     * @param accessToken 请求令牌
     * @param type        素材类型：image、voice、video、thumb
     * @param file        上传文件
     * @return 微信接口响应，包含 media_id、created_at、type
     * @throws Exception 上传异常
     */
    public static JSONObject uploadMedia(String accessToken, String type, File file) throws Exception {
        checkUploadParams(accessToken, type, file);
        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            return uploadMedia(accessToken, type, file.getName(), getContentType(file), inputStream);
        }
    }

    /**
     * 上传微信公众号临时素材
     *
     * @param accessToken 请求令牌
     * @param type        素材类型
     * @param fileName    文件名称
     * @param contentType 文件类型
     * @param inputStream 文件流
     * @return 微信接口响应
     * @throws Exception 上传异常
     */
    public static JSONObject uploadMedia(String accessToken, String type, String fileName, String contentType, InputStream inputStream) throws Exception {
        checkUploadParams(accessToken, type, inputStream);
        String requestUrl = UPLOAD_MEDIA_URL + "?access_token=" + encode(accessToken) + "&type=" + encode(type);
        return requestMultipart(requestUrl, fileName, contentType, inputStream);
    }

    /**
     * 上传微信公众号正文图片
     *
     * 注意：该接口返回的是图片 URL，不是 media_id。
     *
     * @param accessToken 请求令牌
     * @param file        图片文件
     * @return 微信接口响应，包含 url
     * @throws Exception 上传异常
     */
    public static JSONObject uploadImage(String accessToken, File file) throws Exception {
        if (file == null || !file.isFile()) {
            throw new IllegalArgumentException("上传文件不存在");
        }

        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            return uploadImage(accessToken, file.getName(), getContentType(file), inputStream);
        }
    }

    /**
     * 上传微信公众号正文图片
     *
     * @param accessToken 请求令牌
     * @param fileName    文件名称
     * @param contentType 文件类型
     * @param inputStream 文件流
     * @return 微信接口响应，包含 url
     * @throws Exception 上传异常
     */
    public static JSONObject uploadImage(String accessToken, String fileName, String contentType, InputStream inputStream) throws Exception {
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("AccessToken不能为空");
        }

        if (inputStream == null) {
            throw new IllegalArgumentException("上传文件流不能为空");
        }

        String requestUrl = UPLOAD_IMAGE_URL + "?access_token=" + encode(accessToken);

        return requestMultipart(requestUrl, fileName, contentType, inputStream);
    }

    /**
     * 新增微信公众号草稿
     *
     * @param accessToken 请求令牌
     * @param article     图文消息内容
     * @return 微信接口响应，包含 media_id
     * @throws Exception 请求异常
     */
    public static JSONObject addDraft(String accessToken, JSONObject article) throws Exception {
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("AccessToken不能为空");
        }

        if (article == null) {
            throw new IllegalArgumentException("文章内容不能为空");
        }

        JSONObject request = new JSONObject();
        JSONArray articles = new JSONArray();
        articles.add(article);
        request.put("articles", articles);

        return requestJson(ADD_DRAFT_URL + "?access_token=" + encode(accessToken), request);
    }

    /**
     * 发送 JSON POST 请求
     *
     * @param requestUrl 请求地址
     * @param request    请求参数
     * @return 微信接口响应
     * @throws Exception 请求异常
     */
    private static JSONObject requestJson(String requestUrl, JSONObject request) throws Exception {
        HttpURLConnection connection = null;

        try {
            connection = openConnection(requestUrl);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            byte[] requestBytes = request.toJSONString().getBytes(StandardCharsets.UTF_8);
            connection.setFixedLengthStreamingMode(requestBytes.length);
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(requestBytes);
                outputStream.flush();
            }

            return readJsonResponse(connection);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 发送 Multipart 文件上传请求
     *
     * @param requestUrl 请求地址
     * @param fileName   文件名称
     * @param contentType 文件类型
     * @param inputStream 文件流
     * @return 微信接口响应
     * @throws Exception 上传异常
     */
    private static JSONObject requestMultipart(String requestUrl, String fileName, String contentType, InputStream inputStream) throws Exception {
        if (inputStream == null) {
            throw new IllegalArgumentException("上传文件流不能为空");
        }

        String boundary = BOUNDARY_PREFIX + UUID.randomUUID().toString().replace("-", "");

        HttpURLConnection connection = null;
        try {
            connection = openConnection(requestUrl);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            try (OutputStream outputStream = connection.getOutputStream()) {
                // 文件表单头
                writeString(outputStream, "--" + boundary + "\r\n");
                writeString(outputStream, "Content-Disposition: form-data; name=\"media\"; filename=\"" + safeFileName(fileName) + "\"\r\n");
                writeString(outputStream, "Content-Type: " + safeContentType(contentType) + "\r\n\r\n");

                // 写入文件内容
                copyStream(inputStream, outputStream);

                // Multipart 结束标记
                writeString(outputStream, "\r\n--" + boundary + "--\r\n");

                outputStream.flush();
            }

            return readJsonResponse(connection);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 创建 HTTP 连接
     *
     * @param requestUrl 请求地址
     * @return HTTP 连接
     * @throws Exception 连接异常
     */
    private static HttpURLConnection openConnection(
            String requestUrl) throws Exception {

        URL url = new URL(requestUrl);
        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setUseCaches(false);
        connection.setDoInput(true);

        return connection;
    }

    /**
     * 读取微信 JSON 响应并检查错误码
     *
     * @param connection HTTP 连接
     * @return 微信接口响应
     * @throws Exception 响应异常
     */
    private static JSONObject readJsonResponse(HttpURLConnection connection) throws Exception {
        int responseCode = connection.getResponseCode();
        InputStream inputStream = responseCode >= 200 && responseCode < 300 ? connection.getInputStream() : connection.getErrorStream();
        if (inputStream == null) {
            throw new IllegalStateException("微信接口HTTP请求失败，状态码：" + responseCode);
        }

        String responseText = readResponse(inputStream);
        if (responseCode < 200 || responseCode >= 300) {
            throw new IllegalStateException("微信接口HTTP请求失败，状态码：" + responseCode + "，响应：" + responseText);
        }

        JSONObject response = JSON.parseObject(responseText);
        if (response == null) {
            throw new IllegalStateException("微信接口返回数据格式异常");
        }

        Integer errCode = response.getInteger("errcode");
        if (errCode != null && errCode != 0) {
            String errMsg = response.getString("errmsg");
            throw new WeChatApiException(errCode, getErrorMessage(errCode, errMsg), responseText);
        }

        return response;
    }

    /**
     * 复制文件流
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @throws IOException IO异常
     */
    private static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
    }

    /**
     * 写入 UTF-8 字符串
     *
     * @param outputStream 输出流
     * @param value        字符串
     * @throws IOException IO异常
     */
    private static void writeString(OutputStream outputStream, String value) throws IOException {
        outputStream.write(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 读取接口响应
     *
     * @param inputStream 响应流
     * @return 响应内容
     * @throws Exception 读取异常
     */
    private static String readResponse(InputStream inputStream) throws Exception {
        try (InputStream stream = inputStream;
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
    }

    /**
     * 校验上传参数
     *
     * @param accessToken 请求令牌
     * @param type        素材类型
     * @param file        文件
     */
    private static void checkUploadParams(String accessToken, String type, File file) {
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("AccessToken不能为空");
        }

        if (StringUtils.isBlank(type)) {
            throw new IllegalArgumentException("素材类型不能为空");
        }

        if (file == null || !file.isFile() || !file.canRead()) {
            throw new IllegalArgumentException("上传文件不存在或不可读取");
        }
    }

    /**
     * 校验上传参数
     *
     * @param accessToken 请求令牌
     * @param type        素材类型
     * @param inputStream 文件流
     */
    private static void checkUploadParams(String accessToken, String type, InputStream inputStream) {
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("AccessToken不能为空");
        }

        if (StringUtils.isBlank(type)) {
            throw new IllegalArgumentException("素材类型不能为空");
        }

        if (inputStream == null) {
            throw new IllegalArgumentException("上传文件流不能为空");
        }
    }

    /**
     * 获取文件 MIME 类型
     *
     * @param file 文件
     * @return MIME 类型
     */
    private static String getContentType(File file) {
        try {
            String contentType = Files.probeContentType(file.toPath());
            return StringUtils.isBlank(contentType)  ? "application/octet-stream" : contentType;
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    /**
     * 防止文件名破坏 Multipart 请求格式
     *
     * @param fileName 文件名称
     * @return 安全文件名
     */
    private static String safeFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "upload.bin";
        }

        return fileName.replace("\\", "_")
                .replace("/", "_")
                .replace("\"", "_")
                .replace("\r", "_")
                .replace("\n", "_");
    }

    /**
     * 校验 Content-Type
     *
     * @param contentType 文件类型
     * @return 安全文件类型
     */
    private static String safeContentType(String contentType) {
        if (StringUtils.isBlank(contentType) || contentType.contains("\r") || contentType.contains("\n")) {
            return "application/octet-stream";
        }

        return contentType;
    }

    /**
     * URL 参数编码
     *
     * @param value 参数值
     * @return 编码后的参数
     * @throws Exception 编码异常
     */
    private static String encode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8");
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
            return "微信公众号接口调用失败：" + safeMessage(errMsg);
        }

        switch (errCode) {
            case 40001:
            case 40014:
            case 42001:
                return "AccessToken无效或已过期";
            case 40013:
                return "AppID无效";
            case 40003:
                return "OpenID无效";
            case 40004:
            case 40005:
                return "媒体文件类型不合法";
            case 40006:
            case 40009:
                return "媒体文件大小超过限制";
            case 40007:
                return "媒体文件ID无效";
            case 40125:
                return "AppSecret无效或与AppID不匹配";
            case 40164:
                return "服务器出口IP不在微信公众号接口IP白名单中";
            case 45009:
                return "微信公众号接口调用次数超过限制";
            default:
                return "微信公众号接口调用失败：" + safeMessage(errMsg);
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
     * 微信公众号接口异常
     */
    public static class WeChatApiException extends RuntimeException {
        private static final long serialVersionUID = -5648256316933904585L;

        private final Integer errCode;

        private final String response;

        public WeChatApiException(Integer errCode, String message, String response) {
            super(message);
            this.errCode = errCode;
            this.response = response;
        }

        public Integer getErrCode() {
            return errCode;
        }

        public String getResponse() {
            return response;
        }
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
