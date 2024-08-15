package com.github.stazxr.zblog.context;

import com.github.stazxr.zblog.context.exception.ContextErrorCode;
import com.github.stazxr.zblog.context.exception.ContextException;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ContextExceptionTest {
    @Test
    public void testThrowException1() {
        throw new ContextException(ContextErrorCode.ZCXT001);
    }

    @Test
    public void testThrowException2() {
        throw new ContextException(ContextErrorCode.ZCXT001, new RuntimeException("测试"));
    }
}
