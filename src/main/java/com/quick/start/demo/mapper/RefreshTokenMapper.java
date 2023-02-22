package com.quick.start.demo.mapper;

import com.quick.start.demo.entity.RefreshTokenEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yzg
 * @since 2023-02-22
 */
public interface RefreshTokenMapper extends BaseMapper<RefreshTokenEntity> {
    String getRefreshToken(String username);

}
