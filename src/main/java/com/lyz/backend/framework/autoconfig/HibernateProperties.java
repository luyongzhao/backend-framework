package com.lyz.backend.framework.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "hibernate")
public class HibernateProperties {

    private String dialect;

    private String show_sql;

    private String format_sql;

    private String batch_size;


}
