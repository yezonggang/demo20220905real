package com.quick.start.demo.config.security;

import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @author y25958
  // 自定义密码的加密解密方式，暂未使用，因auth可以自定义处理方式
 */
public class CustomPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }
}
