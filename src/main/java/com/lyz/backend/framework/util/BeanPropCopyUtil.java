package com.lyz.backend.framework.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BeanPropCopyUtil {
	
	/**
	 * 属性拷贝
	 * @param s 源对象
	 * @param t
	 * @return
	 */
	public static <T> T copy(Object s, Class<T> t){
		
		T instance = null;
		try {
			instance = t.newInstance();
			
			BeanUtils.copyProperties(s, instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return instance;
	}
	/**
	 * 
	 * @param sList 元对象列表
	 * @param t
	 * @return
	 */
	public static <T> List<T> copyList(Object sList, Class<T> t){
		
		if(sList == null){
			return null;
		}
		
		Set set = (Set)sList;
		
		List<T> tList = new ArrayList<T>();
		for(Object o : set){
			
			tList.add(copy(o, t));
		}
		
		return tList;
	}
}
