package com.multi.log.aop;

import com.multi.log.bean.Log;
import com.multi.log.mapper.LogMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;

@Slf4j
@Aspect
@Component
public class LogAOP {
    @Autowired
    LogMapper logMapper;

//    @Pointcut("@annotation(logging)")
//    public void aspect(Logging logging) {}

    @Around(value = "@annotation(com.multi.log.aop.Logging)")
    public Object methodLogging(ProceedingJoinPoint joinPoint) throws Throwable{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }
        HttpServletRequest req = attributes.getRequest();
        Enumeration<String> headerNames = req.getHeaderNames();
        Log logData = setLog(joinPoint, req);
        Object res;

        try {
            res = joinPoint.proceed();
        } catch (Exception e) {
            log.error("log catch");
            StringWriter stringWriter = new StringWriter();
            PrintWriter out = new PrintWriter(stringWriter);
            e.printStackTrace(out);
            logData.setLogType(Log.EXCEPTION_LOG);
            logData.setException_detail(stringWriter.toString());
            logMapper.insertLog(logData);
            throw e;
        }
        logMapper.insertLog(logData);
        return res;
    }

    private Log setLog(ProceedingJoinPoint joinPoint, HttpServletRequest req) {
        Date time = new Date();
        String method = joinPoint.getSignature().getName();
        StringBuilder builder = new StringBuilder();

        Arrays.stream(joinPoint.getArgs()).sequential().forEach(arg -> {
            builder.append(arg.getClass().getName());
            builder.append(" = ");
            builder.append(arg);
            builder.append(", ");
        });
        String params = builder.toString();
        String ip = req.getRemoteAddr();
        String addr = req.getRequestURI();

        String browser = req.getHeader("User-Agent");

        return new Log(time.getTime(), method + " Log", Log.OPERATION_LOG, method, params, ip, time.getTime(), "test",
            addr, browser, null, time);
    }
}
