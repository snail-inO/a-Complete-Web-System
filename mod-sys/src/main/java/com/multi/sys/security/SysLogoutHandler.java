package com.multi.sys.security;

import com.multi.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SysLogoutHandler implements LogoutHandler {
    RedisUtil redisUtil;

    public SysLogoutHandler(ValueOperations<String, Object> valueOperations) {
        redisUtil = new RedisUtil(valueOperations);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String username = authentication.getName();
        log.info("remove token: " + username);
        redisUtil.removeKey(RedisUtil.JWT_KEY, username);
    }
}
