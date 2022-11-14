package com.quick.start.demo.mapper;

import com.quick.start.demo.entity.RoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yzg
 * @since 2022-09-05
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleEntity> {

}
