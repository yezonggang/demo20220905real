package com.quick.start.demo;

import com.quick.start.demo.utils.EventCenter;
import com.quick.start.demo.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;

/**
 * @author y25958
 */
@SpringBootApplication
@Slf4j
@MapperScan("com.quick.start.demo.mapper")
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DemoApplication.class, args);
        String[] beanDefinitionNames = run.getBeanDefinitionNames();
        log.info(String.format("----->bean DefinitionNames:%s,count:%s", Arrays.asList(beanDefinitionNames),run.getBeanDefinitionCount()));
        log.info(String.format("----->bean SpringContextUtils.getBean(DataSource.class):%s",SpringContextUtils.getBean(DemoApplication.class)));
    }


    // 事件中心
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public EventCenter eventCenter( ThreadPoolTaskExecutor executor) {
        return new EventCenter("demo EventBus", executor);
    }

}
