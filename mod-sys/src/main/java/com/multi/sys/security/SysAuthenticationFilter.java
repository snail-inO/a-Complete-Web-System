package com.multi.sys.security;

import com.multi.common.returns.exception.CustomException;
import com.multi.common.returns.exception.ExceptionEnum;
import com.multi.common.utils.RedisUtil;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysAuthenticationFilter extends OncePerRequestFilter {
    private ValueOperations<String, Object> valueOperations;

    public SysAuthenticationFilter(ValueOperations<String, Object> valueOperations) {
        this.valueOperations = valueOperations;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String code = request.getParameter("code");
        String sessionId = request.getRequestedSessionId();
        RedisUtil redisUtil = new RedisUtil(valueOperations);
        String storeCode = (String) redisUtil.getValue(RedisUtil.VERIFY_CODE, sessionId);
        if (code== null || !code.equals(storeCode))
            throw new CustomException(ExceptionEnum.NOT_FOUND);
        redisUtil.removeKey(RedisUtil.VERIFY_CODE, sessionId);
        return;
    }
}
