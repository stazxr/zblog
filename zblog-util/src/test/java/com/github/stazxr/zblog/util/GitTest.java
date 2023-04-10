package com.github.stazxr.zblog.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.mashape.unirest.http.Unirest;

/**
 * 测试github贡献日历接口
 *
 * @author Thomas Sun
 * @since 2022-02-27
 */
public class GitTest {
    private static final String BASE_URL = "https://api.github.com";

    private static final String USER_AGENT = "Mozilla/5.0";

    @Test
    @Ignore
    public void test01() {
        String username = "stazxr";
        String apiUrl = "https://api.github.com/users/" + username + "/events";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
            conn.setRequestProperty("User-Agent", "Java client");
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void test02() throws Exception {
        // String accessToken = "YOUR_ACCESS_TOKEN";
        String username = "stazxr";
        String apiUrl = "https://api.github.com/users/" + username + "/events";
        // String authHeader = "token " + accessToken;

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        // connection.setRequestProperty("Authorization", authHeader);
        connection.setRequestProperty("Accept", "application/vnd.github+json");

        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        Gson gson = new Gson();
        Event[] events = gson.fromJson(reader, Event[].class);

        for (Event event : events) {
            if (event.type.equals("PushEvent")) {
                System.out.println("Push event on " + event.createdAt);
            } else if (event.type.equals("PullRequestEvent")) {
                System.out.println("Pull request event on " + event.createdAt);
            }
        }
    }

    private static class Event {
        @SerializedName("type")
        private String type;
        @SerializedName("created_at")
        private String createdAt;
    }

    @Test
    @Ignore
    public void test03() throws Exception {
        String accessToken = "YOUR_ACCESS_TOKEN";
        String username = "stazxr";

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = today.minusDays(365);
        String startDate = start.format(formatter);
        String endDate = today.format(formatter);
        System.out.println(startDate);
        System.out.println(endDate);
        String url = String.format("https://api.github.com/users/%s/events?per_page=100&page=1&since=%sT00:00:00Z&until=%sT23:59:59Z", username, startDate, endDate);
        String response = getResponse(url, accessToken);
        System.out.println(response);
        int count = getCount(response);
        System.out.println(count);
    }

    private static String getResponse(String urlString, String accessToken) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // connection.setRequestProperty("Authorization", "token " + accessToken);
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
        connection.setRequestMethod("GET");

        Scanner scanner = new Scanner(connection.getInputStream());
        scanner.useDelimiter("\\A");
        String response = scanner.next();

        scanner.close();

        return response;
    }

    private static int getCount(String response) {
        int count = 0;

        String[] events = response.split("\\{\"type\"");
        for (int i = 1; i < events.length; i++) {
            if (events[i].contains("\"PushEvent\"")) {
                count++;
            }
        }

        return count;
    }

    @Test
    @Ignore
    public void test04() throws Exception {
        String username = "stazxr";

        // Set the start and end dates for the contribution calendar
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = today.minusDays(365);
        String startDate = start.format(formatter);
        String endDate = today.format(formatter);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, 2022);
//        calendar.set(Calendar.MONTH, Calendar.JANUARY);
//        calendar.set(Calendar.DAY_OF_MONTH, 1);
//        Date startDate = calendar.getTime();
//        calendar.set(Calendar.YEAR, 2022);
//        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
//        calendar.set(Calendar.DAY_OF_MONTH, 31);
//        Date endDate = calendar.getTime();

        // Make the API request
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", USER_AGENT);
        headers.put("Authorization", "Bearer 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");
        headers.put("Accept", "application/vnd.github.v3+json");
        String url = BASE_URL + "/users/" + username + "/contributions?from=" + startDate + "&to=" + endDate;
        String responseJson = Unirest.get(url)
                .headers(headers)
                .asString()
                .getBody();

        System.out.println(responseJson);

        // Parse the response JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseNode = objectMapper.readTree(responseJson);
        JsonNode contributionsNode = responseNode.get("contributions");

        // Print the contribution data for each day
        for (JsonNode contributionNode : contributionsNode) {
            String date = contributionNode.get("date").asText();
            int count = contributionNode.get("count").asInt();
            System.out.println("Date: " + date + ", Count: " + count);
        }
    }
}
