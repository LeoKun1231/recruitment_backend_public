package com.hqk.recruitment.user.config.filter;

import com.hqk.recruitment.user.service.impl.UserDetailServiceImpl;
import com.hqk.recruitment.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 访问过滤器
 */
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    UserDetailServiceImpl userDetailsService;
    @Autowired
    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String authorization = request.getHeader("Authorization");
        if(StringUtils.isEmpty(authorization)){
            chain.doFilter(request,response);
            return;
        }
        String token = authorization.substring(7, authorization.length());
        String userName = JwtHelper.getUserName(token);
        Long userId = JwtHelper.getUserId(token);
        String authoritiesString = (String) redisTemplate.opsForValue().get(userName);
        List<GrantedAuthority> userAuthority = userDetailsService.getUserAuthority(userId);
        System.out.println("userAuthority = " + userAuthority);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName, null,userAuthority);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        chain.doFilter(request,response);
    }
}