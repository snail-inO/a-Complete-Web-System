package com.multi.sys.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class SysAuthenticationDetails extends WebAuthenticationDetails {
    private String code;

    public SysAuthenticationDetails(HttpServletRequest request) {
        super(request);
        code = request.getParameter("code");
    }

    public String getCode() {
        return code;
    }
}
