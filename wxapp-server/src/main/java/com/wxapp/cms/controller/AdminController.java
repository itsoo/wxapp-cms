package com.wxapp.cms.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.wxapp.cms.service.UserService;

import java.util.HashMap;
import java.util.Map;

/**
 * 后台管理
 *
 * @author zxy
 */
public class AdminController extends Controller {

    private UserService service = UserService.me;

    public void index() {
        render("admin.html");
    }

    public void user() {
        Page page = service.queryListPage(getPara());
        Map<String, Object> map = new HashMap<>(6);
        map.put("code", 200);
        map.put("list", page.getList());
        map.put("totalRow", page.getTotalRow());
        renderJson(map);
    }
}
