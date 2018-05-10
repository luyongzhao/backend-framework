package com.mobike.iotcloud.backend.framework.autoconfig;

import com.mobike.iotcloud.backend.framework.freemarker.FreemarkerStaticModels;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

@Configuration
public class FreemarkerAutoConfiguration {

    @Bean("freemarkerStaticModels")
    public FreemarkerStaticModels getFreemarkerStaticModels()throws IOException{

        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new PathMatchingResourcePatternResolver().getResource("classpath:freemarkerstatic.properties"));

        FreemarkerStaticModels models = FreemarkerStaticModels.getInstance();
        models.setStaticModels(bean.getObject());

        return models;
    }
}
