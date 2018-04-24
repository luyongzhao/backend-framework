package ${pkgName};

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.PipelineBlock;
import com.yumo.core.cache.memcached.IMemcached;
import com.yumo.core.cache.redis.IRedis;
import com.yumo.core.module.facade.MemcachedFacadeSupport;
import com.yumo.core.module.service.IDatabase;
import com.yumo.core.uid.IDGenerator;
import com.yumo.xschool.module.dto.${entityName};
import com.yumo.xschool.module.facade.common.MemcachedKeys;
import com.yumo.xschool.module.facade.common.RedisKeys;
import com.yumo.xschool.module.facade.common.Storages;

/**
 * ${entityDesc}信息
 * 
 * @author auto
 */
public class ${entityName}Facade extends MemcachedFacadeSupport
{
	@Resource(name = Storages.Memcached.${moduleName}Memcached)
	private IMemcached ${moduleName}Memcached;

	@Resource(name = Storages.Database.${moduleName}Database)
	private IDatabase ${moduleName}Database;

	@Resource(name = Storages.Redis.${moduleName}Redis)
	private IRedis ${moduleName}Redis;

	@Resource(name = Storages.IDGenerator.commonIDGenerator)
	private IDGenerator commonIDGenerator;

	public ${entityName} get(String id)
	{
		return jsonMemcachedGet(${moduleName}Memcached, ${moduleName}Database, MemcachedKeys.${memKey}, id, ${entityName}.class);
	}

	public List<${entityName}> list(Collection<String> ids)
	{
		return jsonMemcachedList(${moduleName}Memcached, ${moduleName}Database, MemcachedKeys.${memKey}, ids, ${entityName}.class);
	}

	public Map<String, ${entityName}> map(Collection<String> ids)
	{
		return jsonMemcachedMap(${moduleName}Memcached, ${moduleName}Database, MemcachedKeys.${memKey}, ids, ${entityName}.class);
	}

	public void cache(${entityName} ${entityName?uncap_first})
	{
		${moduleName}Memcached.setJSON(MemcachedKeys.${memKey} + ${entityName?uncap_first}.getId(), ${entityName?uncap_first}, ${entityName}.class);
	}

	public void save(${entityName} ${entityName?uncap_first})
	{
		if (${entityName?uncap_first} != null)
		{
			Date now = new Date();
			${entityName?uncap_first}.setId(commonIDGenerator.nextStringID());
			${entityName?uncap_first}.setCreateTime(now);
			${entityName?uncap_first}.setUpdateTime(now);
			${moduleName}Database.save(${entityName?uncap_first});
			cache(${entityName?uncap_first});
		}
	}

	

	public List<${entityName}> all()
	{
		return ${moduleName}Database.list("from ${entityName}");
	}

	public void update(${entityName} ${entityName?uncap_first})
	{
		${entityName?uncap_first}.setUpdateTime(new Date());
		${moduleName}Database.update(${entityName?uncap_first});
		cache(${entityName?uncap_first});
	}

}
