package com.github.stazxr.zblog.bas.jexl;

import cn.hutool.core.math.Calculator;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试 JEXL
 *
 * @author Thomas Sun
 * @since 2022-05-25
 */
@Ignore
public class JexlTest {
    @Test
    public void test1() {
        String script = "工程费/工程费占比";
        Map<String, Object> data = new HashMap<>();
        data.put("工程费", 3);
        data.put("工程费占比", 2);
        long begin = System.currentTimeMillis();
        String result = ExpressionUtils.executeExpression(script, data);
        long end = System.currentTimeMillis();
        System.out.println("耗费时长" + (end - begin) + "毫秒");
        System.out.println(result);
    }

    @Test
    public void test2() {
        String script = "91/2.0";
        Map<String, Object> data = new HashMap<>();
        long begin = System.currentTimeMillis();
        String result = ExpressionUtils.executeExpression(script, data);
        long end = System.currentTimeMillis();
        System.out.println("耗费时长" + (end - begin) + "毫秒");
        System.out.println(result);
    }

    @Test
    public void test3() {
        Calculator calculator = new Calculator();
        String replace = "91/2";
        System.out.println(calculator.calculate(replace));
    }

    @Test
    public void test4() {
        String script = "(工程费含税<=5000)?4.10:(工程费含税>5000&工程费含税<=10000)?((工程费含税-5000)*(3.75-4.10)/(10000-5000)+4.10):(工程费含税>10000&工程费含税<=50000)?((工程费含税-10000)*(3.32-3.75)/(50000-10000)+3.75):(工程费含税>50000&工程费含税<=100000)?((工程费含税-50000)*(2.68-3.32)/(100000-50000)+3.32):(工程费含税>100000&工程费含税<=200000)?((工程费含税-100000)*(2.23-2.68)/(200000-100000)+2.68):(工程费含税>200000&工程费含税<=400000)?((工程费含税-200000)*(2.13-2.23)/(400000-200000)+2.23):(工程费含税>400000&工程费含税<=1000000)?((工程费含税-400000)*(2.07-2.13)/(1000000-400000)+2.13):2.07";
        Map<String, Object> data = new HashMap<>();
        data.put("工程费含税", "100000");
        long begin = System.currentTimeMillis();
        String result = ExpressionUtils.executeScript(script, data);
        long end = System.currentTimeMillis();
        System.out.println("耗费时长" + (end - begin) + "毫秒");
        System.out.println(result);
    }

    @Test
    public void test5() {
        String script = "(工程费含税<=500?500:工程费含税)*(建设单位管理费费率/100)*有项目新建或改扩建系数*使用新技术、新材料、新工艺系数";
        Map<String, Object> data = new HashMap<>();
        data.put("工程费含税", "300");
        data.put("建设单位管理费费率", "3.72");
        data.put("有项目新建或改扩建系数", "1");
        data.put("使用新技术、新材料、新工艺系数", "1");
        long begin = System.currentTimeMillis();
        ExpressionUtils.executeScript(script, data);
        long end = System.currentTimeMillis();
        System.out.println("耗费时长" + (end - begin) + "毫秒");
        System.out.println(data.get("result"));
    }

    @Test
    public void test6() {
        String script = "工程费<=5000?15:工程费>5000&工程费<=20000?((工程费-5000)*(50-15)/(20000-5000)+15):工程费>20000&工程费<=100000?((工程费-20000)*(120-50)/(100000-20000)+50):工程费>100000&工程费<=500000?((工程费-100000)*(220-120)/(500000-100000)+120):工程费>500000&工程费<=1000000?((工程费-500000)*(300-220)/(1000000-500000)+220):(300+(工程费-1000000)*0.0001)";
        Map<String, Object> data = new HashMap<>();
        data.put("工程费", "1200000");
        long begin = System.currentTimeMillis();
        ExpressionUtils.executeScript(script, data);
        long end = System.currentTimeMillis();
        System.out.println("耗费时长" + (end - begin) + "毫秒");
        System.out.println(data.get("result"));
    }

