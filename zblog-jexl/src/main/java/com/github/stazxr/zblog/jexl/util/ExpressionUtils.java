package com.github.stazxr.zblog.jexl.util;

import com.github.stazxr.zblog.jexl.function.ArithmeticHandle;
import com.github.stazxr.zblog.jexl.function.ListHandle;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.collection.CollectionUtils;
import com.github.stazxr.zblog.util.math.MathUtils;
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
    public static final String DATA_ERROR = "数据错误";

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

    /**
     * 执行简单脚本
     *
     * @param script 脚本
     * @return 计算结果, 计算错误返回 { ExpressionUtils.DATA_ERROR }
     */
    public static String executeScript(String script) {
        try {
            return String.valueOf(calculateScript(script, null, null, null));
        } catch (Exception e) {
            log.error("数据计算错误：{}", script, e);
            return DATA_ERROR;
        } finally {
            ListHandle.removeKeys();
            ListHandle.removeRankKeys();
            ListHandle.removeOriginData();
        }
    }

    /**
     * 执行携带变量的脚本
     *
     * @param script 脚本
     * @param data   数据信息
     * @return 计算结果, 计算错误返回 { ExpressionUtils.DATA_ERROR }
     */
    public static String executeScript(String script, Map<String, Object> data) {
        try {
            Map<String, String> newVarMap = variableRestructure(data.keySet());
            String newScript = expressionRestructure(script, newVarMap);
            return String.valueOf(calculateScript(newScript, newVarMap, data, null));
        } catch (Exception e) {
            log.error("数据计算错误：{}, {}", script, e);
            return DATA_ERROR;
        } finally {
            ListHandle.removeKeys();
            ListHandle.removeRankKeys();
            ListHandle.removeOriginData();
        }
    }

    /**
     * 执行携带变量的脚本
     *
     * @param script 脚本
     * @param data   数据信息
     * @param format 结果格式
     * @return 计算结果, 计算错误返回 { ExpressionUtils.DATA_ERROR }
     */
    public static String executeScript(String script, Map<String, Object> data, DecimalFormat format) {
        String result = executeScript(script, data);
        if (StringUtils.isNotBlank(result) && !DATA_ERROR.equals(result)) {
            result = format.format(result);
        }

        return result;
    }

    /**
     * 执行复杂脚本,用于处理多个数据之间的关系,如求和\最大值\平均值等
     *
     * @param script   脚本
     * @param dataList 数据集合
     * @return 计算结果, 计算错误返回 { ExpressionUtils.DATA_ERROR }
     */
    public static String executeScript(String script, List<Map<String, Object>> dataList) {
        Map<String, String> newVarMap = variableRestructure(dataList.get(0).keySet());
        return executeScript(script, dataList, newVarMap);
    }

    /**
     * 执行复杂脚本,用于处理多个数据之间的关系,如求和\最大值\平均值等
     *
     * @param script   脚本
     * @param dataList 数据集合
     * @param varMap   变量对应关系,如: 姓名-variable1,性别-variable2
     * @return 计算结果, 计算错误返回 { ExpressionUtils.DATA_ERROR }
     */
    public static String executeScript(String script, List<Map<String, Object>> dataList, Map<String, String> varMap) {
        return executeScript(script, dataList, varMap, null);
    }

    /**
     * 执行复杂脚本,用于处理多个数据之间的关系,如求和\最大值\平均值等
     *
     * @param script   脚本
     * @param dataList 数据集合
     * @param custom   自定义规则
     * @return 计算结果, 计算错误返回 { ExpressionUtils.DATA_ERROR }
     */
    public static String executeScript(String script, List<Map<String, Object>> dataList, HashMap<String, Class<?>> custom) {
        return executeScript(script, dataList, null, custom);
    }

    /**
     * 执行复杂脚本,用于处理多个数据之间的关系,如求和\最大值\平均值等
     *
     * @param script   脚本
     * @param dataList 数据集合
     * @param varMap   变量对应关系,如: 姓名-variable1,性别-variable2
     * @param custom   自定义规则
     * @return 计算结果, 计算错误返回 { ExpressionUtils.DATA_ERROR }
     */
    public static String executeScript(String script, List<Map<String, Object>> dataList, Map<String, String> varMap, Map<String, Class<?>> custom) {
        try {
            String newScript = expressionRestructure(script, varMap);
            ListHandle.setOriginData(dataList);
            return String.valueOf(calculateScript(newScript, varMap, dataList.get(0), custom));
        } catch (Exception e) {
            log.error("数据计算错误：{}, {}", script, e);
            return DATA_ERROR;
        } finally {
            ListHandle.removeKeys();
            ListHandle.removeRankKeys();
            ListHandle.removeOriginData();
        }
    }

    /**
     * 执行简单公式
     *
     * @param expression 数学公式
     * @return 计算结果, 计算错误返回 { ExpressionUtils.DATA_ERROR }
     */
    public static String executeExpression(String expression) {
        try {
            return String.valueOf(calculateExpression(expression, null, null, null));
        } catch (Exception e) {
            log.error("数据计算错误：{}", expression, e);
            return DATA_ERROR;
        } finally {
            ListHandle.removeKeys();
            ListHandle.removeRankKeys();
            ListHandle.removeOriginData();
        }
    }

    /**
     * 执行携带变量的公式
     *
     * @param expression 公式
     * @param data       数据
     * @return 计算结果, 计算错误返回 { ExpressionUtils.DATA_ERROR }
     */
    public static String executeExpression(String expression, Map<String, Object> data) {
        try {
            Map<String, String> newVarMap = variableRestructure(data.keySet());
            String newExpression = expressionRestructure(expression, newVarMap);
            return String.valueOf(calculateExpression(newExpression, newVarMap, data, null));
        } catch (Exception e) {
            log.error("数据计算错误：{}", expression, e);
            return DATA_ERROR;
        } finally {
            ListHandle.removeKeys();
            ListHandle.removeRankKeys();
            ListHandle.removeOriginData();
        }
    }

    /**
     * 执行携带变量的公式
     *
     * @param expression 公式
     * @param data       数据
     * @param format     返回数据格式
     * @return 计算结果, 计算错误返回 { ExpressionUtils.DATA_ERROR }
     */
    public static String executeExpression(String expression, Map<String, Object> data, DecimalFormat format) {
        String result = executeExpression(expression, data);
        if (StringUtils.isNotBlank(result) && !DATA_ERROR.equals(result)) {
            result = format.format(result);
        }

        return result;
    }

    private static Object calculateScript(String script, Map<String, String> varMap, Map<String, Object> valueMap, Map<String, Class<?>> custom) {
        Map<String, Object> varValueMap = parseVarValueMap(varMap, valueMap);
        JexlScript jexlScript = getJexlEngine().createScript(script);
        JexlContext context = new MapContext(varValueMap);
        setFunctionObject(context, custom);
        return jexlScript.execute(context);
    }

    private static Object calculateExpression(String expression, Map<String, String> varMap, Map<String, Object> valueMap, Map<String, Class<?>> custom) {
        Map<String, Object> varValueMap = parseVarValueMap(varMap, valueMap);
        JexlExpression jexlExpression = getJexlEngine().createExpression(expression);
        JexlContext context = new MapContext(varValueMap);
        setFunctionObject(context, custom);
        return jexlExpression.evaluate(context);
    }

    /**
     * 合并变量字典和值字典
     *
     * @param varMap   变量字典
     * @param valueMap 值字典
     * @return 变量 - 值的对应关系
     */
    protected static Map<String, Object> parseVarValueMap(Map<String, String> varMap, Map<String, Object> valueMap) {
        Map<String, Object> varValueMap = new HashMap<>(CollectionUtils.mapSize(varMap == null ? 0 : varMap.size()));
        if (varMap != null && valueMap != null) {
            varMap.forEach((k, var) -> {
                if (valueMap.containsKey(k)) {
                    String value = String.valueOf(valueMap.get(k));
                    varValueMap.put(var, MathUtils.isNumeric(value) ? new BigDecimal(value) : value);
                } else {
                    varValueMap.put(var, null);
                }
            });
        }

        return varValueMap;
    }

    /**
     * 替换脚本中的变量
     *
     * @param script 脚本
     * @param vars   变量信息
     * @return 新脚本
     */
    protected static String expressionRestructure(String script, Map<String, String> vars) {
        if (vars == null || vars.isEmpty()) {
            return script;
        }

        String oldVar, newVar;
        script = "(".concat(script).concat(")");
        for (String key : vars.keySet()) {
            oldVar = "([^a-zA-Z0-9_\\u4e00-\\u9fa5'\"])".concat("[$]{0,1}" + key).concat("([^a-zA-Z0-9_\\u4e00-\\u9fa5'\"])");
            newVar = "$1".concat(vars.get(key)).concat("$2");
            script = script.replaceAll(oldVar, newVar);
        }
        return script.substring(1, script.length() - 1);
    }

    private static void setFunctionObject(JexlContext context, Map<String, Class<?>> custom) {
        context.set("Math", Math.class);
        context.set("BigDecimal", BigDecimal.class);
        context.set("List", ListHandle.class);
        context.set("Arithmetic", ArithmeticHandle.class);
        if (custom != null && !custom.isEmpty()) {
            custom.forEach(context::set);
        }
    }

    private static Map<String, String> variableRestructure(Set<String> keys) {
        int i = 1;
        Map<String, String> map = new HashMap<>(CollectionUtils.mapSize(keys.size()));
        for (String key : keys) {
            map.put(key, "variable" + i++);
        }

        return map;
    }
}
