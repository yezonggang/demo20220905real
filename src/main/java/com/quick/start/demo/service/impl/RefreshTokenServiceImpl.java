package com.quick.start.demo.service.impl;

import com.quick.start.demo.entity.RefreshTokenEntity;
import com.quick.start.demo.mapper.RefreshTokenMapper;
import com.quick.start.demo.service.IRefreshTokenService;
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
public class RefreshTokenServiceImpl extends ServiceImpl<RefreshTokenMapper, RefreshTokenEntity> implements IRefreshTokenService {

}
