package com.github.stazxr.zblog.mask;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Ignore
public class MaskTest {
    @Test
    public void testMark() {
        System.out.println("hello: " + MaskType.FIRST_MASK.mask("hello"));
        System.out.println("password: " + MaskType.PASSWORD.mask("password"));
        System.out.println("---------手机号");
        System.out.println(": " + MaskType.MOBILE_NUMBER.mask(""));
        System.out.println("null: " + MaskType.MOBILE_NUMBER.mask(null));
        System.out.println("15191821272: " + MaskType.MOBILE_NUMBER.mask("15191821272"));
        System.out.println("123213: " + MaskType.MOBILE_NUMBER.mask("123213"));
        System.out.println("---------座机号");
        System.out.println("123213: " + MaskType.LANDLINE_NUMBER.mask("123213"));
        System.out.println("1232134: " + MaskType.LANDLINE_NUMBER.mask("1232134"));
        System.out.println("100-1212-0012: " + MaskType.LANDLINE_NUMBER.mask("100-1212-0012"));
        System.out.println("10012120012: " + MaskType.LANDLINE_NUMBER.mask("10012120012"));
        System.out.println("---------邮箱");
        System.out.println("zblog: " + MaskType.EMAIL.mask("zblog"));
        System.out.println("zblog@123.com: " + MaskType.EMAIL.mask("zblog@123.com"));
        System.out.println("zblog@123.com: " + MaskType.EMAIL_WEAK.mask("zblog@123.com"));
        System.out.println("---------用户名");
        System.out.println("zbg: " + MaskType.USERNAME.mask("zbg"));
        System.out.println("zblog: " + MaskType.USERNAME.mask("zblog"));
        System.out.println("---------身份证号");
        System.out.println("142622199201103125: " + MaskType.ID_CARD.mask("142622199201103125"));
        System.out.println("1231232131: " + MaskType.ID_CARD.mask("1231232131"));
        System.out.println("---------QQ");
        System.out.println("12345: " + MaskType.QQ_NUMBER.mask("12345"));
        System.out.println("1027353579: " + MaskType.QQ_NUMBER.mask("1027353579"));
    }

    @Test
    public void testJsonMask() {
        MaskSubVo subVo = new MaskSubVo("suntao", "1027353579");
        MaskVo maskVo = new MaskVo("suntao", "123456", "1027353579@qq.com");
        maskVo.setSubVo(subVo);

        Map<String, Object> map = new HashMap<>();
        map.put("a", "12344");
        map.put("b", 123121);
        map.put("c", new MaskSubVo("sunmap", "1329046111"));
        maskVo.setMap(map);

        List<Object> list = new ArrayList<>();
        list.add("1231312");
        list.add(1231312);
        list.add(new MaskSubVo("sunlist", "1329046111"));
        maskVo.setList(list);

        System.out.println(MaskUtil.toMaskString(maskVo));
    }
}
