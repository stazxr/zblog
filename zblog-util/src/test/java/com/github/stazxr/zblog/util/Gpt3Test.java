package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.util.http.ChatAiUtils;
import okhttp3.*;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 测试 ChatGPT API
 *
 * @author Thomas Sun
 * @since 2022-03-06
 */
public class Gpt3Test {
    @Test
    @Ignore
    public void test01() throws IOException {
        String apiKey = "";
        String prompt = "OpenAI的API账户的密钥如何创建";
        String apiUrl = "https://api.openai.com/v1/chat/completions";

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType,
                "{\"prompt\": \"" + prompt + "\", \"max_tokens\": 5}");

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            System.out.println(response.body().string());
        }
    }

    @Test
    @Ignore
    public void test02() {
        String apiKey = "";
        String input = "hha";
        try {
            String response = sendRequest(input, apiKey);
            System.out.println("=============");
            System.out.println(response);
            System.out.println("=============");
            String generatedText = getGeneratedText(response);
            System.out.println(generatedText);
            System.out.println("=============");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void test03() throws IOException {
        String apiKey = "";
        String input = "写一个冒泡排序";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String requestBody = "{\"prompt\":\"" + input + "\",\"max_tokens\":1024,\"temperature\":0.9,\"top_p\":1,\"frequency_penalty\":0.0,\"presence_penalty\":0.6}";
        Request request = new Request.Builder()
                .url(API_ENDPOINT)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(mediaType, requestBody))
                .build();

        Call call = client.newCall(request);
        call.timeout().timeout(180, TimeUnit.SECONDS);
        Response response = call.execute();

        String responseBody = response.body().string();
        System.out.println(responseBody);
    }

    @Test
    @Ignore
    public void test04() throws Exception {
        System.out.println(ChatAiUtils.chatGptApi("你好", ""));
    }

    private static final String API_ENDPOINT = "https://api.openai.com/v1/engines/text-davinci-003/completions";

    public static String sendRequest(String input, String apiKey) throws Exception {
        URL url = new URL(API_ENDPOINT);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10809));
        // HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + apiKey);
        con.setRequestProperty("Content-Type", "application/json");

        // model
        String requestBody = "{\"prompt\": \"" + input + "\", \"max_tokens\": 60}";
        con.setDoOutput(true);
        con.getOutputStream().write(requestBody.getBytes(StandardCharsets.UTF_8));

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public static String getGeneratedText(String response) {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getJSONArray("choices").getJSONObject(0).getString("text");
    }
}
