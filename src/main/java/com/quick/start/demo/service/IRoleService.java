package com.quick.start.demo.service;

import com.quick.start.demo.entity.RoleEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.start.demo.framework.exception.ApiError;
import com.quick.start.demo.framework.response.Either;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yzg
 * @since 2022-09-05
 */
public interface IRoleService extends IService<RoleEntity> {
    CompletableFuture<Either<ApiError, List<RoleEntity>>> insertNewUser(RoleEntity roleEntity);

}
