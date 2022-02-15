package com.multi.common.aop;

import com.multi.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Slf4j
@Aspect
@Component
public class AccessRestrictAOP {

    @Autowired
    private ValueOperations<String, Object> valueOperations;

    @Around(value = "@annotation(com.multi.common.aop.RestrictAccess)")
    public Object accessRestriction(ProceedingJoinPoint joinpoint) throws Throwable{
        RedisUtil redisUtil = new RedisUtil(valueOperations);

        String targetName = joinpoint.getTarget().getClass().getName();
        String methodName = joinpoint.getSignature().getName();
        StringBuilder builder = new StringBuilder();

        Arrays.stream(joinpoint.getArgs()).sequential().forEach(arg -> {
            builder.append(arg.getClass().getName());
            builder.append(',');
        });
        String args = builder.toString();
        String key = targetName + '/' + methodName + '/' + args;

        redisUtil.setRestrictKey(key);
        log.info(key + "added and will be proceeded");
        return joinpoint.proceed();
    }

}
