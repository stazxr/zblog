package com.github.stazxr.zblog.bas.sequence;

import com.github.stazxr.zblog.bas.sequence.core.id.IdGeneratorImpl;
import com.github.stazxr.zblog.bas.sequence.core.snowflake.SnowflakeSequenceGenerator;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class IdGeneratorTest {
    @Test
    public void testIdGenerator() {
        SnowflakeSequenceGenerator snowflakeSequenceGenerator = new SnowflakeSequenceGenerator(0, 1);
        IdGeneratorImpl idGenerator = new IdGeneratorImpl();
        idGenerator.setSequenceGenerator(snowflakeSequenceGenerator);
        for (int i = 0; i < 5; i++) {
            System.out.println(idGenerator.generateId());
        }
    }
}
