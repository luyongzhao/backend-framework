package com.lyz.backend.framework.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

public class IPAddressUtil
{
	public static String getLocalHostName()
	{
		String hostName;
		try
		{
			InetAddress addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (Exception ex)
		{
			hostName = "";
		}
		return hostName;
	}

	public static Set<String> getAllLocalHostIP()
	{
		Set<String> ret =  new HashSet<String>();
		ret.add("127.0.0.1");
		try
		{
			String hostName = getLocalHostName();
			if (hostName.length() > 0)
			{
				InetAddress[] addrs = InetAddress.getAllByName(hostName);
				if (addrs.length > 0)
				{
					for (int i = 0; i < addrs.length; i++)
					{
						ret.add(addrs[i].getHostAddress());
					}
				}
			}

		} catch (Exception ex)
		{
		}
		return ret;
	}

	public static void main(String[] args) throws UnknownHostException
	{
		System.out.println(getLocalHostName() + "----" + getAllLocalHostIP());
	}
}
