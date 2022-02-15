package com.multi.sys.security;

import com.multi.common.returns.exception.CustomException;
import com.multi.common.returns.exception.ExceptionEnum;
import com.multi.common.utils.RedisUtil;
import com.power.common.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;


@Slf4j
public class SysAuthenticationProvider implements AuthenticationProvider {
    private UserDetailServiceImpl userDetailService;
    private RedisUtil redisUtil;

    public SysAuthenticationProvider(UserDetailServiceImpl userDetailService, ValueOperations<String, Object> valueOperations) {
        this.userDetailService = userDetailService;
        redisUtil = new RedisUtil(valueOperations);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        SysAuthenticationDetails details = (SysAuthenticationDetails) authentication.getDetails();
        log.info("session id: " + details.getSessionId());
        if (!checkCode(details))
            throw new CustomException(ExceptionEnum.BAD_REQUEST);
        SysUserDetails userDetails = (SysUserDetails) userDetailService.loadUserByUsername(username);
        if (!loginUser(userDetails.getPassword(), password, details.getSessionId()))
            throw new CustomException(ExceptionEnum.BAD_REQUEST);
        log.info("details: " + details.getSessionId());
        log.info("username: " + username);
        log.info("password: " + password);
        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }

    private boolean loginUser(String storePassword, String password, String sessionId) throws CustomException {
        String privateKey = (String) redisUtil.getValue(RedisUtil.RSA_PRIV_KEY, sessionId);
        // test code
//        String publicKey = (String) redisUtil.getValue(RedisUtil.RSA_PUB_KEY, sessionId);
        if (privateKey == null)
            throw new CustomException(ExceptionEnum.NOT_FOUND);
        String decodedPassword;
        try {
            //test code
//            String encodedPassword = RSAUtil.encryptString(password, publicKey);
            decodedPassword = RSAUtil.decryptString(password, privateKey);
        } catch (Exception e) {
            throw new CustomException(ExceptionEnum.INTERNAL_SERVER_ERROR, e);
        }
        log.info(decodedPassword);
        if (storePassword.equals(decodedPassword))
            return true;
        return false;
    }

    private boolean checkCode(SysAuthenticationDetails details) throws CustomException{
        String sessionId = details.getSessionId();
        String code = details.getCode();
        if (sessionId == null || code  == null)
            throw new CustomException(ExceptionEnum.BAD_REQUEST);

        String storeCode = (String) redisUtil.getValue(RedisUtil.VERIFY_CODE, sessionId);
        log.info("session id: " + sessionId);
        log.info("checkCode: " + code + ", " + storeCode);
        if (code!= null && code.equals(storeCode)) {
            redisUtil.removeKey(RedisUtil.VERIFY_CODE, sessionId);
            return true;
        }
        return false;
    }
}
