package com.mobike.iotcloud.backend.framework.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CookiesUtil
{
	/**
	 * 添加cookie
	 * 
	 * @param resp
	 * @param cookieName
	 * @param cookieValue
	 * @param domain
	 *            传递null为当前域名，可以指定上级域名
	 * @param path
	 *            传递null为根目录
	 * @param expireSeconds
	 *            传递空为不设置，关闭浏览器自动失效,传递0就是删除
	 */
	public static void addCookie(HttpServletResponse resp, String cookieName, String cookieValue,
			String domain, String path, Integer expireSeconds)
	{
		try
		{
			Cookie c = new Cookie(cookieName, URLEncoder.encode(cookieValue, "UTF-8"));
			if (StringUtils.isBlank(path))
			{
				c.setPath("/");
			} else
			{
				c.setPath(path);
			}

			if (StringUtils.isNotBlank(domain))
			{
				c.setDomain(domain);
			}

			if (expireSeconds != null)
			{
				c.setMaxAge(expireSeconds);
			}
			resp.addCookie(c);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void removeCookie(HttpServletResponse res, String cookieName, String path)
	{
		try
		{
			Cookie c = new Cookie(cookieName, "");
			c.setMaxAge(0);
			c.setPath(path);
			res.addCookie(c);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String getCookie(HttpServletRequest req, String cookieName)
	{
		String cookieValue = null;
		try
		{
			Cookie c[] = req.getCookies();
			if (c != null)
			{
				for (int n = 0; n < c.length; n++)
				{
					if (c[n].getName().equals(cookieName))
					{
						cookieValue = c[n].getValue();
						break;
					}
				}
			}

			if (StringUtils.isNotBlank(cookieValue))
			{
				cookieValue = URLDecoder.decode(cookieValue, "UTF-8");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return cookieValue;

	}
}
