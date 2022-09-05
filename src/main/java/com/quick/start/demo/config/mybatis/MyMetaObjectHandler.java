package com.quick.start.demo.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject); //创建时间为系统时间
        this.setFieldValByName("updateTime", new Date(), metaObject); //更新时间为系统时间
    }




    @Override
    public void updateFill(MetaObject metaObject) {
     this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}