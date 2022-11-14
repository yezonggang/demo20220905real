package com.quick.start.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.quick.start.demo.entity.StudentTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author y25958
 */
public class TestJsonForClass {

    public static void main(String[] args) throws JsonProcessingException {
        String jsonArrayString = "[{\"sId\":\"001\",\"name\":\"张三\"},{\"sId\":\"005\",\"name\":\"李四\"},{\"sId\":\"012\",\"name\":\"王五\"}]";
        String jsonString = "{\"sId\":\"001\",\"name\":\"张三\"}";
            ObjectMapper mapper = new ObjectMapper();
            StudentTest myClass = mapper.readValue(jsonString, StudentTest.class);
            System.out.println(myClass.getStudentName());


            // json数组转成对象的list
        JSONArray jsonArray = JSONArray.parseArray(jsonArrayString);

        for(int i=0; i<jsonArray.size(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            //StudentTest myclass2 = JSONObject.parseObject(object.toJSONString() , StudentTest.class);
            StudentTest studentTest2=mapper.readValue(object.toJSONString() , StudentTest.class);
            System.out.println(studentTest2.getStudentName());
        }


        List list = null;
        CollectionType collectionType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, StudentTest.class);
        try {
            list = mapper.readValue(jsonArrayString, collectionType);
        }catch (Exception e){
        }
        System.out.println(list.get(0));
    }




    }


