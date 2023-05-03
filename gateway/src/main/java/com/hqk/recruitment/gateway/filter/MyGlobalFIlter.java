package com.hqk.recruitment.gateway.filter;

import com.google.gson.JsonObject;
import com.hqk.recruitment.utils.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class MyGlobalFIlter implements GlobalFilter, Ordered {

    private AntPathMatcher antPathMatcher=new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        //登录接口不拦截
        if(antPathMatcher.match("/user/login/**",path) || antPathMatcher.match("/sms/**",path)){
            chain.filter(exchange);
            log.warn("filter",path);
            System.out.println("path = filter" + path);
        }else{//其他的必须进行拦截，验证是否有Token
            log.warn("token",path);
            System.out.println("path = token" + path);
            List<String> tokenList = request.getHeaders().get("Authorization");
            //判断列表是否为空
            if(null == tokenList){
                ServerHttpResponse response = exchange.getResponse();
                return out(response);
            }else{
                //判断列表不为空
                String token = tokenList.get(0);
                //判断是否含Bearer
                if (token.startsWith("Bearer ")){
                    //含Bearer
                    token = token.substring(7, token.length());
                    boolean isNotExpire = JwtHelper.checkToken(token);
                    //判断是否过期
                    if(!isNotExpire){
                        ServerHttpResponse response = exchange.getResponse();
                        return out(response);
                    }
                } else {
                    //Error
                    ServerHttpResponse response = exchange.getResponse();
                    return out(response);
                }
            }
        }
        return chain.filter(exchange);
    }

    //值越少，优先级越高
    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> out(ServerHttpResponse response) {
        JsonObject message = new JsonObject();
        message.addProperty("success", false);
        message.addProperty("code", 30001);
        message.addProperty("data", "无效token");
        byte[] bits = message.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}
