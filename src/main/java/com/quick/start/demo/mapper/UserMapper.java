package com.quick.start.demo.mapper;

import com.quick.start.demo.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yzg
 * @since 2022-09-05
 */
@Mapper

public interface UserMapper extends BaseMapper<UserEntity> {

}
