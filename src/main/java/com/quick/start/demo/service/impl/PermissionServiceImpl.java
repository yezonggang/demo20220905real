package com.quick.start.demo.service.impl;

import com.quick.start.demo.entity.PermissionEntity;
import com.quick.start.demo.mapper.PermissionMapper;
import com.quick.start.demo.service.IPermissionService;
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
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity> implements IPermissionService {

}
