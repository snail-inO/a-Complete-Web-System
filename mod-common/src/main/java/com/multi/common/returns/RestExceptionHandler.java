package com.multi.common.returns;

import com.multi.common.returns.exception.CustomException;
import com.multi.common.returns.exception.ExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class RestExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public Json customExceptionHandler(CustomException e) {
        LOGGER.error("Service exception: {}", e.getErrorMsg());

        return new Json(false, e.getErrorCode(), e.getErrorMsg());
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public Json nullPointerExceptionHandler(NullPointerException e) {
        LOGGER.error("null pointer exception: {}", e);

        return new Json(ExceptionEnum.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Json exceptionHandler(Exception e) {
        LOGGER.error("Internal error: ", e);
        return new Json(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }
}
