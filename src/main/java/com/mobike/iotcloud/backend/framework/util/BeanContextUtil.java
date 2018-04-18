package com.mobike.iotcloud.backend.framework.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringBeanContext管理器<br />
 * 仅能在spring初始化完成后才可访问,否则可能出applicationContext为空<br />
 * 
 * @author leo
 * 
 */
@Component
public class BeanContextUtil implements ApplicationContextAware
{

	@Override
	public void setApplicationContext(ApplicationContext paramApplicationContext)
			throws BeansException
	{
		BeanContextUtil.applicationContext = paramApplicationContext;
	}

	/**
	 * 仅能在spring初始化完成后才可访问
	 */
	public static ApplicationContext applicationContext;
}
