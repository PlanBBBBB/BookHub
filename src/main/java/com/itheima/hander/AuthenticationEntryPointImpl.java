package com.itheima.hander;

import cn.hutool.json.JSONUtil;
import com.itheima.utils.R;
import com.itheima.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        R r = new R("认证失败请重新登录");
        String jsonStr = JSONUtil.toJsonStr(r);
        WebUtils.renderString(response, jsonStr);
    }
}
