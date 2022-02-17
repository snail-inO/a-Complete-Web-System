package com.multi.sys.controller;

import com.alibaba.fastjson.JSON;
import com.multi.common.returns.Json;
import com.multi.common.returns.exception.CustomException;
import com.multi.common.returns.exception.ExceptionEnum;
import com.multi.common.utils.CURDUtil;
import com.multi.common.utils.ExportUtil;
import com.multi.common.utils.PagingUtil;
import com.multi.common.utils.sqlbuilder.DepartmentSQLBuilder;
import com.multi.log.aop.Logging;
import com.multi.sys.bean.Department;
import com.multi.sys.mapper.DepartmentMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@Api
@RestController
public class DepartmentController {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Department department;

    @Logging
    @PostMapping("/department/curd")
    public Json curd(HttpServletRequest req) {
        try {
            doOperations(req);
        } catch (IOException e) {
            throw new CustomException(ExceptionEnum.BAD_REQUEST, e);
        }

        if (department != null)
            return new Json(department);

        return new Json();
    }

    @Logging
    @GetMapping("/department/export")
    public void export(HttpServletResponse resp) {
        ExportUtil.setJdbcTemplate(jdbcTemplate);
        try {
            ExportUtil.export(resp, DepartmentSQLBuilder.DEP_TB);
        } catch (Exception e) {
            throw new CustomException(ExceptionEnum.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Logging
    @GetMapping("/department/page")
    public Json page(@RequestParam(required = false) String pageNum) {
        int pageNumber;
        if (pageNum == null || pageNum.length() == 0)
            pageNumber = 1;
        else {
            pageNumber = Integer.parseInt(pageNum);
            if (pageNumber <= 0)
                pageNumber = 1;
        }

        return new Json(doPaging(pageNumber));
    }

    private PagingUtil doPaging(int pageNum) {
        return new PagingUtil<Department>(pageNum, 5, departmentMapper.retrieveAllDepartment());
    }

    private void doOperations(HttpServletRequest req) throws IOException{
        String method = req.getParameter("method");
        String deptId;
        long deptIdInt = -1;
        if (method == null || method.length() == 0)
            throw new CustomException(ExceptionEnum.BAD_REQUEST);
        int methodInt = Integer.parseInt(method);
        Department input = null;

        if (methodInt == CURDUtil.CREATE || methodInt == CURDUtil.UPDATE) {
            input = JSON.parseObject(CURDUtil.getBody(req), Department.class);
        } else if (methodInt <= CURDUtil.DELETE){
            deptId = req.getParameter("deptId");
            if (deptId == null || deptId.length() == 0)
                throw new CustomException(ExceptionEnum.BAD_REQUEST);
            deptIdInt = Integer.parseInt(deptId);
        }

        switch (methodInt)
        {
            case CURDUtil.CREATE:
                departmentMapper.createDepartment(input);
                department = null;
                break;
            case CURDUtil.UPDATE:
                departmentMapper.updateDepartment(input);
                department = null;
                break;
            case CURDUtil.RETRIEVE:
                department = departmentMapper.retrieveDepartment(deptIdInt);
                break;
            case CURDUtil.DELETE:
                departmentMapper.deleteDepartment(deptIdInt);
                department = null;
                break;
            default:
                throw new CustomException(ExceptionEnum.BAD_REQUEST);
        }
    }
}
