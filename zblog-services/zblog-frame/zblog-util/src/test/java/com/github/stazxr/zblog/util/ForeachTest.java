package com.github.stazxr.zblog.util;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

@Ignore
public class ForeachTest {
    @Test
    public void test1() throws Exception {
        String[] array = new String[]{"a", "b", "c", "d", "e", "f", "g"};
        for (String s : array) {
            System.out.println(s);
        }
        System.out.println("========");
        Arrays.stream(array).forEach(System.out::println);
    }

    @Test
    public void test2() throws Exception {
        String[][] array = new String[][]{{"a"}, {"b"}, {"c"}};
        List<String[]> strings = Arrays.asList(array);
        String[] summerDesignDataDetailVo = strings.stream().filter(e -> e[0].equals("a")).findFirst().get();
        System.out.println(Arrays.asList(summerDesignDataDetailVo));

        summerDesignDataDetailVo[0] = "aa";

        for (String[] strings1 : array) {
            System.out.println(Arrays.asList(strings1));
        }

    }
}
