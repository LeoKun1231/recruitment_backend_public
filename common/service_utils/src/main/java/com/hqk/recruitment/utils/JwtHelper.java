package com.hqk.recruitment.utils;

import com.hqk.recruitment.exception.MyCustomException;
import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

public class JwtHelper {
    //一天
//    private static final long tokenExpiration = 60*1000;
    private static final long tokenExpiration = 24*60*60*1000;
    private static final String tokenSignKey = "HongQinKun";

    public static String createToken(Long userId, String userName) {
        String token = Jwts.builder()
                .setSubject("user")//设置主题
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                .claim("userName", userName)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }
    public static Long getUserId(String token) {
        if(StringUtils.isEmpty(token)) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        System.out.println("claims = " + claims);
        Integer userId = (Integer)claims.get("userId");
        return userId.longValue();
    }
    public static String getUserName(String token) {
        if(StringUtils.isEmpty(token)) return "";
        Jws<Claims> claimsJws
                = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("userName");
    }

    /**
     * 判断token是否存在与有效
     * @param jwtToken
     * @return
     */
    public static boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getToken(String authorization){
        if(authorization.length()<=7){
            throw new MyCustomException(20000,"token不存在");
        }
        String token= authorization.substring(7, authorization.length());
        boolean isNotExpire = JwtHelper.checkToken(token);
        if(!isNotExpire){
            throw new MyCustomException(20000,"token过期");
        }
        return token;
    }




    public static void main(String[] args) {
        String token = JwtHelper.createToken(1L, "55");
        System.out.println(token);
        System.out.println(JwtHelper.getUserId(token));
        System.out.println(JwtHelper.getUserName(token));
    }
}