    @Test
    public void test7() {
        String script = "计费额<=500?16.5:计费额>500&计费额<=1000?((计费额-500)*(30.1-16.5)/(1000-500)+16.5):计费额>1000&计费额<=3000?((计费额-1000)*(78.1-30.1)/(3000-1000)+30.1):计费额>3000&计费额<=5000?((计费额-3000)*(120.8-78.1)/(5000-3000)+78.1):计费额>5000&计费额<=8000?((计费额-5000)*(181.0-120.8)/(8000-5000)+120.8):计费额>8000&计费额<=10000?((计费额-8000)*(218.6-181.0)/(10000-8000)+181.0):计费额>10000&计费额<=20000?((计费额-10000)*(393.4-218.6)/(20000-10000)+218.6):计费额>20000&计费额<=40000?((计费额-20000)*(708.2-393.4)/(40000-20000)+393.4):计费额>40000&计费额<=60000?((计费额-40000)*(991.4-708.2)/(60000-40000)+708.2):计费额>60000&计费额<=80000?((计费额-60000)*(1255.8-991.4)/(80000-60000)+991.4):计费额>80000&计费额<=100000?((计费额-80000)*(1507.0-1255.8)/(100000-80000)+1255.8):计费额>100000&计费额<=200000?((计费额-100000)*(2712.5-1507.0)/(200000-100000)+1507.0):计费额>200000&计费额<=400000?((计费额-200000)*(4882.6-2712.5)/(400000-200000)+2712.5):计费额>400000&计费额<=600000?((计费额-400000)*(6835.6-4882.6)/(600000-400000)+4882.6):计费额>600000&计费额<=800000?((计费额-600000)*(8658.4-6835.6)/(800000-600000)+6835.6):计费额>800000&计费额<=1000000?((计费额-800000)*(10390.1-8658.4)/(1000000-800000)+8658.4):10390.1";
        Map<String, Object> data = new HashMap<>();
        data.put("计费额", "1000");
        long begin = System.currentTimeMillis();
        ExpressionUtils.executeScript(script, data);
        long end = System.currentTimeMillis();
        System.out.println("耗费时长" + (end - begin) + "毫秒");
        System.out.println(data.get("result"));
    }

    @Test
    public void test8() {
        String script = "建筑工程费<=5000?71:建筑工程费>5000&建筑工程费<=10000?131.50:建筑工程费>10000&建筑工程费<=20000?194.00:建筑工程费>20000&建筑工程费<=30000?260.50:建筑工程费>30000&建筑工程费<=40000?319.90:建筑工程费>40000&建筑工程费<=50000?375.20:建筑工程费>50000&建筑工程费<=60000?448.50:建筑工程费>60000&建筑工程费<=70000?492.80:建筑工程费>70000&建筑工程费<=80000?552.00:建筑工程费>80000&建筑工程费<=90000?617.80:建筑工程费>90000&建筑工程费<=100000?674.20:建筑工程费>100000&建筑工程费<=110000?734.60:建筑工程费>110000&建筑工程费<=120000?803.00:建筑工程费>120000&建筑工程费<=130000?862.50:建筑工程费>130000&建筑工程费<=140000?913.00:建筑工程费>140000&建筑工程费<=150000?971.50:建筑工程费>150000&建筑工程费<=160000?1032.80:建筑工程费>160000&建筑工程费<=170000?1088.30:建筑工程费>170000&建筑工程费<=180000?1152.50:建筑工程费>180000&建筑工程费<=190000?1214.00:建筑工程费>190000&建筑工程费<=200000?1279.50:(建筑工程费-200000)*(1279.50-1214.00)/(200000-190000)+1279.50";
        Map<String, Object> data = new HashMap<>();
        data.put("建筑工程费", "210000");
        long begin = System.currentTimeMillis();
        ExpressionUtils.executeScript(script, data);
        long end = System.currentTimeMillis();
        System.out.println("耗费时长" + (end - begin) + "毫秒");
        System.out.println(data.get("result"));
    }

    @Test
    public void test9() {
        String script = "(非标设备费额*单台非标设备设计费率)*(1+多台非标设备设计费率*(储罐数量-1))";
        Map<String, Object> data = new HashMap<>();
        data.put("非标设备费额", "10000");
        data.put("储罐数量", "2");
        data.put("单台非标设备设计费率", "1.1");
        data.put("多台非标设备设计费率", "0.3");
        long begin = System.currentTimeMillis();
        ExpressionUtils.executeScript(script, data);
        long end = System.currentTimeMillis();
        System.out.println("耗费时长" + (end - begin) + "毫秒");
        System.out.println(data.get("result"));
    }

