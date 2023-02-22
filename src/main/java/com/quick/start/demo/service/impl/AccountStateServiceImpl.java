package com.quick.start.demo.service.impl;

import com.quick.start.demo.entity.AccountStateEntity;
import com.quick.start.demo.mapper.AccountStateMapper;
import com.quick.start.demo.service.IAccountStateService;
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
public class AccountStateServiceImpl extends ServiceImpl<AccountStateMapper, AccountStateEntity> implements IAccountStateService {

}
