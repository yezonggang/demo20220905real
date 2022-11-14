package com.quick.start.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author wulongbo
 * @Date 2020/7/6 15:43
 * @Version 1.0
 */
public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);


    /**
     * byte数组转换成Json
     *
     * @param array
     * @return JSONObject
     */
    public static JSONObject byteToJson(byte[] array) {
        String body = JSONArray.toJSONString(array); // 先转换成Json String
        byte[] json = JSONObject.parseObject(body, byte[].class);//转json byte
        String convent = new String(json); // byte数组转换成String
        JSONObject jsonObject = JSONObject.parseObject(convent);
        return jsonObject;
    }

    /**
     * byte数组转换成Java bean
     *
     * @param array
     * @return JSONObject
     */
    public static <T> T byteToJaveBean(byte[] array, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        JSONObject jsonObject = byteToJson(array);
        T obj = clazz.newInstance();
        return jsonObject.toJavaObject((Type) obj.getClass());
    }

    /**
     * String转换成Json
     *
     * @param key
     * @return JSONObject
     */
    public static JSONObject stringToJson(String key) {
        return JSONObject.parseObject(key);
    }

    /**
     * Java对象转换成String
     *
     * @return
     */
    public static String objectToString(Object object) {
        return JSONArray.toJSONString(object);
    }

    /**
     * Java bean 转换成 JSONObject
     *
     * @return
     */
    public static JSONObject beanToJSONObject(Object object) {
        return JSONObject.parseObject(JSONObject.toJSON(object).toString());
    }

    /**
     * 将map转换成byte[]
     *
     * @return
     */
    public static byte[] changeMapToByte(Map<String, Object> map) {

        byte[] bytes = null;
        try {
            bytes = serilizableForMap(map).getBytes();
        } catch (Exception e) {
            log.error("map到byte[]转换异样", e);
        }
        return bytes;
    }

    /**
     * 将byte[]转换成map
     *
     * @return
     */
    public static Map<String, String> changeByteToMap(byte[] bytes) {

        Map<String, String> retmap = null;

        try {
            if (bytes != null) {
                retmap = deserilizableForMapFromFile(new String(bytes), String.class);
            } else {
                log.error("changeByteToMap中bytes为null");
            }

        } catch (Exception e) {
            log.error("byte到map转换异样", e);
        }

        return retmap;
    }

    /* 将HashMap序列化为字符串存入json文件中 */
    public static String serilizableForMap(Object objMap)
            throws IOException {
        String listString = JSON.toJSONString(objMap, true);// (maps,CityEntity.class);
        return listString;
    }

    /* 将json文件中的内容读取进去，反序列化为HashMap */
    public static <T, K> HashMap<K, T> deserilizableForMapFromFile(String listString2, Class<T> clazz) throws IOException {

        Map<K, T> map = JSON.parseObject(listString2, new TypeReference<Map<K, T>>() {
        });

        return (HashMap<K, T>) map;
    }


    /**
     *
     *json是数组转成对象的list
     * @return
     */
    public static <T> List<T> jsonToObjectListOne(String body,Class<T> clazz)  {
        JSONArray jsonArray = JSONArray.parseArray(body);
        ObjectMapper mapper = new ObjectMapper();
        List<T> list = new ArrayList<T>();
        try {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                list.add(mapper.readValue(object.toJSONString(), clazz));
            }
        }catch (Exception e){
            return null;
        }
        return list;
    }

    /**
     *
     *json是数组转成对象的list
     * @return
     */

    public static <T> List<T> jsonToObjectListTwo(String body,Class<T> clazz){
        List<T> list;
        ObjectMapper mapper = new ObjectMapper();
        CollectionType collectionType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, clazz);
        try {
            list = mapper.readValue(body, collectionType);
        }catch (Exception e){
            return null;
        }
        return list;
    }


    public static <T> List<T> jsonToObjectListThree(String body,Class<T> clazz){
        List<T> list;
        return JSON.parseArray(body,clazz);
    }

}