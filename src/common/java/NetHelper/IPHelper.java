package common.java.NetHelper;

import common.java.nLogger.nLogger;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class IPHelper {
	public static List<String> localIPs() throws SocketException {
		List<String> ips = new ArrayList<>();
		Enumeration<NetworkInterface> interfs = NetworkInterface.getNetworkInterfaces();
		while (interfs.hasMoreElements()) {
			NetworkInterface interf = interfs.nextElement();
			Enumeration<InetAddress> addres = interf.getInetAddresses();
			while (addres.hasMoreElements()) {
				InetAddress in = addres.nextElement();
				if (in instanceof Inet4Address)
	            {
					ips.add(in.getHostAddress());
				} else if (in instanceof Inet6Address) {
					ips.add(in.getHostAddress());
				}
			}
		}
		return ips;
	}

	public static String localIP() {
		List<String> ips = null;
		try {
			ips = localIPs();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			nLogger.logInfo(e);
		}
		return ips != null && ips.size() > 0 ? ips.get(0) : "127.0.0.1";
	}

	public static boolean isLocalIP(String ip) {
		if (ip.equals("127.0.0.1") || ip.equals("0.0.0.0")) {
			return true;
		}

		List<String> ipList = getIpAddress();
		if (ipList.size() > 0) {
			int i, l = ipList.size();
			for (i = 0; i < l; i++) {
				if (ipList.get(i).equals(ip)) {
					return true;
				}
			}
		}

		return false;
	}

	// From HBase Addressing.Java
	private static List<String> getIpAddress() {
		List<String> rList = new ArrayList<>();
		try {
			Enumeration<NetworkInterface> interfaces;
			interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface ni = interfaces.nextElement();
				Enumeration<InetAddress> addresss = ni.getInetAddresses();
				while (addresss.hasMoreElements()) {
					InetAddress nextElement = addresss.nextElement();
					String hostAddress = nextElement.getHostAddress();
					rList.add(hostAddress);
					//System.out.println("本机IP地址为：" +hostAddress);
				}
			} 
		} 
		catch (Exception e) {
			nLogger.logInfo(e);
		}
		return rList;
	}

	/**
	 * 获得内网IP
	 *
	 * @return 内网IP
	 */
	public static String getLocalhostIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获得外网IP
	 *
	 * @return 外网IP
	 */
	public static String getInternetIp() {
		try {
			Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
			InetAddress ip;
			Enumeration<InetAddress> addrs;
			while (networks.hasMoreElements()) {
				addrs = networks.nextElement().getInetAddresses();
				while (addrs.hasMoreElements()) {
					ip = addrs.nextElement();
					if (ip instanceof Inet4Address
							&& ip.isSiteLocalAddress()
							&& !ip.getHostAddress().equals(getLocalhostIp())) {
						return ip.getHostAddress();
					}
				}
			}

			// 如果没有外网IP，就返回内网IP
			return getLocalhostIp();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static long ipToLong(String ipAddress) {

		// ipAddressInArray[0] = 192
		String[] ipAddressInArray = ipAddress.split("\\.");

		long result = 0;
		for (int i = 0; i < ipAddressInArray.length; i++) {

			int power = 3 - i;
			int ip = Integer.parseInt(ipAddressInArray[i]);

			// 1. 192 * 256^3
			// 2. 168 * 256^2
			// 3. 1 * 256^1
			// 4. 2 * 256^0
			result += ip * Math.pow(256, power);

		}

		return result;

	}

	public static long ipToLong2(String ipAddress) {

		long result = 0;

		String[] ipAddressInArray = ipAddress.split("\\.");

		for (int i = 3; i >= 0; i--) {

			long ip = Long.parseLong(ipAddressInArray[3 - i]);

			// left shifting 24,16,8,0 and bitwise OR

			// 1. 192 << 24
			// 1. 168 << 16
			// 1. 1 << 8
			// 1. 2 << 0
			result |= ip << (i * 8);

		}

		return result;
	}

	public static String longToIp(long i) {

		return ((i >> 24) & 0xFF) +
				"." + ((i >> 16) & 0xFF) +
				"." + ((i >> 8) & 0xFF) +
				"." + (i & 0xFF);

	}

	public static String longToIp2(long ip) {
		StringBuilder sb = new StringBuilder(15);

		for (int i = 0; i < 4; i++) {

			// 1. 2
			// 2. 1
			// 3. 168
			// 4. 192
			sb.insert(0, (ip & 0xff));

			if (i < 3) {
				sb.insert(0, '.');
			}

			// 1. 192.168.1.2
			// 2. 192.168.1
			// 3. 192.168
			// 4. 192
			ip = ip >> 8;

		}

		return sb.toString();
	}

}
