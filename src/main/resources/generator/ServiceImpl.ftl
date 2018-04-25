package ${pkgName}.impl;


import com.mobike.iotcloud.backend.framework.cache.MemoryCache;
import com.mobike.iotcloud.backend.framework.dao.BasicDao;
import com.mobike.iotcloud.backend.framework.id.IDGenerator;
import com.mobike.iotcloud.backend.framework.service.MemcachedServiceSupport;
import ${entityPkgName}.${entityName};
import ${pkgName}.${entityName}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ${entityDesc}信息
 * 
 * @author generator
 */
@Service("${entityName?uncap_first}Service")
public class ${entityName}ServiceImpl extends MemcachedServiceSupport implements ${entityName}Service
{
	private static final String memoryKeyPrefix = "${entityName}_";

	@Autowired
	private BasicDao basicDao;

	@Autowired
	private MemoryCache<String,${entityName}> memoryCache;

	@Autowired
	private IDGenerator idGenerator;

	@Override
	public ${entityName} get(String id)
	{
		return memcachedGet(memoryCache,basicDao,memoryKeyPrefix,id, ${entityName}.class);
	}

	@Override
	public List<${entityName}> list(Collection<String> ids)
	{
		return memcachedList(memoryCache,basicDao,memoryKeyPrefix, ids, ${entityName}.class);
	}

    @Override
	public Map<String, ${entityName}> map(Collection<String> ids)
	{
		return memcachedMap(memoryCache,basicDao,memoryKeyPrefix, ids, ${entityName}.class);
	}

    @Override
	public void cache(${entityName} ${entityName?uncap_first})
	{
        memoryCache.put(memoryKeyPrefix+${entityName?uncap_first}.getId(),${entityName?uncap_first});
	}


    @Override
	public void create(${entityName} ${entityName?uncap_first})
	{
		if (${entityName?uncap_first} != null)
		{
			Date now = new Date();
			${entityName?uncap_first}.setId(idGenerator.nextStringID());
			${entityName?uncap_first}.setCreateTime(now);
			${entityName?uncap_first}.setUpdateTime(now);

        	memcachedSave(memoryCache,basicDao,memoryKeyPrefix,${entityName?uncap_first},${entityName?uncap_first}.getId());
		}
	}

	@Override
	public List<${entityName}> all()
	{
		return basicDao.list("from ${entityName}");
	}

	@Override
	public List<${entityName}> listByPage(int pageNo, int pageSize){

		if (pageNo < 1) {
			pageNo = 1;
		}

		return basicDao.listLimit("from ${entityName} ",(pageNo-1)*pageSize,pageSize);
	}

    @Override
	public void update(${entityName} ${entityName?uncap_first})
	{
		${entityName?uncap_first}.setUpdateTime(new Date());

        memcachedUpdate(memoryCache,basicDao,memoryKeyPrefix,${entityName?uncap_first},${entityName?uncap_first}.getId());
	}

	@Override
	public void delete(String id) {

		${entityName} ${entityName?uncap_first} = new ${entityName}();
		${entityName?uncap_first}.setId(id);

		memcachedDelete(memoryCache,basicDao,memoryKeyPrefix,${entityName?uncap_first},${entityName?uncap_first}.getId());
	}

}
