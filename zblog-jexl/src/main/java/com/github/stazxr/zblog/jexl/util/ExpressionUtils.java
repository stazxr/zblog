package com.github.stazxr.zblog.jexl.util;

import com.github.stazxr.zblog.jexl.function.ListHandle;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.collection.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 表达式工具类
 *
 * @author SunTao
 * @since 2022-05-23
 */
@Slf4j
public class ExpressionUtils {
    private static final String DATA_ERROR = "数据错误";

    private static volatile JexlEngine engine = null;

    private ExpressionUtils() {
    }

    private static JexlEngine getJexlEngine() {
        if (engine == null) {
            synchronized(ExpressionUtils.class) {
                if (engine == null) {
                    engine = (new JexlBuilder()).create();
                }
            }
        }

        return engine;
    }


    public static Object executeScript(String script) {
        try {
            return calculateScript(null, script, null, null);
        } catch (Exception e) {
            log.error("数据计算错误：{}", script, e);
            return null;
        }
    }

    public static void executeScript(String script, Map<String, Object> data) {
        Map<String, String> newVarMap = variableRestructure(data.keySet());
        String newExpression = expressionRestructure(script, newVarMap);

        try {
            calculateScript(newVarMap, newExpression, data, null);
        } catch (Exception e) {
            log.error("数据计算错误：{}, {}, {}", script, newExpression, data, e);
            data.put("result", DATA_ERROR);
        }

        ListHandle.removeOriginData();
        ListHandle.removeKeys();
    }

    public static void executeExpression(String expression, Map<String, Object> data) {
        Map<String, String> newVarMap = variableRestructure(data.keySet());
        String newExpression = expressionRestructure(expression, newVarMap);

        try {
            calculateExpression(newVarMap, newExpression, data, null);
        } catch (Exception e) {
            log.error("数据计算错误：{}, {}, {}", expression, newExpression, data, e);
            data.put("result", DATA_ERROR);
        }

        ListHandle.removeOriginData();
        ListHandle.removeKeys();
    }


    public static void batchExecuteExpression(List<Map<String, Object>> dataList) {
        Map<String, String> newVarMap = variableRestructure(dataList.get(0).keySet());
        ListHandle.setOriginData(dataList);

        for (Map<String, Object> stringObjectMap : dataList) {
            try {
                String newExpression = expressionRestructure(stringObjectMap.get("expression").toString(), newVarMap);
                calculateExpression(newVarMap, newExpression, stringObjectMap, null);
            } catch (Exception var5) {
                stringObjectMap.put("result", "数据错误");
            }
        }

        ListHandle.removeOriginData();
        ListHandle.removeKeys();
    }

    public static void executeExpression(String expression, List<Map<String, Object>> dataList) {
        Map<String, String> newVarMap = variableRestructure(dataList.get(0).keySet());
        String newExpression = expressionRestructure(expression, newVarMap);
        ListHandle.setOriginData(dataList);

        for (Map<String, Object> stringObjectMap : dataList) {
            try {
                calculateExpression(newVarMap, newExpression, stringObjectMap, null);
            } catch (Exception var7) {
                stringObjectMap.put("result", "数据错误");
            }
        }

        ListHandle.removeOriginData();
        ListHandle.removeKeys();
    }

    public static void executeScript(String script, List<Map<String, Object>> dataList) {
        Map<String, String> newVarMap = variableRestructure(dataList.get(0).keySet());
        String newExpression = expressionRestructure(script, newVarMap);
        ListHandle.setOriginData(dataList);

        for (Map<String, Object> stringObjectMap : dataList) {
            try {
                calculateScript(newVarMap, newExpression,  stringObjectMap, null);
            } catch (Exception e) {
                log.error("e", e);
                stringObjectMap.put("result", "数据错误");
            }
        }

        ListHandle.removeOriginData();
        ListHandle.removeKeys();
    }

    public static void executeScript(String script, List<Map<String, Object>> dataList, Map<String, String> relation) {
        String newExpression = expressionRestructure(script, relation);
        ListHandle.setOriginData(dataList);

        for (Map<String, Object> stringObjectMap : dataList) {
            try {
                calculateScript(relation, newExpression, stringObjectMap, null);
            } catch (Exception var7) {
                stringObjectMap.put("result", "数据错误");
            }
        }

        ListHandle.removeOriginData();
        ListHandle.removeKeys();
    }

