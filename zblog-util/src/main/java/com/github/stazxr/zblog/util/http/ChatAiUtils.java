package com.github.stazxr.zblog.util.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 聊天助手 API
 *
 * @author SunTao
 * @since 2023-03-06
 */
public class ChatAiUtils {
    // TODO 由于 chatGPT API 突然无法访问，功能暂时搁置

    private static final String CHAT_GPT_API_3_5 = "https://api.openai.com/v1/engines/text-davinci-003/completions";

    private static final String PROXY_SERVER_IP = "127.0.0.1";

    private static final int PROXY_SERVER_PORT = 10809;

    /**
     * 调用 chatgpt api 获取对话内容
     *
     * @param content 对话内容
     * @param apiKey  API KEY
     * @return 回答
     */
    public static String chatGptApi(String content, String apiKey) throws Exception {
        try {
            // 创建 URL
            URL url = new URL(CHAT_GPT_API_3_5);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_SERVER_IP, PROXY_SERVER_PORT));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setDoOutput(true);

            // 自定义参数
            String jsonParam = "{\"prompt\": \"" + content + "\", \"max_tokens\": " + 150 + ", \"temperature\": " + 0.7 + ", \"n\": " + 1 + ", \"stop\": \"" + "\n" + "\"}";
            OutputStream os = conn.getOutputStream();
            byte[] input = jsonParam.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

            StringBuilder response;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            return response.toString();
        } catch (Exception e) {
            throw new Exception("发生错误", e);
        }
    }
}
