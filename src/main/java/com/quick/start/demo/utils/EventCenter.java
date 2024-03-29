package com.quick.start.demo.utils;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * @author y25958
 */


public final class EventCenter {
    private final EventBus sync;
    private final AsyncEventBus async;
    public EventCenter(String identifier){
        this(identifier, ForkJoinPool.commonPool());
    }
    public EventCenter(String identifier, Executor executor){
        sync = new EventBus(identifier);
        async = new AsyncEventBus(identifier,executor);
    }

    // 注册同步监听器
    public void registerSync(Object object){sync.register(object);}
    public void unregisterSync(Object object){sync.unregister(object);}
    // 注册异步监听器
    public void registerAsync(Object object){async.register(object);}
    public void unregisterAsync(Object object){async.unregister(object);}

    // 发布事件并推送
    public void publishSync(Object object){sync.post(object);}
    public void publishAsync(Object object){async.post(object);}




}
