package ${pkgName};

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yumo.core.cache.memcached.IMemcached;
import com.yumo.core.cache.redis.IRedis;
import com.yumo.core.module.service.IMongodb;
import com.yumo.core.module.facade.MemcachedFacadeSupport;
import com.yumo.taste.module.mongodb.${entityName};
import com.yumo.taste.module.facade.common.MemcachedKeys;
import com.yumo.taste.module.facade.common.Storages;
/**
 * ${entityDesc}信息
 * 
 * @author auto
 */
public class ${entityName}Facade  extends MemcachedFacadeSupport
{
	
	private static final Log log = LogFactory.getLog(${entityName}Facade.class);
	
	@Resource(name = Storages.Database.${entityName?uncap_first}Mongodb)
	private IMongodb ${entityName?uncap_first}Mongodb;
	
	@Resource(name = Storages.Redis.${entityName?uncap_first}Redis)
	private IRedis ${entityName?uncap_first}Redis;
	
	@Resource(name = Storages.Memcached.${entityName?uncap_first}Memcached)
	private IMemcached ${entityName?uncap_first}Memcached;
	
	
	public void cache(${entityName} ${entityName?uncap_first})
	{
		${entityName?uncap_first}Memcached.setJSON(MemcachedKeys.${entityName?upper_case} + ${entityName?uncap_first}.getId().toString(), ${entityName?uncap_first}, ${entityName}.class);
	}
	
	public void save(${entityName} ${entityName?uncap_first})
	{
		if (${entityName?uncap_first} != null)
		{
			${entityName?uncap_first}Mongodb.save(${entityName?uncap_first});
			cache(${entityName?uncap_first});
		}
	}

	public ${entityName} get(String id)
	{
		return jsonMemcachedGet(${entityName?uncap_first}Memcached, ${entityName?uncap_first}Mongodb, MemcachedKeys.${entityName?upper_case}, id, ${entityName}.class);
	}
	
	public void upsert(final ${entityName} ${entityName?uncap_first})
	{
		${entityName?uncap_first}Mongodb.upsert(${entityName?uncap_first});
		cache(${entityName?uncap_first});
	}
	
	public List<${entityName}> list(Collection<String> ids)
	{
		return jsonMemcachedList(${entityName?uncap_first}Memcached, ${entityName?uncap_first}Mongodb, MemcachedKeys.${entityName?upper_case}, ids, ${entityName}.class);
	}
	
	public Map<String, ${entityName}> map(Collection<String> ids)
	{
		return jsonMemcachedMap(${entityName?uncap_first}Memcached, ${entityName?uncap_first}Mongodb, MemcachedKeys.${entityName?upper_case}, ids, ${entityName}.class);
	}
	
	public List<${entityName}> all()
	{
		return ${entityName?uncap_first}Mongodb.listAll(${entityName}.class);
	}
}
