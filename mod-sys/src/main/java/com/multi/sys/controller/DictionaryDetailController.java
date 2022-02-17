package com.multi.sys.controller;

import com.alibaba.fastjson.JSON;
import com.multi.common.returns.Json;
import com.multi.common.returns.exception.CustomException;
import com.multi.common.returns.exception.ExceptionEnum;
import com.multi.common.utils.CURDUtil;
import com.multi.common.utils.PagingUtil;
import com.multi.log.aop.Logging;
import com.multi.sys.bean.Dictionary;
import com.multi.sys.bean.DictionaryDetail;
import com.multi.sys.bean.DictionaryDetail;
import com.multi.sys.mapper.DictionaryDetailMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Api
@RestController
public class DictionaryDetailController {
    @Autowired
    private DictionaryDetailMapper dictionaryDetailMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private DictionaryDetail dictionaryDetail;

    @Logging
    @PostMapping("/dictionary_detail/curd")
    public Json curd(HttpServletRequest req) {
        try {
            doOperations(req);
        } catch (IOException e) {
            throw new CustomException(ExceptionEnum.BAD_REQUEST, e);
        }

        if (dictionaryDetail != null)
            return new Json(dictionaryDetail);

        return new Json();
    }

    @Logging
    @GetMapping("/dictionary_detail/page")
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
        return new PagingUtil<>(pageNum, 5, dictionaryDetailMapper.retrieveAllDictionaryDetail());
    }

    private void doOperations(HttpServletRequest req) throws IOException {
        String method = req.getParameter("method");
        String detailId;
        long detailIdInt = -1;
        if (method == null || method.length() == 0)
            throw new CustomException(ExceptionEnum.BAD_REQUEST);
        int methodInt = Integer.parseInt(method);
        DictionaryDetail input = null;

        if (methodInt == CURDUtil.CREATE || methodInt == CURDUtil.UPDATE) {
            input = JSON.parseObject(CURDUtil.getBody(req), DictionaryDetail.class);
        } else if (methodInt <= CURDUtil.DELETE){
            detailId = req.getParameter("detailId");
            if (detailId == null || detailId.length() == 0)
                throw new CustomException(ExceptionEnum.BAD_REQUEST);
            detailIdInt = Integer.parseInt(detailId);
        }

        switch (methodInt)
        {
            case CURDUtil.CREATE:
                dictionaryDetailMapper.createDictionaryDetail(input);
                dictionaryDetail = null;
                break;
            case CURDUtil.UPDATE:
                dictionaryDetailMapper.updateDictionaryDetail(input);
                dictionaryDetail = null;
                break;
            case CURDUtil.RETRIEVE:
                dictionaryDetail = dictionaryDetailMapper.retrieveDictionaryDetail(detailIdInt);
                break;
            case CURDUtil.DELETE:
                dictionaryDetailMapper.deleteDictionaryDetail(detailIdInt);
                dictionaryDetail = null;
                break;
            default:
                throw new CustomException(ExceptionEnum.BAD_REQUEST);
        }
    }
}
