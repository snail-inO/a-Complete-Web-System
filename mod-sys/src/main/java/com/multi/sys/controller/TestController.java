package com.multi.sys.controller;

import com.multi.common.aop.RestrictAccess;
import com.multi.common.returns.exception.CustomException;
import com.multi.common.returns.exception.ExceptionEnum;
import com.multi.log.aop.Logging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api
public class TestController {
    @ApiOperation(value = "test")
    @RequestMapping("/test")
    @RestrictAccess
    @Logging
    public String test(@RequestParam String name) {
        if (name.equals("123"))
            throw new CustomException(ExceptionEnum.BAD_REQUEST.getResultCode(), ExceptionEnum.BAD_REQUEST.getResultMsg());
//        String s = null;
//        s.length();
        log.info("Hello test");
        return name + "Hello";
    }
}
