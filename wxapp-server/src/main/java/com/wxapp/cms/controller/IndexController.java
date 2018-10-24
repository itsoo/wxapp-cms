package com.wxapp.cms.controller;

import com.jfinal.kit.StrKit;
import com.wxapp.cms.controller.base.BaseController;
import com.wxapp.cms.service.StoreService;
import com.wxapp.cms.service.UserService;

/**
 * IndexController
 *
 * @author zxy
 */
public class IndexController extends BaseController {

    /**
     * 查询全部提醒
     */
    public void allWarn() {
        String id = getOpenId();
        setAttr("warnText", StoreService.me.queryWarnText(id));
        setAttr("memoWarn", StrKit.isBlank(UserService.me.queryMemo(id)));
        renderJson();
    }
}
