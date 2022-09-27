package com.quick.start.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quick.start.demo.entity.StudentTest;

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
        JSONArray jsonArray = JSONArray.parseArray(jsonString);

        for(int i=0; i<jsonArray.size(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            //StudentTest myclass2 = JSONObject.parseObject(object.toJSONString() , StudentTest.class);
            StudentTest studentTest2=mapper.readValue(object.toJSONString() , StudentTest.class);
            System.out.println(studentTest2.getStudentName());
        }
    }




    }


