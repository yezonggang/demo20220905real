package com.quick.start.demo.controller;


import com.quick.start.demo.entity.RoleEntity;
import com.quick.start.demo.framework.response.ResponseData;
import com.quick.start.demo.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yzg
 * @since 2022-09-05
 */
@RestController
@RequestMapping("/role-entity")
public class RoleController {

    @Autowired
    IRoleService roleService;

    @PostMapping("/insert")
    public CompletableFuture<ResponseData> getAllRole(@RequestBody RoleEntity roleEntity) {
        return roleService.insertNewUser(roleEntity).thenComposeAsync(resp ->
                resp.left.map(l -> CompletableFuture.completedFuture(ResponseData.fail(l))).orElseGet(() -> {
                    return CompletableFuture.completedFuture(ResponseData.success(roleEntity));
                })
        );
    }

}
