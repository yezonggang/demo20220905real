package com.quick.start.demo.config.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author y25958
 */
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
/*    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/swagger-ui.html","/swagger-resources/**","/webjars/**","/v2/**","/api/**").permitAll()
                .anyRequest().authenticated();
    }*/
}
