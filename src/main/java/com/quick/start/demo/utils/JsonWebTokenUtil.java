package com.quick.start.demo.utils;
import com.quick.start.demo.config.property.JsonWebTokenProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@ConditionalOnProperty(value = "jwt.used",havingValue = "1")
@Slf4j
public class JsonWebTokenUtil {

    private final JsonWebTokenProperty jsonWebTokenProperty;

    @Autowired
    public JsonWebTokenUtil(JsonWebTokenProperty jsonWebTokenProperty) {
        this.jsonWebTokenProperty = jsonWebTokenProperty;
    }

    /**
     * 生成令牌
     * @return
     */
    public String generateToken(UserDetails details,String roles){
        Map<String,Object> claims = new HashMap<>();
        claims.put("sub", details.getUsername());
        claims.put("create",new Date());
        claims.put("role",roles);
        return generateToken(claims);
    }
    /**
     * 从claims生成令牌
     * @param claims
     * @return
     */
    private String generateToken(Map<String,Object> claims){
        Date expirationDate=new Date(System.currentTimeMillis()+jsonWebTokenProperty.getExpiration());
        return Jwts.builder().setClaims(claims)
                .setExpiration(expirationDate)//设置过期时间
                .signWith(SignatureAlgorithm.HS512,jsonWebTokenProperty.getSecret())
                .compact();
    }

    /**
     *
     * @param details 用户信息
     * @param roles 用户角色
     * @return 生成RefreshToken
     */
    public String generateRefreshToken(UserDetails details, String roles){
        log.info(String.format("%s,%s,%s,%s",jsonWebTokenProperty.getSecret(),jsonWebTokenProperty.getExpiration(),jsonWebTokenProperty.getHeader(),jsonWebTokenProperty.getRefresh_expiration()));
        Map<String,Object> claims = new HashMap<>();
        claims.put("sub", details.getUsername());
        claims.put("create",new Date());
        claims.put("role",roles);
        //设置过期时间为七天；人
        Date expirationDate = new Date(System.currentTimeMillis()+jsonWebTokenProperty.getRefresh_expiration());
        return Jwts.builder().setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512,jsonWebTokenProperty.getSecret())
                .compact();
    }

    /**
     * 从令牌中获取用户名
     * @param token 令牌
     * @return 用户名
     */
    public  String getUsernameFromToken(String token){
        String username;
        try {
            Claims claims=getClaimsFromToken(token);
            username=claims.getSubject();
        }catch (ExpiredJwtException e){
            username=null;
        }
        return username;
    }

    /**
     * 无视过期时间，获取用户名
     * @param token
     * @return
     */
    public  String getUsernameIgnoreExpiration(String token){
        String username;
        try {
            Claims claims=getClaimsFromToken(token);
            username=claims.getSubject();
        }catch (ExpiredJwtException e){
            username=e.getClaims().getSubject();
        }
        return username;
    }
    /**
     * 从令牌中获取数据声明，
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(jsonWebTokenProperty.getSecret()).parseClaimsJws(token).getBody();
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    private Boolean isTokenExpired(String token){
        try{
            Claims claims=getClaimsFromToken(token);
            Date expiration=claims.getExpiration();
            return expiration.before(new Date());
        }catch (Exception e){
            return  false;
        }
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token){
        String refreshedToken;
        try {
            Claims claims=getClaimsFromToken(token);
            claims.put("created",new Date());
            refreshedToken=generateToken(claims);
        }catch (ExpiredJwtException e){
            Claims claims = e.getClaims();
            claims.put("created",new Date());
            refreshedToken= generateToken(claims);
        }
        return  refreshedToken;
    }

    /**
     * 验证令牌
     * @param token 令牌
     * @param userDetails 用户
     * @return 是否有效
     */
    public Boolean validateToken(String token,UserDetails userDetails){
        String username=getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }

    public String getHeader() {
        return jsonWebTokenProperty.getHeader();
    }
}
