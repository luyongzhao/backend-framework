package com.mobike.iotcloud.backend.framework.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ClassUtil {
	
	private static final Logger logger = Logger.getLogger(ClassUtil.class);
	/**
	 * 反射机制实例化类
	 * @param className
	 * @return
	 */
	public static Object newInstance(String className, Object[] args){
		if(className == null){
			return null;
		}
		Class cls = null;
		Class[] argsClass = null;
		/*构造参数*/
		if(args != null){
			argsClass = new Class[args.length];
			 for (int i = 0, j = args.length; i < j; i++) {                                
			        argsClass[i] = args[i].getClass();                                        
			 }	  
		}
		
		Object obj = null;
		try {
			cls = Class.forName(className);
			if(argsClass != null){
				Constructor con = cls.getConstructor(argsClass);
				obj = con.newInstance(args);
			}else{
				obj = cls.newInstance();
			}		
		} catch (ClassNotFoundException e) {
			logger.error("class not found!",e);
			return null;
		} catch (Exception e){
			logger.error(e);
			return null;
		} 
		
		return obj;
	}
	/**
	 * 从对像中获取指定属性的值
	 * @param obj
	 * @param attrName
	 * @return
	 */
	public static Object getAttrVal(Object obj, String attrName)
	{
		
		if(obj == null)
		{
			return null;
		}
		
		Field field = getField(obj.getClass(), attrName);
		
		if(field == null)
		{
			return null;
		}
		field.setAccessible(true); //设置些属性是可以访问的  
		
		try
		{
			return field.get(obj);
		} catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Field getField(Class clazz, String attrName)
	{
		
		Field field = null;
		try
		{
			field = clazz.getDeclaredField(attrName);
		} catch (Exception e)
		{
			field = null;
		}
		Class curClz = clazz;
		
		while(field == null)
		{
			Class superClz = curClz.getSuperclass();
			if(superClz == null)
			{
				break;
			}
			curClz = superClz;
			try
			{
				field = curClz.getDeclaredField(attrName);
			} catch (Exception e)
			{
				field = null;
			}
		}
		
		if(field == null)
		{
			return null;
		}
		field.setAccessible(true);
		return field;
	}
	/**
	 * 不同对象的相同属性之间进行属性值拷贝
	 * @param srcObj 源对象
	 * @param target 目标对象
	 */
	public static void getObjFromAttrCopy(Object srcObj, Object target){
		
		if(srcObj==null || target==null){
			return;
		}
		
		Method[] methods = target.getClass().getMethods();
		if (methods == null || methods.length == 0) {
			logger.warn("no method in class:" + target.getClass().getName());
		}
		
		Map<String, Field> name2Field = new HashMap<String, Field>();
		/*获取源对象成员变量*/
		for(Field field : srcObj.getClass().getFields()){
			
			name2Field.put(field.getName(), field);
		}

		try {
			for (Method method : methods) {
				if(!method.getName().startsWith("set")){
					continue;
				}
				/*获取属性名称*/
				String filedName = getFieldName(method.getName());
				/*获取源属性*/
				Field srcF = null;
				try {
					srcF = srcObj.getClass().getDeclaredField(filedName);
				} catch (Exception e) {
					continue;
				}
				srcF.setAccessible(true);
				Object obj = srcF.get(srcObj);
				if(obj == null){
					continue;
				}
				/*通过set方法赋值*/
				method.invoke(target, obj);
			}
		} catch (Exception e) {
			logger.error("fail to getObjFromAttrCopy",e);
		}
	}
	/**
	 * 从多个对象实例拼凑一个对象,通过属性名称赋值，例如源对象有一个属性名称为name,目标对象中的name将被赋值，并且需要两个name的类型相同
	 * @param <Object>
	 * @param dstCls
	 * @param obj
	 * @return
	 */
	public static <T> Object copyAttr(Class<T> dstCls, Object... objs){
		
		if(objs==null || objs.length==0){
			return null;
		}
		Object dstObj = newInstance(dstCls.getName(), null);
		for(Object obj : objs){
			getObjFromAttrCopy(obj, dstObj);
		}
		
		return dstObj;
	}
	
	/**
	 * 获取字段名称，字段必须首字母小写
	 * 
	 * @param setMethodName
	 * @return
	 */
	public static String getFieldName(String setMethodName) {

		String tmpName = setMethodName.substring(3);
		/* 将第一个大写字母小写 */
		String firstChar = (tmpName.charAt(0) + "").toLowerCase();

		return firstChar + tmpName.substring(1);
	}
	
	public static <T> Type getClassOrSuperClassParmType(Class<T> dstCls,String param){
		Field field = null;
		Class<? super T> superClazz = dstCls.getSuperclass();
		Field superField = null;
		try {
			field = dstCls.getDeclaredField(param);
			if(field == null){
				superField = superClazz.getDeclaredField(param);
				if(superField != null){
					return superField.getType();
				}
			}else {
				return field.getType();
			}
		} catch (NoSuchFieldException e) {
			try {
				superField = superClazz.getDeclaredField(param);
				if(superField != null){
					return superField.getType();
				}
			} catch (NoSuchFieldException | SecurityException e1) {
				e1.printStackTrace();
			}
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static void main(String[] args) throws NoSuchFieldException, SecurityException {
//		Type type = getSuperClassParmType(Page.class,"sortIndex");
//		System.out.println(type.toString());
		
//		PDPStudent stu = new PDPStudent();
//		stu.setId("11");
//		
//		String id = (String)getAttrVal(stu, "id");
//		System.out.println(id);
		
//		System.out.println("aaa.bb".split("\\.")[1]);
	}
	public static boolean contains(Class<? extends Object> clazz,
			String attrName)
	{
		try
		{
			clazz.getDeclaredField(attrName);
		} catch (Exception e)
		{
			return false;
		}
		
		return true;
	}
	/**
	 * 将attrObj赋值给targetObject中属性名为attrName的属性
	 * @param targetObj
	 * @param attrName
	 * @param attrObj
	 */
	public static void setValue(Object targetObj, String attrName, Object attrObj)
	{
		
		//构造set方法名称
		String setMethodName = createSetMethodName(attrName);
		
		try
		{
			Method method = targetObj.getClass().getDeclaredMethod(setMethodName, attrObj.getClass());
			
			//调用方法赋值
			method.invoke(targetObj, attrObj);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static String createSetMethodName(String attrName)
	{
		return "set"+StringUtils.capitalize(attrName);
	}
	
	
	
}
