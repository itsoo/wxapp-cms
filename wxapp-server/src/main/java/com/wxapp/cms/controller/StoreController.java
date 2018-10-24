package com.wxapp.cms.controller;

import com.wxapp.cms.controller.base.BaseController;
import com.wxapp.cms.service.StoreService;

/**
 * 库存控制层
 *
 * @author zxy
 */
public class StoreController extends BaseController {

    private StoreService service = StoreService.me;

    /**
     * 查询库存信息
     */
    public void queryById() {
        renderJson(service.queryById(getOpenId()));
    }

    /**
     * 修改库存信息
     */
    public void update() {
        setAttr("success", service.update(getRequestMap("id")));
        renderJson();
    }
}
