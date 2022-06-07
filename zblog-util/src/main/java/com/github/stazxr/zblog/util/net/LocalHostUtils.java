package com.github.stazxr.zblog.util.net;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.*;

/**
 * 网络工具类
 *
 * @author SunTao
 * @since 2021-12-13
 */
@Slf4j
public final class LocalHostUtils {
    private LocalHostUtils() {
    }

    /**
     * 获取主机名称
     *
     * @return HostName
     * @throws UnknownHostException if the local host name could not be resolved into an address.
     */
    public static String getHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * 获取首选IP地址
     *
     * @return LocalIp
     * @throws UnknownHostException if the local host name could not be resolved into an address.
     */
    public static String getFirstLocalIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * 获取当前机器的IP
     *
     * @exception SocketException if an I/O error occurs.
     * @exception UnknownHostException if the local host name could not be resolved into an address.
     *
     * @return ip
     */
    public static String getLocalIp() throws SocketException, UnknownHostException {
        InetAddress candidateAddress = null;

        // 遍历所有的网络接口
        for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
            // 在所有的接口下再遍历IP
            NetworkInterface anInterface = interfaces.nextElement();
            for (Enumeration<InetAddress> inetAddresses = anInterface.getInetAddresses(); inetAddresses.hasMoreElements();) {
                // 排除loopback类型地址
                InetAddress inetAddr = inetAddresses.nextElement();
                if (!inetAddr.isLoopbackAddress()) {
                    if (inetAddr.isSiteLocalAddress()) {
                        // 如果是site-local地址，就是它了
                        return inetAddr.getHostAddress();
                    } else if (candidateAddress == null) {
                        // site-local类型的地址未被发现，先记录候选地址
                        candidateAddress = inetAddr;
                    }
                }
            }
        }

        if (candidateAddress != null) {
            return candidateAddress.getHostAddress();
        }

        // 如果没有发现 non-loopback地址.只能用最次选的方案
        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
        if (jdkSuppliedAddress == null) {
            throw new UnknownHostException("jdkSuppliedAddress");
        }

        return jdkSuppliedAddress.getHostAddress();
    }

    /**
     * 获取所有网卡IP，排除回文地址、虚拟地址
     *
     * @return LocalIps
     * @throws SocketException if an I/O error occurs.
     */
    public static String[] getLocalIps() throws SocketException {
        List<String> list = new ArrayList<>();

        // 获取并遍历网卡设备
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        while (enumeration.hasMoreElements()) {
            NetworkInterface netInter = enumeration.nextElement();
            if (!netInter.isUp() || netInter.isLoopback() || netInter.isVirtual() || netInter.isPointToPoint()) {
                continue;
            }
            Enumeration<InetAddress> addresses = netInter.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr.isLoopbackAddress() || !addr.isSiteLocalAddress() || addr.isAnyLocalAddress()) {
                    continue;
                }
                list.add(addr.getHostAddress());
            }
        }
        return list.toArray(new String[0]);
    }

    /**
     * 获取机器Mac地址
     *
     * @return MacAddress
     */
    public static String getMacAddress() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    builder.append("-");
                }
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    builder.append("0").append(str);
                } else {
                    builder.append(str);
                }
            }

            return builder.toString().toUpperCase();
        } catch (UnknownHostException | SocketException e) {
            log.error("getDomainAddress catch eor", e);
            return "";
        }
    }

    /**
     * 获取域名ip地址
     *
     * @param domain 域名
     * @return ip地址
     */
    public static String getDomainAddress(final String domain) {
        try {
            ExecutorService exec = Executors.newCachedThreadPool();
            Future<String> fs = exec.submit(() -> {
                InetAddress inetAddress;
                try {
                    inetAddress = InetAddress.getByName(domain);
                    return inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    log.error("getDomainAddress catch UnknownHostException [{}]", domain, e);
                    return "";
                }
            });
            return fs.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("getDomainAddress catch eor [{}]", domain, e);
            return "";
        }
    }
}
