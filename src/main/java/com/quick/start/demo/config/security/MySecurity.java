package com.quick.start.demo.config.security;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quick.start.demo.config.property.JsonWebTokenProperty;
import com.quick.start.demo.entity.RefreshTokenEntity;
import com.quick.start.demo.framework.response.ResponseData;
import com.quick.start.demo.framework.response.ResponseMsgUtil;
import com.quick.start.demo.mapper.RefreshTokenMapper;
import com.quick.start.demo.service.IRefreshTokenService;
import com.quick.start.demo.service.impl.UserServiceImpl;
import com.quick.start.demo.utils.JsonWebTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * spring security 配置类
 *
 * @author yzg
 */
@EnableWebSecurity
@Slf4j
public class MySecurity extends WebSecurityConfigurerAdapter {

    public static String LOGIN_URL = "/user/login";

    public static List<String> urls = Arrays.asList(
            "/user/login",
            "/swagger-resources/**",
            "/webjars/**",
            "/v2/**",
            "/api/**",
            "/actuator/*",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/favicon.ico",
            "/static/**",
            "/resources/webjars/**",
            "/actuator/**",
            "/actuator/**/**");

    @Autowired
    LoginAuthProvider loginAuthProvider;

    @Autowired
    JsonWebTokenUtil jsonWebTokenUtil;
    @Autowired
    IRefreshTokenService refreshTokenService;

    @Autowired
    RefreshTokenMapper refreshTokenMapper;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    JsonWebTokenProperty jsonWebTokenProperty;

    // 先认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(loginAuthProvider);
    }

    // 后鉴权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //允许跨域，配置后SpringSecurity会自动寻找name=corsConfigurationSource的Bean
        http.cors();
        http.csrf().disable();
        //当访问接口失败的配置
        http.exceptionHandling().authenticationEntryPoint(new InterfaceAccessException());
        http.authorizeRequests()
                .antMatchers(String.join(",",urls)).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginProcessingUrl(LOGIN_URL)
                // 针对login请求自定义success和failure处理器
                .and()
                .addFilterAt(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 登出接口
        http.logout()
                .logoutUrl("/user/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .deleteCookies(jsonWebTokenProperty.getHeader())
                .clearAuthentication(true);

        //因为用不到session，所以选择禁用
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Bean(name = "myRequestMatcher")
    public SkipPathAntMatcher skipPathAntMatcher() {
        return new SkipPathAntMatcher(urls);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jsonWebTokenUtil, userService, skipPathAntMatcher(), refreshTokenMapper);
    }


    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JsonAuthenticationFilter filter = new JsonAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setFilterProcessesUrl(LOGIN_URL);
        filter.setAuthenticationSuccessHandler(new MySuccessHandler());
        filter.setAuthenticationFailureHandler(new MyFailHandler());
        return filter;
    }


    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler() {
            @Autowired
            public void setObjectMapper(ObjectMapper objectMapper) {
                this.objectMapper = objectMapper;
            }

            private ObjectMapper objectMapper;

            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write(objectMapper.writeValueAsString(ResponseData.success("logout success.")));
                out.flush();
                out.close();
            }
        };
    }

    //登录成功的处理类
    class MySuccessHandler implements AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException {
            UserDetails details = (UserDetails) authentication.getPrincipal();
            List<GrantedAuthority> roles = (List<GrantedAuthority>) details.getAuthorities();
            //登录时同时生成refreshToken，保存到表中
            log.info("begin to onAuthenticationSuccess.");
            RefreshTokenEntity token = new RefreshTokenEntity();
            token.setUsename(details.getUsername());
            String refreshToken = jsonWebTokenUtil.generateRefreshToken(details, roles.get(0).getAuthority());
            token.setToken(refreshToken);
            LambdaQueryWrapper<RefreshTokenEntity> queryWrapper = new QueryWrapper<RefreshTokenEntity>().lambda().eq(RefreshTokenEntity::getUsename, details.getUsername());
            RefreshTokenEntity refreshTokenTemp = refreshTokenMapper.selectOne(queryWrapper);
            if (refreshTokenTemp != null) {
                refreshTokenTemp.setToken(refreshToken);
                refreshTokenMapper.update(refreshTokenTemp, queryWrapper);
            } else {
                log.info("begin to insert token");
                refreshTokenMapper.insert(token);
                log.info("end to insert token");
            }
            response.setHeader(jsonWebTokenUtil.getHeader(), refreshToken);
            Cookie cookie = new Cookie("token", refreshToken);
            cookie.setPath("/");
            response.addCookie(cookie);
            PrintWriter out = response.getWriter();
            out.write(new ObjectMapper().writeValueAsString(ResponseData.success(refreshToken)));
            out.flush();
            out.close();
            ResponseData.success(response);

        }
    }

    //登录失败的处理
    public class MyFailHandler implements AuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException e) throws IOException, ServletException {
            ResponseMsgUtil.sendFailMsg(e.getMessage(), response);
        }
    }


}




