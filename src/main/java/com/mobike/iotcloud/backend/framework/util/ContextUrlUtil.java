package com.mobike.iotcloud.backend.framework.util;

import javax.servlet.http.HttpServletRequest;

public class ContextUrlUtil {
	public static String getContextUrl(HttpServletRequest request){
		StringBuffer url = request.getRequestURL();  
		String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).toString();
		return tempContextUrl;
		
	}

}
