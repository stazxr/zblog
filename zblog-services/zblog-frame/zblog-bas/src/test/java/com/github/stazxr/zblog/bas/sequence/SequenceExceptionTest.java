package com.github.stazxr.zblog.bas.sequence;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SequenceExceptionTest {
    @Test
    public void testThrowExceptionUnSwap() {
        throw new SequenceException("test");
    }

    @Test
    public void testThrowException() {
        throw new SequenceException("ZSEQ001");
    }

    @Test
    public void testThrowException2() {
        throw new SequenceException("ZSEQ001", new IllegalArgumentException("参数异常"));
    }
}
