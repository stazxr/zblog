package com.github.stazxr.zblog.util.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Local network utility class for obtaining host information, IP addresses, MAC addresses, etc.
 *
 * @author SunTao
 * @since 2024-05-05
 */
public final class LocalHostUtils {
    private LocalHostUtils() {
    }

    /**
     * Retrieves the local host name.
     *
     * @return local host name
     * @throws UnknownHostException if the local host name cannot be resolved to an address
     */
    public static String getLocalHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * Retrieves the local host address.
     *
     * @return local host address
     * @throws UnknownHostException if the local host name cannot be resolved to an address
     */
    public static String getLocalHostAddress() throws UnknownHostException {
        // first local ip
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * Retrieves the IP address of the current machine.
     *
     * @return IP address of the current machine
     * @throws SocketException      if an I/O error occurs
     * @throws UnknownHostException if the local host name cannot be resolved to an address
     */
    public static String getLocalIp() throws SocketException, UnknownHostException {
        // Iterate over all network interfaces
        InetAddress candidateAddress = null;
        for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
            NetworkInterface networkInterface = interfaces.nextElement();
            for (Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses(); inetAddresses.hasMoreElements();) {
                InetAddress address = inetAddresses.nextElement();
                if (address.isLoopbackAddress() && address.getHostAddress().indexOf(':') != -1) {
                    // Exclude loopback and IPv6 addresses
                    continue;
                }

                if (address.isSiteLocalAddress()) {
                    // If it's a site-local address, return it directly
                    return address.getHostAddress();
                } else if (candidateAddress == null) {
                    // If no site-local address is found, record the candidate address
                    candidateAddress = address;
                }
            }
        }

        return candidateAddress != null ? candidateAddress.getHostAddress() : getLocalHostAddress();
    }

    /**
     * Retrieves IP addresses of all network interfaces, excluding loopback, virtual, and point-to-point addresses.
     *
     * @return array of IP addresses of all network interfaces
     * @throws SocketException if an I/O error occurs
     */
    public static String[] getLocalIps() throws SocketException {
        List<String> list = new ArrayList<>();

        // Get and iterate through network interfaces
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
     * Retrieves the MAC address of the machine.
     *
     * @return MAC address of the machine
     * @throws SocketException      if an I/O error occurs
     * @throws UnknownHostException if the local host name cannot be resolved to an address
     */
    public static String getMacAddress() throws UnknownHostException, SocketException {
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
    }
}
