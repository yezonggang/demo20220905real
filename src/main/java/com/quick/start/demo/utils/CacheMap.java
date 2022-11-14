package com.quick.start.demo.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例线程安全
 */
public class CacheMap {
     
      private Map<String, String> map = new ConcurrentHashMap<String, String>();
      private Object lock = new Object();

      private static CacheMap instance = new CacheMap();
      private CacheMap(){}
      public static CacheMap getInstance() {
           if (instance == null) {
               synchronized (CacheMap.class) {
                   if (instance == null) {
                       instance = new CacheMap();
                   }
               }
           }
           return instance ;
      }
      public void put(String taskId, String name) {
           synchronized (lock ) {
               map.put(taskId, name);
           }
      }
     
      public Map<String, String> getMap() {
           return map ;
      }
 
}