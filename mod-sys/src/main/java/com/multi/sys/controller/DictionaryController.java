package com.multi.sys.controller;

import com.alibaba.fastjson.JSON;
import com.multi.common.returns.Json;
import com.multi.common.returns.exception.CustomException;
import com.multi.common.returns.exception.ExceptionEnum;
import com.multi.common.utils.CURDUtil;
import com.multi.common.utils.ExportUtil;
import com.multi.common.utils.PagingUtil;
import com.multi.common.utils.sqlbuilder.DictionarySQLBuilder;
import com.multi.log.aop.Logging;
import com.multi.sys.bean.Dictionary;
import com.multi.sys.mapper.DictionaryMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api
@RestController
public class DictionaryController {
    @Autowired
    private DictionaryMapper dictionaryMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Dictionary dictionary;

    @Logging
    @PostMapping("/dictionary/curd")
    public Json curd(HttpServletRequest req) {
        try {
            doOperations(req);
        } catch (IOException e) {
            throw new CustomException(ExceptionEnum.BAD_REQUEST, e);
        }

        if (dictionary != null)
            return new Json(dictionary);

        return new Json();
    }

    @Logging
    @GetMapping("/dictionary/export")
    public void export(HttpServletResponse resp) {
        ExportUtil.setJdbcTemplate(jdbcTemplate);
        try {
            ExportUtil.export(resp, DictionarySQLBuilder.DIC_TB);
        } catch (Exception e) {
            throw new CustomException(ExceptionEnum.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Logging
    @GetMapping("/dictionary/page")
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
        return new PagingUtil<Dictionary>(pageNum, 5, dictionaryMapper.retrieveAllDictionary());
    }

    private void doOperations(HttpServletRequest req) throws IOException {
        String method = req.getParameter("method");
        String dictId;
        long dictIdInt = -1;
        if (method == null || method.length() == 0)
            throw new CustomException(ExceptionEnum.BAD_REQUEST);
        int methodInt = Integer.parseInt(method);
        Dictionary input = null;

        if (methodInt == CURDUtil.CREATE || methodInt == CURDUtil.UPDATE) {
            input = JSON.parseObject(CURDUtil.getBody(req), Dictionary.class);
        } else if (methodInt <= CURDUtil.DELETE){
            dictId = req.getParameter("dictId");
            if (dictId == null || dictId.length() == 0)
                throw new CustomException(ExceptionEnum.BAD_REQUEST);
            dictIdInt = Integer.parseInt(dictId);
        }

        switch (methodInt)
        {
            case CURDUtil.CREATE:
                dictionaryMapper.createDictionary(input);
                dictionary = null;
                break;
            case CURDUtil.UPDATE:
                dictionaryMapper.updateDictionary(input);
                dictionary = null;
                break;
            case CURDUtil.RETRIEVE:
                dictionary = dictionaryMapper.retrieveDictionary(dictIdInt);
                break;
            case CURDUtil.DELETE:
                dictionaryMapper.deleteDictionary(dictIdInt);
                dictionary = null;
                break;
            default:
                throw new CustomException(ExceptionEnum.BAD_REQUEST);
        }
    }
}
