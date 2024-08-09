//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.stazxr.zblog.jexl.function;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Jexl 扩展: 算数
 *
 * @author SunTao
 * @since 2022-05-28
 */
public class ArithmeticHandle {
    public ArithmeticHandle() {
    }

    /**
     * 四舍五入取整
     *
     * @param value 值
     * @return BigDecimal
     */
    public static BigDecimal round(Object value) {
        return new BigDecimal(value.toString()).setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * 四舍五入取整, 根据位数取整
     *
     * @param value 值
     * @param scale 位数
     * @return BigDecimal
     */
    public static BigDecimal round(Object value, int scale) {
        return new BigDecimal(value.toString()).setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * 截取小数点后的数据，只取整数
     *
     * @param value 值
     * @return BigDecimal
     */
    public static BigDecimal trunc(Object value) {
        return new BigDecimal(value.toString()).setScale(0, RoundingMode.DOWN);
    }

    /**
     * 截取位数后的数据，不四舍五入
     *
     * @param value 值
     * @param scale 位数
     * @return BigDecimal
     */
    public static BigDecimal trunc(Object value, int scale) {
        return new BigDecimal(value.toString()).setScale(scale, RoundingMode.DOWN);
    }

    /**
     * 如果为空，替换为默认值
     *
     * @param value 值
     * @param other 默认值
     * @return isNull(value) ? other : value
     */
    public static Object nvl(Object value, Object other) {
        return isNull(value) ? other : value;
    }

    /**
     * 数据, 如果... 是...，否则是...
     *
     * @param value 值1
     * @param where 值2
     * @param then  结果1
     * @param other 结果2
     * @return 如果值1等于值2,返回结果1,否则返回结果2
     */
    public static Object decode(Object value, Object where, Object then, Object other) {
        return nullToString(value,"").equals(nullToString(where,"")) ? then : other;
    }

    /**
     * 根据值限定该值的最大值最小值，超过范围以最大最小值取值
     *  eg:
     *  limit(5, 4, -1) 结果为 4
     *  limit(-2, 4, -1) 结果为 -1
     *
     * @param value    值
     * @param maxvalue 最大值
     * @param minvalue 最小值
     * @return BigDecimal
     */
    public static BigDecimal limit(Object value, Object maxvalue, Object minvalue) {
        double v = new BigDecimal(nullToString(value, "0")).doubleValue();
        double maxV = new BigDecimal(nullToString(maxvalue, "0")).doubleValue();
        double minV = new BigDecimal(nullToString(minvalue, "0")).doubleValue();
        return v > maxV ? new BigDecimal(nullToString(maxvalue, "0")) :
            (v < minV ? new BigDecimal(nullToString(minvalue, "0")) :
            new BigDecimal(nullToString(value, "0")));
    }

    /**
     * 是否为空
     *
     * @param obj 判断值
     * @return boolean
     */
    public static boolean isNull(Object obj) {
        return (obj == null || (obj instanceof String && ("null".equals(obj) || ((String) obj).trim().length() == 0)));
    }

    /**
     * null 转为空或默认值
     *
     * @param obj 判断值
     * @param def 默认值
     * @return 空或默认值
     */
    public static String nullToString(Object obj, String def) {
        return isNull(obj) ? (isNull(def) ? "" : def) : String.valueOf(obj);
    }
}
