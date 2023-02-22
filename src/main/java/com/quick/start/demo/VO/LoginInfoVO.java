package com.quick.start.demo.VO;

import lombok.Data;

import java.util.List;

@Data
public class LoginInfoVO {
    public List<String> roles;
    public String name;
    public String avatar;
    public String introductions;
}
