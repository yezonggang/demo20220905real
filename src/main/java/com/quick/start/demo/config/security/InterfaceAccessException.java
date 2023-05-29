package com.quick.start.demo.config.security;

import com.quick.start.demo.framework.exception.ApiError;
import com.quick.start.demo.framework.exception.ApiErrorEnum;
import com.quick.start.demo.framework.response.ResponseData;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author y25958
 */
public class InterfaceAccessException implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ResponseData.fail(ApiError.from(ApiErrorEnum.ACCESS_EXCEPTION));
    }

    public boolean isAjaxRequest(HttpServletRequest request) {
        String ajaxFlag = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(ajaxFlag);
    }
}
