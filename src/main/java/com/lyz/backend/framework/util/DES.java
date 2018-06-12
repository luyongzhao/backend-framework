package com.lyz.backend.framework.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * <ul>
 * <p>
 * 包含三重DES加密算法
 * </p>
 * </li>
 * </ul>
 */
public class DES
{

	public static final String TYPE_DES = "DES";

	// 3位
	public static final String TYPE_DESede = "DESede";

	// 2重
	public static final String TYPE_Blowfish = "Blowfish";

	public static void main(String[] args)
	{
		String key = "123456781234567812345678123456781234567812345678";

		String str = "5GLdlihlArMbnfDo4IaAkPjdmBQWKo/yOx7oqw+B5aFE2QhE3H/UWUjUp2FYyCTsmEedSNTamCZaipC99tzrRiLEDNzbGPM2GXbDtiexSvQQ9UC0uWPF4ukkRAKgw9PKKOfTH0tKGmGI2UR5u3Vfnm1Q0xy3hjDZmh2IteN7Onf4Otz3e85Nag1gvUynKh4IoZv5pQkUIKXkL057VboazXmdR/R7i2RF1grOCYCxfdcEWvm6bD9WJGEfHrw9EZFbT7Kt+Y2d0DUsnF0ac39ndemvOJZDcxkq";
		try
		{
			str = URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		// String c = Des
		// .encrytWithBase64(
		// Des.DESede,
		// str, key);
		// System.out.println("...." + c);
		System.out.println(DES.deEncrytWithBase64(DES.TYPE_DESede, str, key));
	}

	/**
	 * <p>
	 * <ul>
	 * <li>解密</li>
	 * </ul>
	 * </p>
	 * 
	 * @param Algorithm
	 *            算法
	 * @param myinfo
	 *            待加密的字符串
	 * @param key
	 *            1：Des.DES密钥必须是16位长度的字符串，即16进制编码后必须为8位<br />
	 *            2：Des.Blowfish密钥必须是32位长度的字符串，即16进制编码后必须为16位<br />
	 *            3：Des.DESede密钥必须是48位长度的字符串，即16进制编码后必须为32位<br />
	 * 
	 * @return
	 */
	public static String encrytWithBase64(String Algorithm, String myinfo,
			String key)
	{
		byte[] keybyte = HexString2Bytes(key);

		byte[] str = DES.encryt(Algorithm, myinfo, keybyte);

		return Base64.encodeBase64String(str);
	}

	/**
	 * <p>
	 * <ul>
	 * <li>解密</li>
	 * </ul>
	 * </p>
	 * 
	 * @param Algorithm
	 *            算法
	 * @param myinfo
	 *            待解密的秘文
	 * @param key
	 *            1：Des.DES密钥必须是16位长度的字符串，即16进制编码后必须为8位<br />
	 *            2：Des.Blowfish密钥必须是32位长度的字符串，即16进制编码后必须为16位<br />
	 *            3：Des.DESede密钥必须是48位长度的字符串，即16进制编码后必须为32位<br />
	 * @return
	 */
	public static String deEncrytWithBase64(String Algorithm, String myinfo,
			String key)
	{
		byte[] keybyte = HexString2Bytes(key);
		byte[] info = null;
		try
		{
			info = Base64.decodeBase64(myinfo);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		String str = deEncryt(Algorithm, info, keybyte);
		return str;
	}

	/**
	 * <p>
	 * <ul>
	 * <li>解密</li>
	 * </ul>
	 * </p>
	 * 
	 * @param Algorithm
	 *            算法
	 * @param myinfo
	 *            待加密的字符串
	 * @param key
	 *            1：Des.DES密钥必须是16位长度的字符串，即16进制编码后必须为8位<br />
	 *            2：Des.Blowfish密钥必须是32位长度的字符串，即16进制编码后必须为16位<br />
	 *            3：Des.DESede密钥必须是48位长度的字符串，即16进制编码后必须为32位<br />
	 * @return
	 */
	public static byte[] encryt(String Algorithm, String myinfo, String key)
	{
		byte[] keybyte = HexString2Bytes(key);

		byte[] str = DES.encryt(Algorithm, myinfo, keybyte);

		return str;
	}

	/**
	 * <p>
	 * <ul>
	 * <li>解密</li>
	 * </ul>
	 * </p>
	 * 
	 * @param Algorithm
	 *            算法
	 * @param myinfo
	 *            待解密的秘文
	 * @param key
	 *            1：Des.DES密钥必须是16位长度的字符串，即16进制编码后必须为8位<br />
	 *            2：Des.Blowfish密钥必须是32位长度的字符串，即16进制编码后必须为16位<br />
	 *            3：Des.DESede密钥必须是48位长度的字符串，即16进制编码后必须为32位<br />
	 * @return
	 */
	public static String deEncryt(String Algorithm, byte[] myinfo, String key)
	{
		byte[] keybyte = HexString2Bytes(key);

		String str = deEncryt(Algorithm, myinfo, keybyte);
		return str;
	}

	/*
	 * 加密 Algorithm 定义 加密算法,可用 DES,DESede,Blowfish myinfo 要加密的信息
	 */
	private static byte[] encryt(String Algorithm, String myinfo, byte[] keybyte)
	{

		byte[] cipherByte = null;
		try
		{
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

			// 加密

			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			cipherByte = c1.doFinal(myinfo.getBytes());

		} catch (java.security.NoSuchAlgorithmException e1)
		{
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2)
		{
			e2.printStackTrace();
		} catch (Exception e3)
		{
			e3.printStackTrace();
		}
		return cipherByte;

	}

	/*
	 * 解密 Algorithm 定义 加密算法,可用 DES,DESede,Blowfish cipherByte 要解密的信息
	 */
	private static String deEncryt(String Algorithm, byte[] cipherByte,
			byte[] keybyte)
	{
		byte[] clearByte = null;
		try
		{
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			clearByte = c1.doFinal(cipherByte);

		} catch (java.security.NoSuchAlgorithmException e1)
		{
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2)
		{
			e2.printStackTrace();
		} catch (Exception e3)
		{
			e3.printStackTrace();
		}
		return new String(clearByte);

	}

	private static int parse(char c)
	{
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;
		return (c - '0') & 0x0f;
	}

	/**
	 * 从十六进制字符串到字节数组转换
	 */
	public static byte[] HexString2Bytes(String hexstr)
	{
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;
		for (int i = 0; i < b.length; i++)
		{
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);
			b[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return b;
	}

	/**
	 * 从字节数组到十六进制字符串转换
	 */
	public static final String encodeHex(byte[] bytes)
	{
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		int i;

		for (i = 0; i < bytes.length; i++)
		{
			if (((int) bytes[i] & 0xff) < 0x10)
			{
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString();
	}

}