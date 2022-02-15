package com.multi.sys.controller;

import com.multi.common.returns.Json;
import com.multi.common.utils.RedisUtil;
import com.multi.log.aop.Logging;
import com.power.common.util.RandomUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@RestController
@Api
public class VerificationCode {
    @Autowired
    private ValueOperations<String, Object> valueOperations;

    @Logging
    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public String code(HttpServletRequest req) {
        String code = RandomUtil.randomNumbers(4);
        log.info(code);
        RedisUtil redisUtil = new RedisUtil(valueOperations);
        String sessionId = req.getRequestedSessionId();
        log.info(sessionId);
        redisUtil.set1hKey(RedisUtil.VERIFY_CODE, sessionId, code);
        Map<String, String> res = new HashMap<>();
        res.put("code", code);

        return code;
    }
}
