package com.github.stazxr.zblog.web.serializer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;

import java.lang.reflect.Type;
import java.math.BigInteger;

/**
 * 自定义序列化机制
 *
 * Long 在序列化为 String 时，如果没有超过浏览器精度，则仍然返回 Long，否则返回 String
 *
 * @author SunTao
 * @since 2022-04-18
 */
public class LongToStringSerializer implements ObjectWriter<Long> {
    public static final LongToStringSerializer INSTANCE = new LongToStringSerializer();

    private static final long JS_IEEE_754_MIN = -9007199254740992L;

    private static final long JS_IEEE_754_MAX = 9007199254740992L;

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        if (object == null) {
            jsonWriter.writeNull();
        } else {
            try {
                // 判断 long 的范围是否在 IEEE 754 标准范围内
                long value = Long.parseLong(object.toString());
                if (value >= JS_IEEE_754_MIN && value < JS_IEEE_754_MAX) {
                    jsonWriter.writeBigInt(new BigInteger(object.toString()));
                    return;
                }
            } catch (Exception ignored) { }
            jsonWriter.writeString(object.toString());
        }
    }
}
