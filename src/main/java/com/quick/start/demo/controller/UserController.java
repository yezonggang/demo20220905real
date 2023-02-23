package com.quick.start.demo.controller;


import com.quick.start.demo.framework.response.ResponseData;
import com.quick.start.demo.service.impl.UserServiceImpl;
import com.quick.start.demo.utils.AuthenticationFacade;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yzg
 * @since 2022-09-05
 */
@RestController
@Slf4j
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    private AuthenticationFacade authenticationFacade;


    @GetMapping("/getInfo")
    public ResponseData loginInfo(HttpServletRequest request) {
        log.info("begin to llogger login_info");
        return userService.loginInfo(request);
    }

}
