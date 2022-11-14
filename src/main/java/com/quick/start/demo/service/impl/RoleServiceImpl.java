package com.quick.start.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.start.demo.entity.RoleEntity;
import com.quick.start.demo.entity.UserEntity;
import com.quick.start.demo.framework.exception.ApiError;
import com.quick.start.demo.framework.response.Either;
import com.quick.start.demo.mapper.RoleMapper;
import com.quick.start.demo.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yzg
 * @since 2022-09-05
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements IRoleService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    public CompletableFuture<Either<ApiError, List<RoleEntity>>> insertNewUser(RoleEntity roleEntity) {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);
        Lock lock = reentrantReadWriteLock.writeLock();
        lock.lock();
        try {
            roleMapper.insert(roleEntity);
        }catch (Exception e){
            Either.Left(e);
        }finally {
            lock.unlock();

        }
        LambdaQueryWrapper<RoleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<RoleEntity>().eq(RoleEntity::getId, roleEntity.getId());
        List<RoleEntity> roleEntityList = roleMapper.selectList(lambdaQueryWrapper);
        roleEntityList.stream().filter(r -> r.getName().startsWith("xx")).collect(Collectors.toList());
        return CompletableFuture.completedFuture(Either.Right(roleEntityList));
    }


}
