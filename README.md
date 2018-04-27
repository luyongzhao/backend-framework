
##校验框架参考
https://blog.csdn.net/csyuyaoxiadn/article/details/56016359  
https://blog.csdn.net/wingkoo1986/article/details/70768730

##拦截器引用配置
interceptor:  
  list:  
    -className:com.mobike.iotcloud.guard.api.interceptor.InternalInterceptor  
      include:/iotcloud/guard/internal/*  
      exclude:  
    -className:com.mobike.iotcloud.backend.framework.interceptor.OpenApiInterceptor  
      include:/iotcloud/guard/**  
      exclude:/iotcloud/guard/internal/*  
      
##持久化缓存引用配置
persistent:  
    cache:  
        type: redis  
        host: 127.0.0.1  
        port: 6379  
        timeout: 5000  
        password:（没有可以不填写）
        
##基于redis的自增id生成器
id:  
    generator:  
        incr:  
            key: redis中的key名称  
            cacheSize:20--批量从redis中获取的缓存在本地的数量
            initValue: 10000--初始值
            

##基于redis的队列id生成（预先生成id放入队列）
id:  
    generator:  
        incr:  
            key: redis中的key名称  
            cacheSize:20--批量从redis中获取的缓存在本地的数量