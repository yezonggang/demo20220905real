package com.quick.start.demo.config;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


// 初始化mysql数据库的记录，随时可以登录
@Configuration
@ConditionalOnProperty(value = "jwt.used",havingValue = "1")
@Slf4j
public class CreateMysqlTableConfiguration {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Bean
    public MysqlIni mysqlIni(JdbcTemplate jdbcTemplate,@Value("classpath:/security.sql") Resource resource){
        return new MysqlIni(jdbcTemplate,resource);

    }

    public static class MysqlIni implements CommandLineRunner {

        private final JdbcTemplate jdbcTemplate;
        private final Resource mysqlResource;


        public MysqlIni(JdbcTemplate jdbcTemplate, Resource mysqlResource) {
            this.jdbcTemplate = jdbcTemplate;
            this.mysqlResource = mysqlResource;
        }

        @Override
        public void run(String... args) throws Exception {
            initTableData();
        }


        private void initTableData(){
            log.isDebugEnabled();
            BufferedReader reader = null;
            try{
                reader = new BufferedReader(new InputStreamReader(mysqlResource.getInputStream()));
            }catch (IOException e){
                log.error("mysqlResouce error.");
            }
            String allSQL= reader.lines().reduce("",(a,b)->a+""+b);
            String[] sqls = allSQL.split(";");
            for(String sql:sqls){
                try {
                    log.info("sql"+sql);
                    //logger.info("jdbctemplate"+jdbcTemplate.toString());
                    jdbcTemplate.execute(sql);
                }catch (Exception e){
                    log.error("jdbc error");
                }
            }


        }
    }


}
