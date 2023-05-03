package com.hqk.recruitment.user.config.custom;


import com.google.gson.JsonObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * 权限不足
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success",false);
        jsonObject.addProperty("code",HttpServletResponse.SC_FORBIDDEN);
        jsonObject.addProperty("data","权限不足");
        jsonObject.addProperty("message","权限不足");
        byte[] bytes = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
