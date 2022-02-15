package com.multi.sys.security;

import com.multi.common.utils.JwtUtil;
import com.multi.common.utils.RedisUtil;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticateSuccessHandler implements AuthenticationSuccessHandler {
    private RedisUtil redisUtil;

    public AuthenticateSuccessHandler(ValueOperations<String, Object> valueOperations) {
        redisUtil = new RedisUtil(valueOperations);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        request.setAttribute("token", createToken(username));
        request.getSession(false).setAttribute("username", username);
        request.getRequestDispatcher("/success").forward(request, response);
    }

    private String createToken(String username) {
        String token = JwtUtil.generateToken(username);
        redisUtil.set1hKey(RedisUtil.JWT_KEY, username, token);

        return token;
    }
}
