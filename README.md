##包结构
+ annotation：自定义注解
+ auth：授权加密算法
+ autoconfig：自动配置，仿照springboot自动配置实现的自定义自动配置
+ cache：缓存相关类封装
+ controller：通用controller封装以及相关响应、校验类封装
+ dao：通用数据库操作类
+ exception：web通用异常捕获类
+ freemarker：模版引擎相关类封装
+ generator：代码生成模块，从数据表反转生成实体映射、service类和controller类
+ http：http客户端相关类封装
+ id：封装各种id生成类
+ interceptor：通用拦截器模块
+ service：通用服务类封装
+ util：通用工具类封装
+ validate：校验框架的自定义扩展
+ weixin：微信公众号、支付接入类封装

##数据表命名规则
1. 数据表名称多个单词通过下划线分割
2. 数据表字段采用驼峰写法，首字母小写

##校验框架参考
+ [https://blog.csdn.net/csyuyaoxiadn/article/details/56016359](https://blog.csdn.net/csyuyaoxiadn/article/details/56016359)
+ [https://blog.csdn.net/wingkoo1986/article/details/70768730](https://blog.csdn.net/wingkoo1986/article/details/70768730)

##application.yml相关配置
###拦截器引用配置
<pre><code>
interceptor:  
    list:  
        -className:com.mobike.iotcloud.guard.api.interceptor.InternalInterceptor  
        include:/iotcloud/guard/internal/*  
        exclude: （没有可以不填写）
        -className:com.mobike.iotcloud.backend.framework.interceptor.OpenApiInterceptor  
        include:/iotcloud/guard/**  
        exclude:/iotcloud/guard/internal/*  
</code></pre>
      
###持久化缓存引用配置
<pre><code>
persistent:  
    cache:  
        type: redis  
        host: 127.0.0.1  
        port: 6379  
        timeout: 5000  
        password:（没有可以不填写）
</code></pre>     
  
        
###基于redis的自增id生成器
<pre><code>
id:  
    generator:  
        incr:  
            key: redis中的key名称  
            cacheSize:20--批量从redis中获取的缓存在本地的数量
            initValue: 10000--初始值
</code></pre>             

###基于redis的队列id生成（预先生成id放入队列）
<pre><code>
id:  
    generator:  
        incr:  
            key: redis中的key名称  
            cacheSize:20--批量从redis中获取的缓存在本地的数量
</code></pre>
