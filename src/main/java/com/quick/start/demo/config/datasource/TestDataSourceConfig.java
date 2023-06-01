package com.quick.start.demo.config.datasource;


import com.quick.start.demo.utils.EventCenter;
import com.quick.start.demo.utils.ProxyUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author y25958
 */
//@Configuration
public class TestDataSourceConfig {

    private final EventCenter eventCenter;

    public TestDataSourceConfig(EventCenter eventCenter) {
        this.eventCenter = eventCenter;
    }


    @Bean
    public TestDataSourceInvocationHandler dataSourceInvocationHandler(){
        return new TestDataSourceInvocationHandler(eventCenter);
    }

    @Bean
    public DataSource dataSource(){
        return ProxyUtils.newProxyInstance(DataSource.class, dataSourceInvocationHandler());
    }

}
