package com.mobike.iotcloud.backend.framework.util;

import java.security.MessageDigest;

public class MD5
{
	private static final char[] HEX_DIGITS =
	{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	public static final String ALGORITHM_MD5 = "MD5";
	public static final String ALGORITHM_SHA1 = "SHA1";

	public static String encode(String algorithm, String str)
	{
		if (str == null)
		{
			return null;
		}
		try
		{
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(str.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static String encode(String algorithm, String str, String encodeType)
	{
		if (str == null)
		{
			return null;
		}
		try
		{
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(str.getBytes(encodeType));
			return getFormattedText(messageDigest.digest());
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private static String getFormattedText(byte[] bytes)
	{
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		for (int j = 0; j < len; j++)
		{
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}

	public static String encode(String str)
	{
		return encode(ALGORITHM_MD5, str);
	}
	
	public static String encodeWithType(String str, String encodeType)
	{
		return encode(ALGORITHM_MD5, str, encodeType);
	}

	public static String encodeMD5(String str)
	{
		return encode(ALGORITHM_MD5, str);
	}

	public static String encodeSHA1(String str)
	{
		return encode(ALGORITHM_SHA1, str);
	}

	public static void main(String[] args)
	{
		String str = encode("artminsheng_fuwu_ecannetwork");
		System.out.println(str.substring(0,2));
		System.out.println(str.substring(2,4));
		
		System.out.println(encode("1dac4dccc45bc243972b495cbf85171a:120439"));
		
		System.out.println("111111 MD5  :" + str);
		System.out.println("111111 MD5  :" + encodeMD5("Cgq@2017"));
		System.out.println("111111 SHA1 :" + encode("SHA1", "1231337553451395265968"));
		System.out.println("111111 SHA1 :" + encodeSHA1("1231337553451395265968"));
	}

}
