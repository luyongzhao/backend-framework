package com.mobike.iotcloud.backend.framework.dao;


import com.mobike.iotcloud.backend.framework.controller.bean.PageLimit;
import com.mobike.iotcloud.backend.framework.dao.dto.DtoIdxSupport;
import com.mobike.iotcloud.backend.framework.dao.dto.DtoSupport;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface BasicDao
{
	public abstract <T> T listOnlyObject(String hql, Object... params);

	public abstract void delete(String hql, Object... args);

	public abstract <T extends DtoSupport> void delete(Class<T> cls);

	public abstract <T extends DtoSupport> List<T> list(Class<T> c);

	public abstract <T> List<T> list(String hql, Object... args);

	public abstract <T> List<T> listWithNativeSQL(String sql, Object... args);

	public Long countWithNativeSQL(final String sql, final Object... args);

	public Long count(final String sql, final Object... args);

	public Double sum(final String sql, final Object... args);

	public abstract <T extends DtoSupport> void save(T dto);

	/**
	 * 批量归并入库:::每个数据库一个单独的进程， 可以用来保存某些优先级比较低的数据，例如日志等
	 * 
	 * @param dto
	 */
	public abstract <T extends DtoSupport> void asyncBatchSave(T dto);

	public abstract <T extends DtoSupport> void save(T... dtos);

	public abstract <T extends DtoSupport> void save(Collection<T> list);

	public abstract <T extends DtoSupport> void delete(T dto);

	public abstract <T extends DtoSupport> void deleteAll(Collection<T> collection);

	public abstract <T extends DtoSupport, K extends Serializable> T get(K id, Class<T> c);

	public abstract <T extends DtoSupport> void update(T... dto);

	public abstract <T extends DtoSupport> void saveOrUpdate(T... dto);

	public abstract <T extends DtoSupport> void update(Collection<T> list);

	public abstract <T extends DtoSupport> List<T> listWithNameValues(String hql, List<String> names, List<Object> args);

	public abstract <T extends DtoSupport> List<T> listLimit(String hql, Integer from, Integer rowCount,
                                                             Object... params);

	public abstract <T extends DtoSupport> List<T> listWithNoCache(String hql, Object... params);

	public abstract PageLimit listPageLimit(PageLimit limit);

	public abstract Integer updateHQL(String hql, String[] paramName, Object[] paramValue);

	public abstract Integer updateHQL(String hql, Object... paramValues);

	public Integer idxMax(Class<? extends DtoIdxSupport> cls);

	public Integer idxMax(Class<? extends DtoIdxSupport> cls, Integer defaultValue);

	public Integer idxMin(Class<? extends DtoIdxSupport> cls);

	public Integer idxMin(Class<? extends DtoIdxSupport> cls, Integer defaultValue);

	public boolean idxMoveToTop(Class<? extends DtoIdxSupport> cls, Serializable id);

	public boolean idxMoveToBottom(Class<? extends DtoIdxSupport> cls, Serializable id);

	public boolean idxMoveToDown(Class<? extends DtoIdxSupport> cls, Serializable id, String adtionalWhere,
                                 List<Object> adtionalArgs);

	public boolean idxMoveToUp(Class<? extends DtoIdxSupport> cls, Serializable id, String adtionalWhere,
                               List<Object> adtionalArgs);


}