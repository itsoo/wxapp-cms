package com.wxapp.cms.service;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wxapp.cms.model.Store;

import java.util.Map;

/**
 * 库存业务层
 *
 * @author zxy
 */
public class StoreService {

    public static final StoreService me = new StoreService();

    private Store dao = new Store().dao();

    /**
     * 库存管理查询
     *
     * @param id
     * @return
     */
    public Store queryById(String id) {
        return dao.findById(id);
    }

    /**
     * 查询提醒内容
     *
     * @return
     */
    public String queryWarnText(String id) {
        Store store = StoreService.me.queryById(id);
        String warnText = null;
        if (store != null) {
            Integer nowNum = store.getNowNum();
            Integer warnNum = store.getWarnNum();
            if (nowNum != null && warnNum != null && nowNum < warnNum) {
                warnText = store.getWarnText();
            }
        }
        return StrKit.isBlank(warnText) ? "" : warnText;
    }

    /**
     * 库存管理修改
     *
     * @param map
     * @return
     */
    public boolean update(Map<String, Object> map) {
        // 处理进货数量
        String bn = (String) map.remove("buy_num");
        Record store = new Record().setColumns(map);
        if (StrKit.notBlank(bn)) {
            int buyNum = Integer.parseInt(bn);
            // 设置当前库存数量
            Integer nowNum = store.get("now_num", Store.NOW_NUM);
            store.set("now_num", nowNum + buyNum);
        }
        // 处理预警数值
        String warnNum = store.get("warn_num");
        if (StrKit.isBlank(warnNum)) {
            store.set("warn_num", null);
        }
        boolean flag = Db.update("t_store", store);
        if (!flag) {
            flag = Db.save("t_store", store);
        }
        return flag;
    }

    /**
     * 库存减少 1 个单位
     *
     * @return
     */
    public boolean minus(String user) {
        Store store = dao.findById(user);
        Integer nowNum = store == null ? null : store.getNowNum();
        if (nowNum != null) {
            store.setNowNum(--nowNum);
            return store.update();
        }
        return false;
    }
}
