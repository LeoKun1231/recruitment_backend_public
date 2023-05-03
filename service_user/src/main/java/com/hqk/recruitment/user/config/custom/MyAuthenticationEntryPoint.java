package com.hqk.recruitment.user.config.custom;

import com.google.gson.JsonObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 未授权的统一处理方式
 * </p>
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success",false);
        jsonObject.addProperty("code",HttpServletResponse.SC_UNAUTHORIZED);
        jsonObject.addProperty("data","未授权");
        jsonObject.addProperty("message","未授权");
        byte[] bytes = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
