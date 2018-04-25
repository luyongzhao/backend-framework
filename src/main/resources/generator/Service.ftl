package ${pkgName};

import ${entityPkgName}.${entityName};
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
* ${entityDesc}处理逻辑
*
* @author generator
*/
public interface ${entityName}Service {

    /**
    * 获取所有${entityDesc}
    * @return
    */
    List<${entityName}> all();


    /**
    * 分业获取${entityName}列表
    * @param pageNo 页码，从1开始，小于1默认返回第一页
    * @param pageSize 页大小，每页数据量
    * @return
    */
    List<${entityName}> listByPage(int pageNo, int pageSize);

    /**
    * 创建${entityDesc}，除了保存数据之外要做一些其他的逻辑
    * @param ${entityName?uncap_first}
    */
    void create(${entityName} ${entityName?uncap_first});


    /**
    * 更新${entityDesc}
    * @param ${entityName?uncap_first}
    * @return
    */
    void update(${entityName} ${entityName?uncap_first});


    /**
    * 删除${entityDesc}
    * @param id ${entityDesc}id
    * @return
    */
    void delete(String id);


    /**
    * 获取${entityDesc}
    * @param id ${entityDesc}id
    * @return
    */
    ${entityName} get(String id);

    /**
    * 根据id列表获取${entityDesc}列表，没有对应的${entityDesc}，${entityDesc}列表对应位置为null
    * @param ids ${entityDesc}id集合
    * @return 都不存在返回空列表，不返回null
    */
    List<${entityName}> list(Collection<String> ids);

    /**
    * 根据id列表获取id到${entityDesc}的映射
    * @param ids ${entityDesc}id集合
    * @return 都不存在返回空map，不返回null
    */
    Map<String, ${entityName}> map(Collection<String> ids);

    /**
    * 缓存${entityDesc}对象
    * @param ${entityName?uncap_first} ${entityDesc}对象
    * @return 都不存在返回空map，不返回null
    */
    void cache(${entityName} ${entityName?uncap_first});


}