    public static void executeScript(String script, List<Map<String, Object>> dataList, HashMap<String, Class<?>> custom) {
        Map<String, String> newVarMap = variableRestructure(dataList.get(0).keySet());
        String newExpression = expressionRestructure(script, newVarMap);
        ListHandle.setOriginData(dataList);

        for (Map<String, Object> stringObjectMap : dataList) {
            try {
                calculateScript(newVarMap, newExpression, stringObjectMap, custom);
            } catch (Exception var8) {
                stringObjectMap.put("result", "数据错误");
            }
        }

        ListHandle.removeOriginData();
        ListHandle.removeKeys();
    }

    public static void executeScript(String script, List<Map<String, Object>> dataList, Map<String, String> relation, Map<String, Class<?>> custom) {
        String newExpression = expressionRestructure(script, relation);
        ListHandle.setOriginData(dataList);

        for (Map<String, Object> stringObjectMap : dataList) {
            try {
                calculateScript(relation, newExpression, stringObjectMap, custom);
            } catch (Exception var8) {
                stringObjectMap.put("result", "数据错误");
            }
        }

        ListHandle.removeOriginData();
        ListHandle.removeKeys();
    }

    private static Object calculateScript(Map<String, String> varMap, String expression, Map<String, Object> data, Map<String, Class<?>> custom) {
        Map<String, Object> varValueMap = new HashMap<>();
        if (varMap != null && data != null) {
            varMap.forEach((k, v) -> {
//            if () {
//
//            }

                varValueMap.put(v, StringUtils.isBlank(String.valueOf(data.get(k))) ? "" : new BigDecimal(String.valueOf(data.get(k))));
            });
        }

        JexlScript s = getJexlEngine().createScript(expression);
        JexlContext context = new MapContext(varValueMap);
        setFunctionObject(context, custom);
        Object result = s.execute(context);

        // 计算结果保留四位小数
        // DecimalFormat decimalFormat = new DecimalFormat("#.####");
        // data.put("result", decimalFormat.format(result));
        // System.out.println(decimalFormat.format(result));
        return result;
    }

    private static void calculateExpression(Map<String, String> varMap, String expression, Map<String, Object> data, Map<String, Class<?>> custom) {
        Map<String, Object> varValueMap = new HashMap<>();
        varMap.forEach((k, v) -> varValueMap.put(v, StringUtils.isBlank(String.valueOf(data.get(k))) ? "" : new BigDecimal(String.valueOf(data.get(k)))));
        JexlExpression e = getJexlEngine().createExpression(expression);
        JexlContext context = new MapContext(varValueMap);
        setFunctionObject(context, custom);
        Object result = e.evaluate(context);
        data.put("result", result);
    }

    private static void setFunctionObject(JexlContext context, Map<String, Class<?>> custom) {
        context.set("Math", Math.class);
        context.set("BigDecimal", BigDecimal.class);
        context.set("List", ListHandle.class);
        if (null != custom) {
            custom.forEach(context::set);
        }
    }

    protected static Map<String, String> variableRestructure(Set<String> keys) {
        int i = 1;
        Map<String, String> map = new HashMap<>(CollectionUtils.mapSize(keys.size()));
        for (String key : keys) {
            map.put(key, "variable" + i++);
        }

        return map;
    }

    protected static String expressionRestructure(String expression, Map<String, String> vars) {
        String oldVar, newVar;
        expression = "(".concat(expression).concat(")");
        for (String key : vars.keySet()) {
            oldVar = "([^a-zA-Z0-9_\\u4e00-\\u9fa5'\"])".concat("[$]{0,1}" + key).concat("([^a-zA-Z0-9_\\u4e00-\\u9fa5'\"])");
            newVar = "$1".concat(vars.get(key)).concat("$2");
            expression = expression.replaceAll(oldVar, newVar);
        }
        return expression.substring(1, expression.length() - 1);
    }
}
