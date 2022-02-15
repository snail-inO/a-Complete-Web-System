package com.multi.sys.controller;

import com.alibaba.fastjson.JSON;
import com.multi.common.returns.Json;
import com.multi.common.returns.exception.CustomException;
import com.multi.common.returns.exception.ExceptionEnum;
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
    private static final int CREATE = 0;
    private static final int UPDATE = 1;
    private static final int RETRIEVE = 2;
    private static final int DELETE = 3;

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
        if (methodInt == CREATE || methodInt == UPDATE) {
            input = JSON.parseObject(getBody(req), Menu.class);
        }

        switch (methodInt) {
            case CREATE:
                menuMapper.insertMenu(input);
                break;
            case UPDATE:
                menuMapper.updateMenu(input);
                break;
            case RETRIEVE:
                menu = menuMapper.selectMenuById(menuIdInt);
                break;
            case DELETE:
                menuMapper.deleteMenuById(menuIdInt);
                break;
            default:
                throw new CustomException(ExceptionEnum.BAD_REQUEST);
        }
    }
    private String getBody(HttpServletRequest req) throws IOException {
        int len = req.getContentLength();
        byte[] buffer = new byte[len];
        ServletInputStream in = req.getInputStream();

        in.read(buffer, 0, len);
        in.close();

        return new String(buffer, StandardCharsets.UTF_8);
    }
}
