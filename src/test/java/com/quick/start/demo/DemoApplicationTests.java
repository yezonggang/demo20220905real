package com.quick.start.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    String jsonString = "[{\"sId\":\"001\",\"name\":\"张三\"},{\"sId\":\"005\",\"name\":\"李四\"},{\"sId\":\"012\",\"name\":\"王五\"}]";



/*
    @Test
    void testJsonForClass() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StudentTest myClass = mapper.readValue(jsonString, StudentTest.class);
        System.out.println(myClass.getName());
    }
*/




}
