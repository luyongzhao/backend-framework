package com.lyz.backend.framework.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.jsoup.Jsoup;
//import org.jsoup.safety.Whitelist;

public class StringHelper
{
	public static String formatException(Throwable e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.close();
		String error = sw.toString();
		return error;
	}

	/**
	 * 每四个字符通过一个以split分割
	 * 
	 * @param code
	 * @return
	 */
	public static String formatCode(String code, char split, int splitLength)
	{
		
		if (code == null || code.length() <= splitLength)
		{

			return code;
		}

		// /* 去除分隔线 */
		// code = code.replace(split, "");

		StringBuffer builder = new StringBuffer();
		for (int i = 0; i < code.length(); i++)
		{

			builder.append(code.charAt(i));
			if ((i + 1) % splitLength == 0 && (i + 1) < code.length())
			{

				builder.append(split);
			}
		}

		return builder.toString();
	}

	/**
	 * 获取掩盖字符串
	 * 
	 * @param srcStr
	 * @param start
	 *            从0开始
	 * @param end
	 *            不包含
	 * @return
	 */
	public static String getMaskStr(String srcStr, int start, int end)
	{
		if (StringUtils.isBlank(srcStr))
		{
			return srcStr;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < srcStr.length(); i++)
		{
			if (i >= start && i < end)
			{
				sb.append("*");
			} else
			{
				sb.append(srcStr.charAt(i));
			}
		}

		return sb.toString();
	}

	/**
	 * 解析文件的后缀名,
	 * 
	 * @param fileName
	 * @return
	 */
	public static String parseFileExt(String fileName)
	{
		if (StringUtils.isBlank(fileName))
		{
			return null;
		} else
		{
			int idx = fileName.lastIndexOf(".");
			if (idx != -1)
			{
				return fileName.substring(idx + 1).toLowerCase();
			} else
			{
				return null;
			}
		}
	}

	public static String parseFileExt(String fileName, Set<String> allow)
	{
		String ext = parseFileExt(fileName);
		if (ext != null && allow.contains(ext))
		{
			return ext;
		} else
		{
			return null;
		}
	}

	public static String parseExtFromURL(String url)
	{
		try
		{
			URI uri = new URI(url);
			String file = uri.getPath();
			file = file.substring(file.lastIndexOf("."));
			return file;
		} catch (URISyntaxException e)
		{
		} finally
		{
		}
		return null;
	}

	public static String parseExtFromURL(String url, Set<String> allow)
	{
		String ext = parseExtFromURL(url);
		if (ext != null && allow.contains(ext))
		{
			return ext;
		} else
		{
			return null;
		}

	}

	public static List<String> stringToLineList(String str)
	{
		List<String> list = null;
		if (StringUtils.isNotBlank(str))
		{
			StringReader sr = new StringReader(str);
			try
			{
				list = IOUtils.readLines(sr);
			} catch (IOException e)
			{
				e.printStackTrace();
			} finally
			{
				IOUtils.closeQuietly(sr);
			}
		}
		if (list == null)
		{
			return new ArrayList<String>();
		} else
		{
			return list;
		}
	}
/**
	public static String cleanHtmlBaisc(String content)
	{
		if (StringUtils.isNotBlank(content))
		{
			return Jsoup.clean(
					content,
					Whitelist.basic().addTags(new String[] { "img" })
							.addAttributes("img", new String[] { "align", "alt", "height", "src", "title", "width" })
							.addProtocols("img", "src", new String[] { "/", "http", "https" }).addTags("span")
							.addAttributes("span", new String[] { "style" }));
		} else
		{
			return content;
		}
	}
*/
	/**
	 * 判断一个字符串的显示长度 中文显示两个长度 其他显示一个长度
	 * 
	 * @param s
	 * @return
	 */
	public static int getLengthFixCN(String s)
	{
		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < s.length(); i++)
		{
			// 获取一个字符
			String temp = s.substring(i, i + 1);
			// 判断是否为中文字符
			if (temp.matches(chinese))
			{
				// 中文字符长度为1
				valueLength += 2;
			} else
			{
				// 其他字符长度为0.5
				valueLength += 1;
			}
		}
		// 进位取整
		return Double.valueOf(Math.ceil(valueLength)).intValue();
	}

	/**
	 * 将一个字符串按照固定长度分隔成多个字符串
	 * 
	 * @param content
	 * @param maxSingleLength
	 * @return
	 */
	public static List<String> splitByFixLength(String content, int maxSingleLength)
	{

		List<String> array = new ArrayList<String>();

		if (StringUtils.isBlank(content) || maxSingleLength <= 0)
		{
			return null;
		}
		/* 不足一个长度，则返回一个 */
		if (content.length() <= maxSingleLength)
		{
			array.add(content);
			return array;
		}

		for (int i = 0; i < content.length(); i = i + maxSingleLength)
		{

			int end = (i + maxSingleLength) > content.length() ? content.length() : (i + maxSingleLength);
			array.add(content.substring(i, end));
		}

		return array;
	}

	public static boolean isEmpty(String str)
    {
        return null == str || str.trim().length()==0?true:false;
    }
	
	public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
	
	public static void main(String[] args)
	{
		// System.out.println(getLengthFixCN("kirito"));

		// List<String> list =
		// StringHelper.splitByFixLength("abkdajfaja中国jdfa;jdfajjka;", 5);
		// for(String sub : list){
		// System.out.println(sub);
		// }

//		System.out.println(StringHelper.formatCode("123456788900", '-', 4));
//		String s = "a s d gjdkls; s s ";
//		System.out.println(replaceBlank(s));
		System.out.println(isEmpty(" "));
	}

}
