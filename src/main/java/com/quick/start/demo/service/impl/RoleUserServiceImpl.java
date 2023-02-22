package com.quick.start.demo.service.impl;

import com.quick.start.demo.entity.RoleUserEntity;
import com.quick.start.demo.mapper.RoleUserMapper;
import com.quick.start.demo.service.IRoleUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yzg
 * @since 2023-02-22
 */
@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUserEntity> implements IRoleUserService {

}