    @Test
    public void test10() {
        String script = "工程费+联合试运转费-(场平系数+110~6.3kV总变电所系数+厂前区系数+110kV外部输电线路系数)";
        Map<String, Object> data = new HashMap<>();
        data.put("工程费", "10");
        data.put("联合试运转费", "2");
        data.put("场平系数", "1.1");
        data.put("110~6.3kV总变电所系数", "0.3");
        data.put("厂前区系数", "0.3");
        data.put("110kV外部输电线路系数", "0.3");
        long begin = System.currentTimeMillis();
        ExpressionUtils.executeScript(script, data);
        long end = System.currentTimeMillis();
        System.out.println("耗费时长" + (end - begin) + "毫秒");
        System.out.println(data.get("result"));
    }

    @Test
    public void test11() {
        String script = "return Math.floorDiv(2, 3)";
        long begin = System.currentTimeMillis();
        Object result = ExpressionUtils.executeScript(script);
        long end = System.currentTimeMillis();
        System.out.println("耗费时长" + (end - begin) + "毫秒");
        System.out.println(result);
    }

    @Test
    public void test11_2() {
        String script = "return 3/2";
        long begin = System.currentTimeMillis();
        Object result = ExpressionUtils.executeScript(script);
        long end = System.currentTimeMillis();
        System.out.println("耗费时长" + (end - begin) + "毫秒");
        System.out.println(result);
    }

    @Test
    public void test11_3() {
        String script = "3.0/2";
        long begin = System.currentTimeMillis();
        Object result = ExpressionUtils.executeExpression(script);
        long end = System.currentTimeMillis();
        System.out.println("耗费时长" + (end - begin) + "毫秒");
        System.out.println(result);
    }

    @Test
    public void test12() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("姓名", "张三");
        dataMap.put("性别", "男");
        dataMap.put("数学成绩", 90);
        dataMap.put("物理成绩", 93);
        dataMap.put("化学成绩", 78);
        dataMap.put("体育成绩", 62);
        dataMap.put("成绩系数", 0.58);
        dataList.add(dataMap);

        dataMap = new HashMap<>();
        dataMap.put("姓名", "李四");
        dataMap.put("性别", "男");
        dataMap.put("数学成绩", 91);
        dataMap.put("物理成绩", 94);
        dataMap.put("化学成绩", 76);
        dataMap.put("体育成绩", 63);
        dataMap.put("成绩系数", 0.58);
        dataList.add(dataMap);

        dataMap = new HashMap<>();
        dataMap.put("姓名", "小二");
        dataMap.put("性别", "男");
        dataMap.put("数学成绩", 96);
        dataMap.put("物理成绩", 54);
        dataMap.put("化学成绩", 76);
        dataMap.put("成绩系数", 0.58);
        dataList.add(dataMap);

        String script = "return List.sum('体育成绩')";
        long begin = System.currentTimeMillis();
        String result = ExpressionUtils.executeScript(script, dataList);
        long end = System.currentTimeMillis();
        System.out.println("耗费时长" + (end - begin) + "毫秒");
        System.out.println(result);
    }

    @Test
    public void test13() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("姓名", "张三");
        dataMap.put("性别", "男");
        dataMap.put("数学成绩", 90);
        dataMap.put("物理成绩", 93);
        dataMap.put("化学成绩", 78);
        dataMap.put("体育成绩", 62);
        dataMap.put("成绩系数", 0.58);
        dataList.add(dataMap);

        dataMap = new HashMap<>();
        dataMap.put("姓名", "李四");
        dataMap.put("性别", "男");
        dataMap.put("数学成绩", 91);
        dataMap.put("物理成绩", 94);
        dataMap.put("化学成绩", 76);
        dataMap.put("体育成绩", 63);
        dataMap.put("成绩系数", 0.58);
        dataList.add(dataMap);

        dataMap = new HashMap<>();
        dataMap.put("姓名", "小二");
        dataMap.put("性别", "男");
        dataMap.put("数学成绩", 99);
        dataMap.put("物理成绩", 54);
        dataMap.put("化学成绩", 76);
        dataMap.put("成绩系数", 0.58);
        dataList.add(dataMap);

        String script = "return Arithmetic.round(List.avg('体育成绩'), 2)";
        long begin = System.currentTimeMillis();
        String result = ExpressionUtils.executeScript(script, dataList);
        long end = System.currentTimeMillis();
        System.out.println("耗费时长" + (end - begin) + "毫秒");
        System.out.println(result);
    }
}
