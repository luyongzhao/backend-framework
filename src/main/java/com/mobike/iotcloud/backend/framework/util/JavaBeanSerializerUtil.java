package com.mobike.iotcloud.backend.framework.util;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.FieldSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.util.DeserializeBeanInfo;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

@SuppressWarnings(
		{ "unchecked" })
public class JavaBeanSerializerUtil
{
	public static <K, V> Map<K, V> json2Map(String text)
	{
		return (Map<K, V>) JSON.parseObject(text);
	}

	public static <T> T json2Object(String text, T bean)
	{
		Map map = JSON.parseObject(text);
		return map2Object(map, bean);
	}

	public static String object2Json(Object bean)
	{
		return JsonUtil.toJSONString(bean);
	}

	public static Map<String, String> describe(Object bean)
	{
		return object2Map(bean, false, true, bean.getClass());
	}

	public static Map<String, String> describe(Object bean, Class cls)
	{
		return object2Map(bean, false, true, cls);
	}

	public static Map<String, Object> object2MapWithoutNullValue(Object bean)
	{
		return object2Map(bean, false, false, bean.getClass());
	}

	public static Map<String, Object> object2MapWithoutNullValue(Object bean, Class cls)
	{
		return object2Map(bean, false, false, cls);
	}

	public static Map<String, Object> object2Map(Object bean)
	{
		return object2Map(bean, true, false, bean.getClass());
	}

	/**
	 * 对象转map
	 *
	 * @param bean
	 *            对象
	 * @param withNullValue
	 *            包含空值
	 * @param toString
	 *            转化为string类型
	 * @return
	 */
	@SuppressWarnings(
			{ "rawtypes" })
	public static Map object2Map(Object bean, boolean withNullValue, boolean toString, Class cls)
	{
		try
		{
			JavaBeanSerializer beanInfo = getJavaBeanSerializer(cls);

			FieldSerializer[] getters = beanInfo.getGetters();
			Map map = new HashMap(getters.length * 2);

			for (FieldSerializer getter : getters)
			{
				// 得到property对应的getter方法
				Object value = getter.getPropertyValue(bean);
				if (value != null)
				{
					if (value instanceof Date)
					{
						value = String.valueOf(((Date) value).getTime());
					} else
					{
						if (toString)
						{
							value = value.toString();
						}
					}

					Field g = getter.getField();
					if (g != null)
					{
						map.put(g.getName(), value);
					}
				} else if (withNullValue)
				{
					map.put(getter.getField().getName(), value);
				}
			}
			return map;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T map2Object(Map map, Class<T> cls)
	{
		T bean = null;
		try
		{
			bean = cls.newInstance();
			return map2Object(map, bean);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings(
			{ "rawtypes" })
	public static <T> T map2Object(Map map, T bean)
	{
		try
		{
			DeserializeBeanInfo beanInfo = getJavaBeanDeSerializer(bean.getClass());

			List<FieldInfo> setters = beanInfo.getFieldList();

			for (FieldInfo setter : setters)
			{
				// 得到property对应的setter方法
				Object value = map.get(setter.getName());
				if (value != null)
				{
					value = TypeUtils.cast(value, setter.getFieldClass(),
							ParserConfig.getGlobalInstance());

					setter.set(bean, value);
				}
			}
			return bean;
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private static Map<Class, JavaBeanSerializer> classGetMethodCache = new HashMap<Class, JavaBeanSerializer>();
	private static Map<Class, DeserializeBeanInfo> classSetMethodCache = new HashMap<Class, DeserializeBeanInfo>();

	private static JavaBeanSerializer getJavaBeanSerializer(Class cls)
			throws IntrospectionException
	{
		JavaBeanSerializer beanInfo = classGetMethodCache.get(cls);
		if (beanInfo == null)
		{
			beanInfo = new JavaBeanSerializer(cls);
			classGetMethodCache.put(cls, beanInfo);
		}
		return beanInfo;
	}

	private static DeserializeBeanInfo getJavaBeanDeSerializer(Class cls)
			throws IntrospectionException
	{
		DeserializeBeanInfo beanInfo = classSetMethodCache.get(cls);
		if (beanInfo == null)
		{
			beanInfo = DeserializeBeanInfo.computeSetters(cls, cls);
			classSetMethodCache.put(cls, beanInfo);
		}
		return beanInfo;
	}
}
