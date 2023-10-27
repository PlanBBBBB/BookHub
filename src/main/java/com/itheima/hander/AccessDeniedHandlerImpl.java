package com.itheima.hander;

import cn.hutool.json.JSONUtil;
import com.itheima.utils.R;
import com.itheima.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        R r = new R("权限不足");
        String jsonStr = JSONUtil.toJsonStr(r);
        WebUtils.renderString(response, jsonStr);
    }
}