package com.multi.sys.security;

import com.alibaba.fastjson.JSON;
import com.multi.common.returns.Json;
import com.multi.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class SysLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        PrintWriter out = new PrintWriter(response.getOutputStream());
        Json res = new Json();
        res.setData(username);
        out.print(JSON.toJSONString(res));
        out.flush();
        out.close();
    }
}
