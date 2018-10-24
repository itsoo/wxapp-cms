package com.wxapp.cms.controller;

import com.jfinal.kit.Base64Kit;
import com.wxapp.cms.controller.base.BaseController;
import com.wxapp.cms.model.User;
import com.wxapp.cms.service.UserService;

import java.util.Map;

/**
 * 用户控制层
 *
 * @author zxy
 */
public class UserController extends BaseController {

    private UserService service = UserService.me;

    /**
     * 用户登录
     */
    public void login() {
        Map<String, Object> map = getRequestMap();
        String res = service.login(map);
        String prefix = "null-";
        // 新增用户为试用
        if (res.startsWith(prefix)) {
            res = res.replace(prefix, "");
            setAttr("sessionKey", Base64Kit.encode(res));
            setAttr("errorMsg", User.TRYDAYS);
        }
        // 用户已被禁用
        else if (User.DISABLED.equals(res)) {
            setAttr("errorMsg", "账号已过期，请联系 ");
        }
        // 正常登录
        else {
            setAttr("sessionKey", Base64Kit.encode(res));
        }
        renderJson();
    }

    /**
     * 查询备忘录
     */
    public void queryMemo() {
        String memo = service.queryMemo(getOpenId());
        setAttr("memo", memo == null ? "" : memo);
        renderJson();
    }

    /**
     * 修改备忘录
     */
    public void saveMemo() {
        Map<String, Object> map = getRequestMap("id");
        setAttr("success", service.saveMemo(map));
        renderJson();
    }
}
