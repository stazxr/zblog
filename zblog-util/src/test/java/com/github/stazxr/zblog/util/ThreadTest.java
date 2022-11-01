package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.util.thread.ThreadUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 多线程测试工具类
 *
 * @author Thomas Sun
 * @since 2022-09-28
 */
public class ThreadTest {
    @Test
    @Ignore
    public void test1() throws Exception {
        List<Thread> workers = new ArrayList<Thread>() {{
            add(ThreadUtils.runThread(() -> thread1()));
            add(ThreadUtils.runThread(() -> thread2()));
        }};

        // 等待子线程执行结束
        for (Thread worker : workers) {
            worker.join();
        }

        System.out.println("执行主线程");
    }

    private void thread1() {
        System.out.println(1111);
        ThreadUtils.sleepSecond(10);
        System.out.println(11111);
    }

    private void thread2() {
        System.out.println(2222);
        ThreadUtils.sleepSecond(5);
        System.out.println(22222);
    }
}
