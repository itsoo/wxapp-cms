package com.wxapp.cms.controller;

import com.wxapp.cms.controller.base.BaseController;
import com.wxapp.cms.service.SettingService;

/**
 * 设置控制层
 *
 * @author zxy
 */
public class SettingController extends BaseController {

    private SettingService service = SettingService.me;

    /**
     * 查询系统设置
     */
    public void queryById() {
        renderJson(service.queryById(getOpenId()));
    }

    /**
     * 修改系统设置
     */
    public void update() {
        setAttr("success", service.update(getRequestMap("id")));
        renderJson();
    }
}
