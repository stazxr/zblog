package com.github.stazxr.zblog.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json 常用工具集
 *
 * @author SunTao
 * @since 2021-12-12
 */
public class JsonUtils {
    /**
     * parse JsonNode to JSONObject
     *
     * @param jsonNode JsonNode obj
     * @return JSONObject obj
     * @throws JsonProcessingException parse eor
     */
    public static JSONObject jsonNodeToJsonObject(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(jsonNode);
        return JSON.parseObject(jsonStr);
    }

    /**
     * parse jsonStr to JsonNode
     *
     * @param jsonStr json str
     * @return JsonNode obj
     * @throws JsonProcessingException parse eor
     */
    public static JsonNode parseJsonNode(String jsonStr) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonStr);
    }
}
