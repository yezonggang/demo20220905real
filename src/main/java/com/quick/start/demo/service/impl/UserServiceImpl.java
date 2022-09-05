package com.quick.start.demo.service.impl;

import com.quick.start.demo.entity.UserEntity;
import com.quick.start.demo.mapper.UserMapper;
import com.quick.start.demo.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yzg
 * @since 2022-09-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {

}
