package com.mobike.iotcloud.backend.framework.util;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 自定义X进制与10进制之间的转换,
 * 
 * @author luyongzhao
 * 
 */
public class NumberX
{
	public static NumberX Number33 = new NumberX(
			"0123456789abcdefghjkmnpqrstuvwxyz");
	public static NumberX Number62 = new NumberX(
			"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
	public static NumberX Number2 = new NumberX("01");
	public static NumberX Number16 = new NumberX("0123456789ABCDEF");


	public String number10ToX(long num, int fixedLength)
	{
		return StringUtils.leftPad(number10ToX(num), fixedLength,
				CHARS.charAt(0));
	}

	public String number10ToX(long num)
	{
		StringBuffer str = new StringBuffer();
		Stack<Character> s = new Stack<Character>();
		while (num != 0)
		{
			s.push(CHARS.charAt((int) (num % BASE)));
			num /= BASE;
		}

		while (!s.isEmpty())
		{
			str.append(s.pop());
		}

		return str.toString();
	}

	/**
	 * 返回-1时为错误
	 * 
	 * @param numberX
	 * @return
	 */
	public long numberXTo10(String numberX)
	{
		long number10 = 0;

		int length = numberX.length();
		for (int index = 0; index < length; index++)
		{
			char c = numberX.charAt(index);

			Integer num = CHAR_INDEX_MAP.get(String.valueOf(c));
			if (num == null)
			{
				return -1;
			}

			int pow = length - index - 1;
			number10 = (long) (number10 + Math.pow(BASE, pow) * num);
		}
		// 10-->2^1 + 0

		return number10;
	}

	public long maxNumber10ForXLength(int length)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			sb.append(CHARS.charAt(CHARS.length() - 1));
		}
		return numberXTo10(sb.toString());
	}

	public NumberX(String chars)
	{
		this.CHARS = chars;
		this.BASE = chars.length();

		for (int i = 0; i < CHARS.length(); i++)
		{
			CHAR_INDEX_MAP.put(String.valueOf(CHARS.charAt(i)), i);
		}
	}

	public String CHARS = "0123456789abcdefghjkmnpqrstuvwxyz";
	private int BASE = CHARS.length();
	private Map<String, Integer> CHAR_INDEX_MAP = new HashMap<String, Integer>(
			BASE);


	public static void main(String args[]){

		for (int i=0; i<100; i++){
			String num = NumberX.Number62.number10ToX(System.currentTimeMillis());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(num);
		}


	}

}
