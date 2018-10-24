package com.wxapp.cms.controller;

import com.jfinal.kit.StrKit;
import com.wxapp.cms.controller.base.BaseController;
import com.wxapp.cms.model.Setting;
import com.wxapp.cms.service.CustomService;
import com.wxapp.cms.service.StoreService;
import com.wxapp.cms.util.EhCacheUtil;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 用户控制层
 *
 * @author zxy
 */
public class CustomController extends BaseController {

    private CustomService service = CustomService.me;

    /**
     * 客户列表查询
     *
     * @throws UnsupportedEncodingException
     */
    public void list() throws UnsupportedEncodingException {
        setAttrs(service.queryList(getParaToInt(0, 1), getDecodePara(1), getOpenId(2)));
        renderJson();
    }

    /**
     * 快速出货列表查询
     *
     * @throws UnsupportedEncodingException
     */
    public void quick() throws UnsupportedEncodingException {
        setAttrs(service.queryQuick(getParaToInt(0, 1), getDecodePara(1), getOpenId(2)));
        renderJson();
    }

    /**
     * 查询客户信息
     */
    public void queryById() {
        renderJson(service.queryById(getParaToInt()));
    }

    /**
     * 保存客户信息验证 id
     * > 存在修改客户信息
     * > 不存在添加客户信息
     */
    public void save() {
        Map<String, Object> map = getRequestMap("user");
        boolean flag;
        if (StrKit.isBlank((String) map.get("id"))) {
            flag = service.add(map);
        } else {
            flag = service.update(map);
        }
        setAttr("success", flag);
        renderJson();
    }

    /**
     * 删除客户信息
     */
    public void delete() {
        setAttr("success", service.delete(getRequestList()));
        renderJson();
    }

    /**
     * 添加购买数量
     */
    public void addBuyCount() {
        setAttr("success", service.addBuyCount(getParaToInt(0)));
        StoreService.me.minus(getOpenId(1));
        renderJson();
    }

    /**
     * 修改赠送数量
     */
    public void updateGiftCount() {
        setAttr("success", service.updateGiftCount(getParaToInt(0)));
        String user = getOpenId(1);
        Object gift = EhCacheUtil.get(user, "gift");
        if (gift != null && Setting.ENABLED.equals(gift.toString())) {
            StoreService.me.minus(user);
        }
        renderJson();
    }
}
