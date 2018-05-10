package com.mobike.iotcloud.backend.framework.freemarker;

import org.apache.commons.lang.StringUtils;

import java.net.URLEncoder;

/**
 * html处理类
 * 
 * @author luyongzhao
 * 
 */
public class HTMLContent
{
	public static String urlEncode(String url)
	{
		if(StringUtils.isBlank(url))
		{
			return "";
		}

		return URLEncoder.encode(url);
	}
	
}
