package com.mobike.iotcloud.backend.framework.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringEscape
{
	public static void main(String[] args)
	{
		String str = "<body>";
		str += "<div id=\"page\">";
		str += "<div id=\"header\">";
		str += "<div class=\"clearfix\"><div id=\"user_nav\">";
		str += "<a href=\"/login\" class=\"welcome\" title=\"登录\">您 -</a>";
		str += "<a href=\"/login\">登录</a>";
		str += "<a href=\"/signup\" class=\"nobg\">注册</a>";
		str += "</div>";
		str += "</div>";

		System.out.println(htmlEmptyWithMaxLength(str, 6, null));
	}

	public static String htmlEmptyWithMaxLength(String source, int lenght,
			String fix)
	{
		if (StringUtils.isBlank(source))
		{
			return source;
		}

		boolean needFix = (fix != null);

		if (needFix)
			lenght = lenght - fix.length();

		StringBuilder sb = new StringBuilder();

		int len = 0;
		boolean isTxt = true;
		for (int i = 0; i < source.length(); i++)
		{
			char c = source.charAt(i);
			if (c == '<')
			{
				isTxt = false;
			} else if (c == '>')
			{
				isTxt = true;
			} else if (isTxt && c != '\n' && c != '\r')
			{
				sb.append(c);

				len++;

				if (len == lenght)
				{
					if (needFix)
						sb.append(fix);
					break;
				}
			}
		}

		return sb.toString().trim();
	}

	public static String htmlEncode(String source)
	{
		return HtmlUtils.htmlEscape(source);
	}

	public static String transformSolrMetacharactor(String input)
	{
		input = HtmlUtils.htmlEscape(input);
		StringBuffer sb = new StringBuffer();
		String regex = "[+\\-&|!(){}\\[\\]^\"~*?:(\\)/]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find())
		{
			matcher.appendReplacement(sb, "");
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
}
