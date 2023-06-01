package com.quick.start.demo.config.datasource;

import com.google.common.eventbus.Subscribe;
import com.quick.start.demo.utils.EventCenter;
import com.quick.start.demo.utils.ProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author y25958
 */
@Slf4j
public class TestDataSourceInvocationHandler implements InvocationHandler {

    private final EventCenter eventCenter;
    private DataSource dataSource;
//   这个property就是自定义数据源的配置项
//   private TestDataSourceProperty property;

    public TestDataSourceInvocationHandler(EventCenter eventCenter) {
        this.eventCenter = eventCenter;
        // 注册表明我是个事件的监听器,使用@Subscribe注解来监听关注的事件
        eventCenter.registerAsync(this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().equalsIgnoreCase("getConnection")){
            log.info("begin to create a connection");
        }
        try {
            return method.invoke(dataSource, args);
        }catch (Exception e){
            throw ProxyUtils.unwrapInvocationThrowable(e);
        }
    }

    // 监听StatusChangedEvent这个事件，这个事件在某个地方必然被 publishAsync了，如果status是running那么就初始化数据源；
    @Subscribe
    public void handleStatusChanged(StatusChangedEvent status){
        if (status.getStatus().equalsIgnoreCase("running")) {
            initDataSource();
        }
    }

    private void initDataSource() {
        if (dataSource != null) {
            return;
        }

        dataSource = DataSourceBuilder.create().driverClassName("xxx").url("xx").username("xx").password("xx").build();
        eventCenter.publishAsync(dataSource);

    }

}
