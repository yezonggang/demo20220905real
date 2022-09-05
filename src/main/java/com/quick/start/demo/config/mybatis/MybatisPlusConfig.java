package com.quick.start.demo.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
@Configuration
public class MybatisPlusConfig {
    /*关于MybatisPlus分页规则说明
     *   规则: 需要设定一个拦截器,将分页的Sql进行动态的拼接
     *   Sql: 规则现在的Sql都支持Sql92标准!!!!(相当CRUD)
     *     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor(DbType.MARIADB);
        //请求页面大于最大页面操作后，返回首页
        innerInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(innerInterceptor);
        return interceptor;
    }
}
