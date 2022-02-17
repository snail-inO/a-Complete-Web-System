package com.multi.sys.controller;

import com.alibaba.fastjson.JSON;
import com.multi.common.returns.Json;
import com.multi.common.returns.exception.CustomException;
import com.multi.common.returns.exception.ExceptionEnum;
import com.multi.common.utils.CURDUtil;
import com.multi.log.aop.Logging;
import com.multi.sys.bean.Menu;
import com.multi.sys.mapper.MenuMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

@Api
@RestController
public class MenuController {
    @Autowired
    private MenuMapper menuMapper;
    private Menu menu;

    @Logging
    @PostMapping("/menu/curd")
    public Json menuOperations(HttpServletRequest req) {
        try {
            doOperations(req);
        } catch (IOException e) {
            throw new CustomException(ExceptionEnum.BAD_REQUEST, e);
        }

        if (menu != null)
            return new Json(menu);
        else
            return new Json();
    }

    @Logging
    @GetMapping("/menu/authentication")
    @PostAuthorize("hasAnyAuthority(returnObject.permission)")
    public Menu menuData(@RequestParam String menuId) {
        if (menuId.length() == 0)
            throw new CustomException(ExceptionEnum.BAD_REQUEST);
        long menuIdInt = Integer.parseInt(menuId);
        return menuMapper.selectMenuById(menuIdInt);
    }

    @Logging
    @GetMapping("/menu/child")
    public Set<Menu> childMenu(@RequestParam String menuId) {
        if (menuId.length() == 0)
            throw new CustomException(ExceptionEnum.BAD_REQUEST);
        long pid = Integer.parseInt(menuId);
        return menuMapper.selectMenuByPid(pid);
    }

    @Logging
    @GetMapping("/menu/relative")
    Set<Menu> relativeMenu(@RequestParam String menuId) {
        if (menuId.length() == 0)
            throw new CustomException(ExceptionEnum.BAD_REQUEST);
        long menuIdInt = Integer.parseInt(menuId);
        return getRelativeMenu(menuIdInt);
    }

    private Set<Menu> getRelativeMenu(long menuId) {
        Menu curMenu = menuMapper.selectMenuById(menuId);
        long pid = curMenu.getPid();
        Menu parent = menuMapper.selectMenuById(pid);
        Set<Menu> relative = menuMapper.selectMenuByPid(pid);
        relative.add(parent);

        return relative;
    }

    private void doOperations(HttpServletRequest req) throws IOException{
        String method = req.getParameter("method");
        String menuId = req.getParameter("menuId");
        if (method.length() == 0 || menuId.length() == 0)
            throw new CustomException(ExceptionEnum.BAD_REQUEST);
        int methodInt = Integer.parseInt(method);
        long menuIdInt = Integer.parseInt(menuId);
        Menu input = null;
        if (methodInt == CURDUtil.CREATE || methodInt == CURDUtil.UPDATE) {
            input = JSON.parseObject(CURDUtil.getBody(req), Menu.class);
        }

        switch (methodInt) {
            case CURDUtil.CREATE:
                menuMapper.insertMenu(input);
                break;
            case CURDUtil.UPDATE:
                menuMapper.updateMenu(input);
                break;
            case CURDUtil.RETRIEVE:
                menu = menuMapper.selectMenuById(menuIdInt);
                break;
            case CURDUtil.DELETE:
                menuMapper.deleteMenuById(menuIdInt);
                break;
            default:
                throw new CustomException(ExceptionEnum.BAD_REQUEST);
        }
    }
}
