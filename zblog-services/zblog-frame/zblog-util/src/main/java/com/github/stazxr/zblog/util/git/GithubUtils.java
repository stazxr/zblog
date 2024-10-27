package com.github.stazxr.zblog.util.git;

import com.github.stazxr.zblog.util.collection.CollectionUtils;
import com.github.stazxr.zblog.util.time.DateUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GITHUB 工具类
 *
 * @author SunTao
 * @since 2023-02-24
 */
public class GithubUtils {
    /**
     * GITHUB 连接
     */
    public static final String BASE_URL = "https://github.com";

    /**
     * 查询 Github 贡献日历数据
     *
     * @param username github 用户名
     * @return data
     */
    public static List<Map<String, Object>> getGithubCalendarData(String username) {
        try {
            String requestUrl = "https://github.com/".concat(username);
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("Content-Type", "application/text");

            try (InputStream is = connection.getInputStream();
                 InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)
            ) {
                String tempLine;
                List<String> data = new ArrayList<>();
                while ((tempLine = reader.readLine()) != null) {
                    if (tempLine.contains("<rect") && tempLine.contains("rect>") && tempLine.contains("data-date")) {
                        data.add(tempLine.trim());
                    }
                }

                // data count
                List<Map<String, Object>> contributions = new ArrayList<>();
                for (String datum : data) {
                    // 解析日期和数量
                    Map<String, Object> dataMap = new HashMap<>(CollectionUtils.mapSize(2));
                    String date = getDateFromRectLabel(datum);
                    int count = getCountFromRectLabel(datum);
                    dataMap.put("date", date);
                    dataMap.put("count", count);
                    contributions.add(dataMap);
                }

                // 数据集
                return contributions;
            }
        } catch (Exception e) {
            throw new IllegalStateException("仓库贡献日历数据获取异常", e);
        }
    }

    private static int getCountFromRectLabel(String datum) {
        int sIndex = datum.indexOf(">");
        int eIndex = datum.indexOf("contribution");
        if (sIndex != -1 && eIndex != -1) {
            String countLabel = datum.substring(sIndex + 1, eIndex).trim();
            final String noCount = "No";
            if (noCount.equalsIgnoreCase(countLabel)) {
                return 0;
            }

            return Integer.parseInt(countLabel);
        }

        return 0;
    }

    private static String getDateFromRectLabel(String datum) {
        int sIndex = datum.indexOf("data-date");
        return datum.substring(sIndex + 11, sIndex + 21);
    }

    private static List<List<Map<String, Object>>> splitDataList(List<Map<String, Object>> dataList) {
        List<List<Map<String, Object>>> result = new ArrayList<>();

        int itemSize = 7;
        List<Map<String, Object>> item = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            item.add(dataList.get(i));

            // 是否满一周
            if (item.size() == itemSize || i == dataList.size() - 1) {
                result.add(item);
                item = new ArrayList<>();
            }
        }

        return result;
    }

    private static String getWeekOfYear(String date) throws ParseException {
        int weekCountOfYear = DateUtils.getWeekCountOfYear(date);
        return weekCountOfYear < 10 ? "0".concat(String.valueOf(weekCountOfYear)) : String.valueOf(weekCountOfYear);
    }

    private static List<List<Map<String, Object>>> groupByDataList(List<Map<String, Object>> dataList) {
        Map<String, List<Map<String, Object>>> tmpMap = dataList.stream().collect(
                Collectors.groupingBy(data -> ((String) data.get("year")).concat((String) data.get("week")))
        );
        Map<String, List<Map<String, Object>>> sortedMap = tmpMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new)
        );

        List<List<Map<String, Object>>> result = new ArrayList<>();
        for (String item : sortedMap.keySet()) {
            result.add(sortedMap.get(item));
        }

        return result;
    }
}
