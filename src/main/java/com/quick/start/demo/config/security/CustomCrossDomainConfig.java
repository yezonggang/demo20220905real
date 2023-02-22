package com.quick.start.demo.config.security;

import com.quick.start.demo.utils.JsonWebTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * 跨域配置
 * @author yzg
 */
@Configuration
public class CustomCrossDomainConfig {

    @Autowired
    JsonWebTokenUtil jsonWebTokenUtil;

    /**
     *
     * @return 基于URL的跨域配置信息
     * //允许客户端携带认证信息
     * //允许哪些请求方式可以访问
     * //允许服务端访问的客户端请求头
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration cores=new CorsConfiguration();
        cores.setAllowCredentials(true);
        cores.setAllowedMethods(Arrays.asList("GET","POST","DELETE","PUT","UPDATE"));
        cores.setAllowedHeaders(Collections.singletonList("*"));
        // 暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
        cores.addExposedHeader(jsonWebTokenUtil.getHeader());
        // 注册跨域配置
        // 也可以使用CorsConfiguration 类的 applyPermitDefaultValues()方法使用默认配置
        source.registerCorsConfiguration("/**",cores.applyPermitDefaultValues());
        return source;
    }

}
