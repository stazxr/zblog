package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.util.net.IpUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

@Ignore
public class IpUtilTest {
    @Test
    public void testAllMethod() throws Exception {
        long ipNum = IpUtils.formatIpToNum("192.123.27.2");
        System.out.println("192.123.27.2 > " + ipNum);
        String ip = IpUtils.parseNumToIp(ipNum);
        System.out.println(ipNum + " > " + ip);

        String charFromIpString = IpUtils.get7CharFromIpString("192.255.27.2", 3);
        System.out.println("[192.255.27.2, 3] > " + charFromIpString);
        String[] ipInfoFromChar = IpUtils.parse7CharToIpCountAry(charFromIpString);
        System.out.println(charFromIpString + " > " + Arrays.toString(ipInfoFromChar));
    }
}
