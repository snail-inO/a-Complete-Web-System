package com.multi.common.utils;

import com.alibaba.fastjson.JSON;
import com.multi.common.returns.exception.CustomException;
import com.multi.common.returns.exception.ExceptionEnum;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CURDUtil {
    public static final int CREATE = 0;
    public static final int UPDATE = 1;
    public static final int RETRIEVE = 2;
    public static final int DELETE = 3;

    public static String getBody(HttpServletRequest req) throws IOException {
        int len = req.getContentLength();
        byte buffer[] = new byte[len];
        InputStream in = req.getInputStream();

        in.read(buffer, 0, len  );
        in.close();

        return new String(buffer, StandardCharsets.UTF_8);
    }
}
