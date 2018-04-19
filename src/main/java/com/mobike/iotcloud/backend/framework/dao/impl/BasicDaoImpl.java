package com.mobike.iotcloud.backend.framework.dao.impl;

import com.mobike.iotcloud.backend.framework.controller.bean.PageLimit;
import com.mobike.iotcloud.backend.framework.dao.BasicDao;
import com.mobike.iotcloud.backend.framework.dao.dto.DtoIdxSupport;
import com.mobike.iotcloud.backend.framework.dao.dto.DtoSupport;
import com.mobike.iotcloud.backend.framework.util.CollectionUtil;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Service("basicDao")
public class BasicDaoImpl implements BasicDao{

    private int asyncBatchSize = 200;
    private DAO dao = new DAO();
    @Autowired
    private SessionFactory sessionFacotry;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.yumo.core.module.service.IDBService#listOnlyObject(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public <T> T listOnlyObject(String hql, Object... params)
    {
        Session session = sessionFacotry.openSession();
        try
        {
            return dao.listOnlyObject(session, hql, params);
        } finally
        {
            sessionClose(session);
        }
    }

    private void sessionClose(Session session)
    {
        if (session != null)
            session.close();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.yumo.core.module.service.IDBService#delete(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public void delete(String hql, Object... args)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            dao.delete(session, hql, args);
            ts.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.yumo.core.module.service.IDBService#list(java.lang.Class)
     */
    @Override
    public <T extends DtoSupport> List<T> list(Class<T> c)
    {
        Session session = sessionFacotry.openSession();
        try
        {
            return dao.list(session, c);
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.yumo.core.module.service.IDBService#list(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public <T> List<T> list(String hql, Object... args)
    {
        Session session = sessionFacotry.openSession();
        try
        {
            return dao.list(session, hql, args);
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.yumo.core.module.service.IDBService#save(T)
     */
    @Override
    public <T extends DtoSupport> void save(T dto)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            dao.save(session, dto);
            ts.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.yumo.core.module.service.IDBService#save(T)
     */
    @Override
    public <T extends DtoSupport> void save(T... dtos)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            dao.save(session, dtos);
            ts.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see com.yumo.core.module.service.IDBService#save(java.util.Collection)
     */
    @Override
    public <T extends DtoSupport> void save(Collection<T> list)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            dao.save(session, list);
            ts.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.yumo.core.module.service.IDBService#delete(T)
     */
    @Override
    public <T extends DtoSupport> void delete(T dto)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            dao.delete(session, dto);
            ts.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.yumo.core.module.service.IDBService#deleteAll(java.util.Collection)
     */
    @Override
    public <T extends DtoSupport> void deleteAll(Collection<T> collection)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            dao.deleteAll(session, collection);
            ts.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.yumo.core.module.service.IDBService#get(K, java.lang.Class)
     */
    @Override
    public <T extends DtoSupport, K extends Serializable> T get(K id, Class<T> c)
    {
        Session session = sessionFacotry.openSession();
        try
        {
            return dao.get(session, id, c);
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.yumo.core.module.service.IDBService#update(T)
     */
    @Override
    public <T extends DtoSupport> void update(T... dto)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            dao.update(session, dto);
            ts.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.yumo.core.module.service.IDBService#update(java.util.Collection)
     */
    @Override
    public <T extends DtoSupport> void update(Collection<T> list)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            dao.update(session, list);
            ts.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.yumo.core.module.service.IDBService#listWithNameValues(java.lang.
     * String, java.util.List, java.util.List)
     */
    @Override
    public <T extends DtoSupport> List<T> listWithNameValues(String hql, List<String> names, List<Object> args)
    {
        Session session = sessionFacotry.openSession();

        try
        {
            return dao.listWithNameValues(session, hql, names, args);
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.yumo.core.module.service.IDBService#listLimit(java.lang.String,
     * java.lang.Integer, java.lang.Integer, java.lang.Object[])
     */
    @Override
    public <T extends DtoSupport> List<T> listLimit(String hql, Integer from, Integer rowCount, Object... params)
    {
        Session session = sessionFacotry.openSession();

        try
        {
            return dao.listLimit(session, hql, from, rowCount, params);
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.yumo.core.module.service.IDBService#listWithNoCache(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public <T extends DtoSupport> List<T> listWithNoCache(String hql, Object... params)
    {
        Session session = sessionFacotry.openSession();

        try
        {
            return dao.listWithNoCache(session, hql, params);
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.yumo.core.module.service.IDBService#listPageLimit(com.yumo.core.module
     * .controller.PageLimit)
     */
    @Override
    public PageLimit listPageLimit(PageLimit limit)
    {
        Session session = sessionFacotry.openSession();

        try
        {
            return dao.listPageLimit(session, limit);
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.yumo.core.module.service.IDBService#update(java.lang.String,
     * java.lang.String[], java.lang.Object[])
     */
    @Override
    public Integer updateHQL(String hql, String[] paramName, Object[] paramValue)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            Integer i = dao.updateHQL(session, hql, paramName, paramValue);
            ts.commit();
            return i;
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }
    }

    public Integer updateHQL(String hql, Object... paramValues)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            Integer i = dao.updateHQL(session, hql, paramValues);
            ts.commit();
            return i;
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.yumo.core.module.service.IDBService#executeQuery(org.springframework
     * .orm.hibernate3.HibernateCallback)
     */
    @Override
    public <T> T executeQuery(HibernateCallback<T> callback)
    {
        Session session = sessionFacotry.openSession();

        try
        {
            session.beginTransaction();
            T t = callback.doInHibernate(session);
            session.getTransaction().commit();
            return t;
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.yumo.core.module.service.IDBService#executeUpdate(org.springframework
     * .orm.hibernate3.HibernateCallback)
     */
    @Override
    public <T> T executeUpdate(HibernateCallback<T> callback)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            T t = callback.doInHibernate(session);
            ts.commit();
            return t;
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }
    }

    public Integer idxMax(Class<? extends DtoIdxSupport> cls)
    {
        return this.idxMax(cls, DtoIdxSupport.DEFALUT_IDX);
    }

    public Integer idxMax(Class<? extends DtoIdxSupport> cls, Integer defaultValue)
    {
        Session session = sessionFacotry.openSession();
        try
        {
            Integer idx = (Integer) dao.listOnlyObject(session, "select max(idx) from " + cls.getName());
            if (idx == null)
            {
                return defaultValue != null ? defaultValue : DtoIdxSupport.DEFALUT_IDX;
            } else
            {
                return idx;
            }
        } finally
        {
            sessionClose(session);
        }
    }

    public Integer idxMin(Class<? extends DtoIdxSupport> cls)
    {
        return this.idxMin(cls, DtoIdxSupport.DEFALUT_IDX);
    }

    public Integer idxMin(Class<? extends DtoIdxSupport> cls, Integer defaultValue)
    {
        Session session = sessionFacotry.openSession();
        try
        {

            Integer idx = (Integer) this.dao.listOnlyObject(session, "select min(idx) from " + cls.getName());
            if (idx == null)
            {
                return defaultValue != null ? defaultValue : DtoIdxSupport.DEFALUT_IDX;
            } else
            {
                return idx;
            }
        } finally
        {
            sessionClose(session);
        }

    }

    public boolean idxMoveToTop(Class<? extends DtoIdxSupport> cls, Serializable id)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            DtoIdxSupport dto = (DtoIdxSupport) this.dao.get(session, id, cls);
            if (dto == null)
            {
                return false;
            }

            dto.setIdx(this.idxMax(cls) + 1);

            dao.updateHQL(session, "update " + cls.getName() + " set idx=? where id=?", dto.getIdx(), id);
            ts.commit();
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            return false;
        } finally
        {
            sessionClose(session);
        }
    }

    public boolean idxMoveToBottom(Class<? extends DtoIdxSupport> cls, Serializable id)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {

            DtoIdxSupport dto = (DtoIdxSupport) this.dao.get(session, id, cls);
            if (dto == null)
            {
                return false;
            }

            dto.setIdx(this.idxMin(cls) - 1);
            this.dao.updateHQL(session, "update " + cls.getName() + " set idx=? where id=?", dto.getIdx(), id);
            ts.commit();
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            return false;
        } finally
        {
            sessionClose(session);
        }
    }

    public boolean idxMoveToDown(Class<? extends DtoIdxSupport> cls, Serializable id, String adtionalWhere,
                                 List<Object> adtionalArgs)
    {

        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            DtoIdxSupport dto = (DtoIdxSupport) this.dao.get(session, id, cls);
            if (dto == null)
            {
                ts.commit();
                return false;
            }
            DtoIdxSupport next = null;
            if (adtionalWhere != null)
            {
                if (adtionalArgs == null)
                {
                    adtionalArgs = new ArrayList<Object>();
                }
                adtionalArgs.add(dto.getIdx());

                next = (DtoIdxSupport) this.dao.listOnlyObject(session, "from " + cls.getName() + " t where "
                                + adtionalWhere + " and idx<? order by idx desc",
                        CollectionUtil.collectionToArray(adtionalArgs, new Object[adtionalArgs.size()]));
            } else
            {
                next = (DtoIdxSupport) this.dao.listOnlyObject(session, "from " + cls.getName()
                        + " t where idx<? order by idx desc", dto.getIdx());
            }
            if (next == null)
            {
                ts.commit();
                return false;
            } else
            {
                Integer tmp = dto.getIdx();
                dto.setIdx(next.getIdx());
                next.setIdx(tmp);

                this.dao.updateHQL(session, "update " + cls.getName() + " set idx=:idx where id=:id", new String[] {
                        "idx", "id" }, new Object[] { dto.getIdx(), id });

                this.dao.updateHQL(session, "update " + cls.getName() + " set idx=:idx where id=:id", new String[] {
                        "idx", "id" }, new Object[] { next.getIdx(), next.getId() });
                ts.commit();
                return true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            return false;
        } finally
        {
            sessionClose(session);
        }
    }

    public boolean idxMoveToUp(Class<? extends DtoIdxSupport> cls, Serializable id, String adtionalWhere,
                               List<Object> adtionalArgs)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {

            DtoIdxSupport dto = (DtoIdxSupport) this.dao.get(session, id, cls);
            if (dto == null)
            {
                ts.commit();
                return false;
            }
            DtoIdxSupport prev = null;
            if (adtionalWhere != null)
            {
                if (adtionalArgs == null)
                {
                    adtionalArgs = new ArrayList<Object>();
                }
                adtionalArgs.add(dto.getIdx());

                prev = (DtoIdxSupport) this.dao.listOnlyObject(session, "from " + cls.getName() + " t where "
                                + adtionalWhere + " and idx>? order by idx",
                        CollectionUtil.collectionToArray(adtionalArgs, new Object[adtionalArgs.size()]));
            } else
            {
                prev = (DtoIdxSupport) this.dao.listOnlyObject(session, "from " + cls.getName()
                        + " t where idx>? order by idx", dto.getIdx());
            }
            if (prev == null)
            {
                ts.commit();
                return false;
            } else
            {
                Integer tmp = dto.getIdx();
                dto.setIdx(prev.getIdx());
                prev.setIdx(tmp);

                this.dao.updateHQL(session, "update " + cls.getName() + " set idx=:idx where id=:id", new String[] {
                        "idx", "id" }, new Object[] { dto.getIdx(), id });

                this.dao.updateHQL(session, "update " + cls.getName() + " set idx=:idx where id=:id", new String[] {
                        "idx", "id" }, new Object[] { prev.getIdx(), prev.getId() });
                ts.commit();
                return true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            return false;
        } finally
        {
            sessionClose(session);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.yumo.core.module.service.IDBService#getSessionFacotry()
     */
    @Override
    public SessionFactory getSessionFacotry()
    {
        return sessionFacotry;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.yumo.core.module.service.IDBService#setSessionFacotry(org.hibernate
     * .SessionFactory)
     */
    @Override
    public void setSessionFacotry(SessionFactory sessionFacotry)
    {
        this.sessionFacotry = sessionFacotry;
    }

    @Override
    public <T extends DtoSupport> void delete(Class<T> cls)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            dao.delete(session, "delete from " + cls.getName());
            ts.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> listWithNativeSQL(final String sql, final Object... args)
    {
        return this.executeQuery(new HibernateCallback<List<T>>()
        {
            @Override
            public List<T> doInHibernate(Session s) throws HibernateException
            {
                SQLQuery query = s.createSQLQuery(sql);
                if (args != null && args.length > 0)
                {
                    for (int i = 0; i < args.length; i++)
                    {
                        query.setParameter(i, args[i]);
                    }
                }
                return query.list();
            }
        });
    }

    public Long count(final String hql, final Object... args)
    {
        Number num = this.listOnlyObject(hql, args);
        if (num == null)
        {
            return 0L;
        } else
        {
            return num.longValue();
        }
    }

    public Double sum(final String hql, final Object... args)
    {
        Object obj = this.listOnlyObject(hql, args);
        if (obj == null)
        {
            return null;
        }

        Number num = (Number) obj;

        return num.doubleValue();
    }

    public Long countWithNativeSQL(final String sql, final Object... args)
    {
        return this.executeQuery(new HibernateCallback<Long>()
        {
            @Override
            public Long doInHibernate(Session s) throws HibernateException
            {

                SQLQuery query = s.createSQLQuery(sql);
                if (args != null && args.length > 0)
                {
                    for (int i = 1; i <= args.length; i++)
                    {
                        query.setParameter(i-1, args[i-1]);
                    }
                }

                List list = query.list();
                if (list != null && list.size() > 0)
                {
                    Number n = (Number) list.get(0);
                    return n.longValue();
                } else
                {
                    return 0L;
                }
            }
        });
    }

    @Override
    public <T extends DtoSupport> void asyncBatchSave(T dto)
    {
        if (asyncBatchQueueToDB == null)
        {// 要发消息必然要先启动消息消费者
            startAsyncBatchConsumerThread();
        }

        asyncBatchQueueToDB.add(dto);
    }

    // 这里不能使用static的队列， 需要保证每个database实例(就是一个database) 一个队列,而不是所有database是一个队列
    private BlockingQueue<DtoSupport> asyncBatchQueueToDB = null;

    private synchronized void startAsyncBatchConsumerThread()
    {
        if (asyncBatchQueueToDB == null)
        {
            asyncBatchQueueToDB = new LinkedBlockingDeque<DtoSupport>();

            Thread consumerThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    while (true)
                    {
                        try
                        {
                            DtoSupport dto = asyncBatchQueueToDB.take();

                            if (asyncBatchQueueToDB.size() > 0)
                            {
                                int i = 0;
                                List<DtoSupport> list = new LinkedList<DtoSupport>();
                                list.add(dto);

                                while (i < asyncBatchSize)
                                {
                                    dto = asyncBatchQueueToDB.poll();
                                    if (dto != null)
                                    {
                                        list.add(dto);
                                        i++;
                                    } else
                                    {
                                        break;
                                    }
                                }

                                save(list);
                            } else
                            {
                                save(dto);
                            }
                        } catch (Throwable e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            });

            // 设置为后台线程, 主线程退出时，放置阻塞造成不能自动退出
            consumerThread.setDaemon(true);
            consumerThread.setName("AsyncBatchDatabaseSaveThread");
            consumerThread.start();
        }
    }

    @Override
    public <T extends DtoSupport> void saveOrUpdate(T... dtos)
    {
        Session session = sessionFacotry.openSession();
        Transaction ts = session.beginTransaction();

        try
        {
            for (T dto : dtos)
            {
                dao.saveOrUpdate(session, dto);
            }

            ts.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
            ts.rollback();
            throw new RuntimeException(e);
        } finally
        {
            sessionClose(session);
        }

    }
}
