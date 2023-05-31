package com.quick.start.demo.config.security;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 默认的用户名/密码提取是通过request中的getParameter来提取的
 * 该过滤器实现了从json获取用户名和密码
 * @author yzg
 */

@Slf4j
public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        final String APPLICATION_JSON_UTF8_VALUE_MY = "application/json;charset=UTF-8";

        //判断请求类型是否是json
        logger.info("request.getContentType():"+request.getContentType());
        if ( request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)
                ||request.getContentType().equals(APPLICATION_JSON_UTF8_VALUE_MY)  ){
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authenticationToken;
            try {
                InputStream is = request.getInputStream();
                Map authenticationBean = mapper.readValue(is,Map.class);
                authenticationToken = new UsernamePasswordAuthenticationToken(authenticationBean.get("username"),
                        authenticationBean.get("password"));
            }catch (IOException e){
                e.printStackTrace();
                authenticationToken = new UsernamePasswordAuthenticationToken("","");
            }
            setDetails(request,authenticationToken);
            return this.getAuthenticationManager().authenticate(authenticationToken);
        }else {
            request.getContentType();
            return super.attemptAuthentication(request, response);
        }

    }
}
