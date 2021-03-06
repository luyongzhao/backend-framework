package com.lyz.backend.framework.autoconfig;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(HibernateProperties.class)
@ConditionalOnProperty("hibernate.dialect")
public class HibernateAutoConfiguration {

    @Autowired
    private HibernateProperties hibernateProperties;

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource keepaliveDataSource() {
        return DataSourceBuilder.create().build();
    }



    @Bean(name="sessionFactory")
    public SessionFactory sessionFactory(@Qualifier("dataSource") DataSource dataSource) throws IOException {

        SessionFactory sessionFactory = null;

        //设置数据源
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSource);

        //设备hibernate的xml映射文件
        localSessionFactoryBean.setMappingLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/**/*.hbm.xml"));

        //设置hibernate的属性
        Properties prop = new Properties();
        prop.setProperty("hibernate.dialect",hibernateProperties.getDialect());
        prop.setProperty("hibernate.show_sql",hibernateProperties.getShow_sql());
        prop.setProperty("hibernate.format_sql",hibernateProperties.getFormat_sql());
        prop.setProperty("hibernate.jdbc.batch_size",hibernateProperties.getBatch_size());

        localSessionFactoryBean.setHibernateProperties(prop);

        localSessionFactoryBean.afterPropertiesSet();

        sessionFactory = localSessionFactoryBean.getObject();

        return sessionFactory;

    }

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager keepaliveTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }




}
