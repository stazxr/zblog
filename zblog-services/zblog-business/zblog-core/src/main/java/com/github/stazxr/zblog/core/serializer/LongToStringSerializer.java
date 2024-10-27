package com.github.stazxr.zblog.core.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson2.JSONWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;

/**
 * 自定义序列化机制，Long 在序列化为 字符串时，如果没有超过浏览器精度，则仍然返回 Long，否则返回字符串
 *
 * @author SunTao
 * @since 2022-04-18
 */
public class LongToStringSerializer implements ObjectSerializer {
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

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter writer = serializer.out;
        if (object == null) {
            writer.writeNull();
        } else {
            try {
                // 判断 long 的范围是否在 IEEE 754 标准范围内，在则不转换
                long value = Long.parseLong(object.toString());
                if (value >= JS_IEEE_754_MIN && value < JS_IEEE_754_MAX) {
                    writer.writeLong(value);
                    return;
                }
            } catch (Exception ignored) { }
            writer.writeString(object.toString());
        }
    }
}
