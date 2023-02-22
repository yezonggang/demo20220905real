package com.quick.start.demo;

import com.quick.start.demo.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

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

}
