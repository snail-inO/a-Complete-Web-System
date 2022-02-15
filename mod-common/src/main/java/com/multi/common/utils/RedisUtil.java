package com.multi.common.utils;

import com.multi.common.returns.exception.CustomException;
import com.multi.common.returns.exception.ExceptionEnum;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

public class RedisUtil {
    private ValueOperations<String, Object> redisKey;
    public final static String RESTRICT_KEY = "restrict_key_";
    public final static String VERIFY_CODE = "verify_code_";
    public final static String RSA_PRIV_KEY = "rsa_private_key_";
    public final static String RSA_PUB_KEY = "rsa_public_key_";
    public final static String JWT_KEY = "jwt_key_";

    public RedisUtil(ValueOperations<String, Object> redisKey) {
        this.redisKey = redisKey;
    }

    public boolean isExisted(String prefix, String key) {
        return redisKey.get(prefix == null? key : prefix + key) != null;
    }

    public void set1hKey(String prefix, String id, String value) {
        redisKey.set(prefix + id, value, Duration.ofHours(1));
    }

    public Object getValue(String prefix, String key) {
        return redisKey.get(prefix + key);
    }
    public void removeKey(String prefix, String key) {
        redisKey.set(prefix + key, "", Duration.ofSeconds(1));
    }

    public void setRestrictKey(String key) throws CustomException {
        if (redisKey.setIfAbsent (RESTRICT_KEY + key, 1, Duration.ofSeconds(60)))
            return;
        int count = (int) redisKey.get(RESTRICT_KEY + key);
        if (count >= 10) {
            throw new CustomException(ExceptionEnum.METHOD_NOT_ALLOWED.getResultCode(), ExceptionEnum.METHOD_NOT_ALLOWED.getResultMsg());
        } else {
            redisKey.increment(RESTRICT_KEY + key);
        }

        return;
    }

    public void setPermanentKey(String prefix, String key, Object value) {
        redisKey.setIfAbsent(prefix + key, value);
    }
}
