package com.multi.common.returns;

import com.alibaba.fastjson.JSON;
import com.multi.common.returns.exception.ExceptionEnum;


public class Json {
    private boolean success;
    private String code;
    private String msg;
    private Object data;

    public Json() {
        success = true;
        code = "200";
        msg = "OK";
    }

    public Json(Object data) {
        success = true;
        code = "200";
        msg = "OK";
        this.data = data;
    }

    public Json(boolean success, String code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    public Json(boolean success, String code, String msg, Object data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Json(ExceptionEnum e) {
        success = false;
        this.code = e.getResultCode();
        this.msg = e.getResultMsg();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
