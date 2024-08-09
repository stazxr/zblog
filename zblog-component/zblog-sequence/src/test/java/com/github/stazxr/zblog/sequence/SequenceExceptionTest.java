package com.github.stazxr.zblog.sequence;

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
}
