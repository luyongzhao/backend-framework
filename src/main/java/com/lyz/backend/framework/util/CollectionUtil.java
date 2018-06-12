package com.lyz.backend.framework.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.*;

public class CollectionUtil
{
	/**
	 * 判断两个集合中元素是否完全相同
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static <T> boolean equals(Set<T> s1, Set<T> s2)
	{
		if(isEmpty(s1) && isEmpty(s2))
		{
			return true;
		}else if(!isEmpty(s1) && !isEmpty(s2) && s1.size()==s2.size())
		{
			for(T t : s1)
			{
				//只要s1中有一个不再s2中，则说明不同
				if(!s2.contains(t))
				{
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	public static <T> List<T> convertCollection2List(Collection<T> coll)
	{
		if(coll == null)
		{
			return null;
		}
		
		return new ArrayList<T>(coll);
	}
	
	public static boolean isEmpty(Collection coll)
	{
		return coll==null || coll.isEmpty();
	}
	
	public static boolean isEmpty(Map map)
	{
		return map==null || map.isEmpty();
	}
	
	public static <T> T[] collectionToArray(Collection<T> c, T[] ar)
	{
		int i = 0;
		for (Iterator<T> it = c.iterator(); it.hasNext();)
		{
			ar[i] = it.next();
			i++;
		}

		return ar;
	}

	public static Set<String> stringToSet(String dashStr)
	{
		return stringToSet(dashStr, ",");
	}

	public static Set<String> stringToSet(String dashStr, String split)
	{
		if (StringUtils.isNotBlank(dashStr))
		{
			String[] strs = dashStr.split(split);
			Set<String> set = new HashSet<String>(strs.length);
			for (String str : strs)
			{
				if (StringUtils.isNotBlank(str))
				{
					set.add(str);
				}
			}
			return set;
		} else
		{
			return new HashSet<String>();
		}
	}

	public static List<String> stringToList(String dashStr)
	{
		return stringToList(dashStr, ",");
	}
	

	public static List<String> stringToList(String dashStr, String split)
	{
		if (StringUtils.isNotBlank(dashStr))
		{
			String[] strs = dashStr.split(split);
			List<String> list = new ArrayList<String>(strs.length);
			for (String str : strs)
			{
				if (StringUtils.isNotBlank(str))
				{
					list.add(StringUtils.trim(str));
				}
			}
			return list;
		} else
		{
			return new ArrayList<String>();
		}
	}
	
	public static List<Double> stringToDoubleList(String dashStr, String split)
	{
		if (StringUtils.isNotBlank(dashStr))
		{
			String[] strs = dashStr.split(split);
			List<Double> list = new ArrayList<Double>(strs.length);
			for (String str : strs)
			{
				if (StringUtils.isNotBlank(str))
				{
					double price = NumberUtils.toDouble(StringUtils.trim(str), -1);
					if(price < 0)
					{
						continue;
					}
					list.add(price);
				}
			}
			return list;
		} else
		{
			return new ArrayList<Double>();
		}
	}

	public static List<String> stringToListWithBlank(String dashStr)
	{
		if (StringUtils.isNotBlank(dashStr))
		{
			String[] strs = dashStr.split(",");
			List<String> set = new ArrayList<String>(strs.length);
			for (String str : strs)
			{
				set.add(str);
			}
			return set;
		} else
		{
			return new ArrayList<String>();
		}
	}
	/**
	 * 合并两个集合数据到新的集合
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static <T> Set<T> mergeSet(Set<T> list1, Set<T> list2)
	{
		
		Set<T> set = new HashSet<>();
		if(list1 != null)
		{
			set.addAll(list1);
		}
		
		if(list2 != null)
		{
			set.addAll(list2);
		}
		
		return set;
	}
	
	public static <T> List<T> mergeList(List<T> set1, List<T> set2)
	{
		
		List<T> newList = new ArrayList<>();
		if(set1 != null)
		{
			newList.addAll(set1);
		}
		
		if(set2 != null)
		{
			newList.addAll(set2);
		}
		
		return newList;
	}

	public static <T> List<T> collectionToList(Collection<T> set)
	{
		
		if(set == null)
		{
			return null;
		}
		
		return new ArrayList<>(set);
	}
	
	public static <T> Set<T> listToSet(List<T> list)
	{
		if(list == null)
		{
			return null;
		}
		
		return new HashSet(list);
	}
	
	public static <T> List<T> setToList(Set<T> set)
	{
		if(set == null)
		{
			return null;
		}
		
		return new ArrayList(set);
	}
	
	public static <K,T> Map<K,T> listToMap(List<T> list,String attrName)
	{
		
		if(isEmpty(list))
		{
			return null;
		}
		
		Map<K,T> map = new HashMap<K,T>();
		
		for(T t : list)
		{
			map.put((K)ClassUtil.getAttrVal(t, attrName), t);
		}
		
		return map;
	}

	public static List<Object> listAttr(List list, String attrName)
	{
		if(isEmpty(list))
		{
			return null;
		}
		
		List<Object> attrList = new ArrayList();
		
		for(Object obj : list)
		{
			attrList.add(ClassUtil.getAttrVal(obj, attrName));
		}
		
		return attrList;
	}
	
	
}
