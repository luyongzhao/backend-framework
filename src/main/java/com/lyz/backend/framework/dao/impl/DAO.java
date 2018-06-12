package com.lyz.backend.framework.dao.impl;

import com.lyz.backend.framework.controller.bean.PageLimit;
import com.lyz.backend.framework.dao.dto.DtoSupport;
import org.hibernate.Query;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class DAO
{
	public <T> T listOnlyObject(Session session, String hql, Object... params)
	{
		List<T> list = this.listLimit(session, hql, 0, 1, params);
		if (list.size() > 0)
		{
			return list.get(0);
		} else
		{
			return null;
		}
	}

	public Integer delete(Session session, String hql, Object... args)
	{
		Query query = session.createQuery(hql);

		if (args != null && args.length > 0)
		{
			for (int i = 0; i < args.length; i++)
			{
				query.setParameter(i, args[i]);
			}
		}

		return query.executeUpdate();
	}

	/**
	 * 列表查询
	 * 
	 * @param hql
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> list(Session session, String hql, Object... args)
	{
		if (args == null)
		{
			return session.createQuery(hql).list();
		} else
		{
			Query query = session.createQuery(hql);
			for (int i = 0; i < args.length; i++)
			{
				query.setParameter(i, args[i]);
			}
			return query.list();
		}
	}

	/**
	 * 保存对象
	 * 
	 * @param dto
	 */
	public <T extends DtoSupport> void save(Session session, T dto)
	{
		session.save(dto);
	}

	public <T extends DtoSupport> void save(Session session, T... dtos)
	{
		for (T dto : dtos)
		{
			session.save(dto);
		}
	}

	/**
	 * 保存多个对象
	 * 
	 * @param list
	 */
	public <T extends DtoSupport> void save(Session session, Collection<T> list)
	{
		for (T o : list)
		{
			session.save(o);
		}
	}

	/**
	 * 获取单个对象
	 * 
	 * @param id
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends DtoSupport, K extends Serializable> T get(Session session, K id, Class<T> c)
	{
		return (T) session.get(c, id);
	}

	@SuppressWarnings("unchecked")
	public <T extends DtoSupport> List<T> list(Session session, Class<T> c)
	{
		return session.createCriteria(c).list();
	}

	/**
	 * 删除
	 * 
	 * @param dto
	 */
	public <T extends DtoSupport> void delete(Session session, T dto)
	{
		session.delete(dto);
	}

	public <T extends DtoSupport> void deleteAll(Session session, Collection<T> collection)
	{
		if (collection != null && collection.size() > 0)
		{
			for (T t : collection)
			{
				session.delete(t);
			}
		}
	}

	public <T extends DtoSupport> void update(Session session, T... dto)
	{
		for (T d : dto)
		{
			session.update(d);
		}
	}

	public <T extends DtoSupport> void update(Session session, Collection<T> list)
	{
		for (T o : list)
		{
			session.update(o);
		}
	}

	public <T extends DtoSupport> void saveOrUpdate(Session session, T... dtos)
	{
		for (T dto : dtos)
		{
			session.saveOrUpdate(dto);
		}
	}

	public <T extends DtoSupport> void saveOrUpdate(Session session, Collection<T> list)
	{
		for (T dto : list)
			session.saveOrUpdate(dto);
	}

	@SuppressWarnings("unchecked")
	public <T extends DtoSupport> List<T> listWithNameValues(Session session, String hql, List<String> names,
			List<Object> args)
	{
		Query query = session.createQuery(hql);

		if (names != null && names.size() > 0)
		{
			for (int i = 0; i < names.size(); i++)
			{
				query.setParameter(names.get(i), args.get(i));
			}
		}

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> listLimit(Session session, String hql, Integer from, Integer rowCount, Object[] params)
	{

		org.hibernate.Query query = session.createQuery(hql);
		if (params != null)
		{
			for (int i = 0; i < params.length; i++)
			{
				query.setParameter(i, params[i]);
			}
		}
		query.setFirstResult(from);
		query.setMaxResults(rowCount);

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public <T extends DtoSupport> List<T> listWithNoCache(Session session, String hql, Object... params)
	{

		org.hibernate.Query query = session.createQuery(hql);
		if (params != null)
		{
			for (int i = 0; i < params.length; i++)
			{
				query.setParameter(i, params[i]);
			}
		}

		List<T> list = query.list();
		session.clear();
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageLimit listPageLimit(Session session, PageLimit limit)
	{
		// 查询总记录数

		Query query = session.createQuery(limit.getCountHQL());
		for (String key : limit.getParams().keySet())
		{
			query.setParameter(key, limit.getParams().get(key));
		}

		Long count = (Long) query.uniqueResult();

		limit.setTotalResult(count);

		query = session.createQuery(limit.getListHQL());
		for (String key : limit.getParams().keySet())
		{
			query.setParameter(key, limit.getParams().get(key));
		}
		query.setFirstResult(limit.getPageDisplay() * (limit.getPageNumber() - 1));
		query.setMaxResults(limit.getPageDisplay());
		List<Object> list = query.list();
		limit.setList(list);

		return limit;
	}

	public Integer updateHQL(Session session, String hql, String paramName[], Object paramValue[])
	{
		Query query = session.createQuery(hql);
		if (paramName != null)
		{
			for (int i = 0; i < paramName.length; i++)
			{
				query.setParameter(paramName[i], paramValue[i]);
			}
		}
		return query.executeUpdate();
	}

	public Integer updateHQL(Session session, String hql, Object... args)
	{
		Query query = session.createQuery(hql);
		if (args != null && args.length > 0)
		{
			for (int i = 0; i < args.length; i++)
			{
				query.setParameter(i, args[i]);
			}
		}
		return query.executeUpdate();
	}
}
