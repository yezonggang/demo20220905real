package com.quick.start.demo.framework.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author y25958
 */
@FeignClient(name = "myFeignClient",url = "http://127.0.0.1:8701")
public interface MyFeignClient {

    @PostMapping
    String executeCommand(@RequestBody String params);
}
