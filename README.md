##生成mybatis代码
1、修改generatorConfig.xml中的table标签，增加自己需要的表
2、mvn mybatis-generator:generate -e

##参数校验参考
https://blog.csdn.net/csyuyaoxiadn/article/details/56016359
https://blog.csdn.net/wingkoo1986/article/details/70768730

##目录结构说明
com.mobike.iotcloud.guard.api.bean: 数据库表对应的实体类
com.mobike.iotcloud.guard.api.bean.ext: 实体类扩展，可以定义一些跟实体类相关的方法，常量以及需要返回给前端的额外的属性字段
com.mobike.iotcloud.guard.api.mapper：mybatis映射的mapper类，定义数据库操作方法
com.mobike.iotcloud.guard.api.datasource：数据源相关的配置类
com.mobike.iotcloud.guard.api.exception：异常类，以及全局的异常处理类
com.mobike.iotcloud.guard.api.interceptor：拦截器配置以及相应的拦截器
com.mobike.iotcloud.guard.api.service：服务类，封装业务逻辑
com.mobike.iotcloud.guard.api.controller：控制类，对外暴露接口使用
com.mobike.iotcloud.guard.api.controller.bean: 控制类使用到的通用实体类
com.mobike.iotcloud.guard.api.util：工具类库