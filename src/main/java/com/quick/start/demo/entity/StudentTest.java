package com.quick.start.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author y25958
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class StudentTest {
    @JsonProperty("sId")
    String studentID;
    @JsonProperty("name")
    String studentName;
}
