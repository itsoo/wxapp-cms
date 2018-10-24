package com.wxapp.cms.service;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wxapp.cms.model.Setting;
import com.wxapp.cms.util.EhCacheUtil;

import java.util.Map;

/**
 * 设置业务层
 *
 * @author zxy
 */
public class SettingService {

    public static final SettingService me = new SettingService();

    private Setting dao = new Setting().dao();

    /**
     * 查询系统设置
     *
     * @param id
     * @return
     */
    public Setting queryById(String id) {
        return dao.findById(id);
    }

    /**
     * 添加系统设置
     *
     * @param id
     * @return
     */
    public boolean add(String id) {
        return new Setting().setId(id)
                .setListSize(Setting.LIST_SIZE)
                .setTime(Setting.ENABLED)
                .setGift(Setting.ENABLED)
                .save();
    }

    /**
     * 修改系统设置
     *
     * @param map
     * @return
     */
    public boolean update(Map<String, Object> map) {
        Record setting = new Record().setColumns(map);
        String free = setting.get("free");
        if (StrKit.isBlank(free)) {
            setting.set("free", null);
        }
        boolean flag = Db.update("t_setting", setting);
        if (flag) {
            String id = setting.getStr("id");
            EhCacheUtil.put(id, "list_size", setting.get("list_size"));
            EhCacheUtil.put(id, "time", setting.get("time"));
            EhCacheUtil.put(id, "free", setting.get("free"));
            EhCacheUtil.put(id, "gift", setting.get("gift"));
        }
        return flag;
    }
}
