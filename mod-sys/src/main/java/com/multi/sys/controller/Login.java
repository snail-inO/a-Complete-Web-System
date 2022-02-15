package com.multi.sys.controller;

import com.multi.common.returns.exception.CustomException;
import com.multi.common.returns.exception.ExceptionEnum;
import com.multi.common.utils.RedisUtil;
import com.multi.log.aop.Logging;
import com.power.common.util.RSAUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api
@Controller
public class Login {
    @Autowired
    ValueOperations<String, Object> valueOperations;

    @Logging
    @GetMapping("/login")
    public void welcome(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession(false);
        if (session == null) {
            session = req.getSession(true);
            String sessionId = session.getId();
            KeyPair rsaKeyPair = RSAUtil.generateKeyPair(2048);
            RedisUtil redisUtil = new RedisUtil(valueOperations);
            String pubKey = RSAUtil.getPublicKey(rsaKeyPair);
            log.info("session id: " + sessionId);
            log.info("encoded 123: " + RSAUtil.encryptString("123", pubKey));
            String privKey = RSAUtil.getPrivateKey(rsaKeyPair);
            redisUtil.set1hKey(RedisUtil.RSA_PUB_KEY, sessionId, pubKey);
            redisUtil.set1hKey(RedisUtil.RSA_PRIV_KEY, sessionId, privKey);
        } else
            log.info("session id: " + session.getId());


        req.getRequestDispatcher("login_register.html").forward(req, resp);
    }

    @Logging
    @PostMapping("/success")
    @ResponseBody
    public Map<String, Object> success(HttpServletRequest req) {
        Map<String, Object> data = new HashMap<>();
        String token = (String) req.getAttribute("token");
        if (token == null)
            throw new CustomException(ExceptionEnum.NOT_FOUND);
        data.put("username", req.getSession(false).getAttribute("username"));
        data.put("token", token);

        return data;
    }

}
