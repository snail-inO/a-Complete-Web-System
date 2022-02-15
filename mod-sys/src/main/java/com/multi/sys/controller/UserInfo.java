package com.multi.sys.controller;

import com.multi.common.utils.RedisUtil;
import com.multi.log.aop.Logging;
import com.multi.sys.bean.User;
import com.multi.sys.mapper.RoleMapper;
import com.multi.sys.mapper.UserMapper;
import com.multi.sys.security.SysUserDetails;
import com.multi.sys.security.UserDetailServiceImpl;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Api
@RestController
public class UserInfo {
    @Autowired
    ValueOperations<String, Object> valueOperations;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserMapper userMapper;

    @Logging
    @GetMapping("/user_info")
    public SysUserDetails userInfo(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        RedisUtil redisUtil = new RedisUtil(valueOperations);
        log.info("session id: " + session.getId());
        String username = (String) session.getAttribute("username");
        log.info("username: " + username);
        UserDetailServiceImpl service = new UserDetailServiceImpl(userMapper, roleMapper);

        return (SysUserDetails) service.loadUserByUsername(username);
    }
}
