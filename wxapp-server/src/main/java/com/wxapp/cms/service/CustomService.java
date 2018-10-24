package com.wxapp.cms.service;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.wxapp.cms.model.*;
import com.wxapp.cms.util.EhCacheUtil;

import java.math.BigDecimal;
import java.util.*;

/**
 * 客户业务层
 *
 * @author zxy
 */
public class CustomService {

    public static final CustomService me = new CustomService();

    private Custom dao = new Custom().dao();

    /**
     * 客户列表查询
     *
     * @param pageNum
     * @param search
     * @param sessionKey
     * @return
     */
    public Map<String, Object> queryList(int pageNum, String search, String sessionKey) {
        SqlPara sql = Db.getSqlPara("custom.queryList", search, sessionKey);
        int pageSize = EhCacheUtil.getInt(sessionKey, "list_size", Setting.LIST_SIZE);
        Page<Custom> page = dao.paginate(pageNum, pageSize, sql);
        // 封装返回内容
        int pageNumber = page.getPageNumber();
        Map<String, Object> map = new HashMap<>(6);
        map.put("dataList", page.getList());
        map.put("hasNext", !page.isLastPage());
        map.put("pageNum", pageNumber);
        return map;
    }

    /**
     * 快速出货列表查询
     *
     * @param pageNum
     * @param search
     * @param sessionKey
     * @return
     */
    public Map<String, Object> queryQuick(int pageNum, String search, String sessionKey) {
        int pageSize = EhCacheUtil.getInt(sessionKey, "list_size", Setting.LIST_SIZE);
        String select = new String(new StringBuilder()
                .append(" select t.id, t.name, t.phone, t.address, ")
                .append("   count(bh.id) total, ")
                .append("   sum(bh.free - 1) gift "));
        String sqlExceptSelect = new String(new StringBuilder()
                .append(" from t_custom t ")
                .append(" left join t_buy_history bh on t.id = bh.custom ")
                .append("   where (t.name like concat('%', ?, '%') ")
                .append("     or t.phone like concat('%', ?, '%') ")
                .append("     or t.address like concat('%', ?, '%')) ")
                .append("   and t.user = ? ")
                .append(" group by t.id, t.name, t.phone, t.address "));
        Page<Custom> page = dao.paginate(pageNum, pageSize, true,
                select, sqlExceptSelect, search, search, search, sessionKey);
        List<Custom> dataList = page.getList();
        // 用户设置赠送阈值
        int free = EhCacheUtil.getInt(sessionKey, "free", Integer.MAX_VALUE);
        Integer gift;
        for (Custom custom : dataList) {
            // 计算赠送数量
            gift = custom.getInt("gift");
            if (gift != null) {
                custom.put("gift", Math.abs(gift) / free);
            }
        }
        // 封装返回内容
        int pageNumber = page.getPageNumber();
        Map<String, Object> map = new HashMap<>(6);
        map.put("dataList", dataList);
        map.put("hasNext", !page.isLastPage());
        map.put("pageNum", pageNumber);
        return map;
    }

    /**
     * 查询客户信息
     *
     * @param id
     * @return
     */
    public Map<String, Object> queryById(int id) {
        Map<String, Object> map = new HashMap<>(16);
        Record custom = Db.findById("t_custom", id);
        map.putAll(custom.getColumns());
        // 客户购买数量
        Record buyHistory = Db.findFirst("select count(*) Count from t_buy_history where custom = ?", id);
        map.put("Count", buyHistory.get("Count"));
        // 客户标签集合
        List<Label> labelList = new Label().dao().find("select name from t_label where custom = ?", id);
        List<String> labels = new ArrayList<>(8);
        for (Label label : labelList) {
            labels.add(label.getName());
        }
        map.put("Label", labels);
        return map;
    }

    /**
     * 添加客户信息
     *
     * @param map
     * @return
     */
    public boolean add(Map<String, Object> map) {
        Date date = new Date();
        // 添加客户
        Custom custom = new Custom().put(map).setCreateDate(date);
        custom.save();
        int id = custom.getId();
        // 添加购买数量
        String strCount = (String) map.get("Count");
        strCount = StrKit.isBlank(strCount) ? "0" : strCount;
        for (int i = 0, len = Integer.parseInt(strCount); i < len; i++) {
            addBuyCount(id);
        }
        // 添加价格记录
        new PriceHistory().setCustom(id)
                .setPrice(new BigDecimal((String) custom.get("price")))
                .setUpdateDate(date)
                .save();
        // 添加客户标签
        List<String> labels = JSONObject.parseArray(map.get("Label").toString(), String.class);
        saveLabel(id, labels);
        return true;
    }

    /**
     * 修改客户信息
     *
     * @param map
     * @return
     */
    public boolean update(Map<String, Object> map) {
        // 修改客户
        String jsonLabel = map.remove("Label").toString();
        Record custom = new Record().setColumns(map);
        Db.update("t_custom", custom);
        int id = custom.getInt("id");
        // 修改客户标签
        Db.delete("delete from t_label where custom = ?", id);
        List<String> labels = JSONObject.parseArray(jsonLabel, String.class);
        saveLabel(id, labels);
        return true;
    }

    /**
     * 批量删除客户信息
     *
     * @param list
     */
    public boolean delete(List<String> list) {
        boolean flag = false;
        for (String id : list) {
            // 删除价格历史
            Db.delete("delete from t_price_history where custom = ?", id);
            // 删除购买历史
            Db.delete("delete from t_buy_history where custom = ?", id);
            // 删除客户标签
            Db.delete("delete from t_label where custom = ?", id);
            // 删除客户
            flag = dao.deleteById(Integer.parseInt(id));
        }
        return flag;
    }

    /**
     * 添加购买数量
     *
     * @return
     */
    public boolean addBuyCount(int custom) {
        return new BuyHistory().setCustom(custom)
                .setBuyDate(new Date())
                .setFree(BuyHistory.FREE)
                .save();
    }

    /**
     * 修改赠送数量
     *
     * @return
     */
    public boolean updateGiftCount(int custom) {
        SqlPara sql = Db.getSqlPara("custom.updateGiftCount", custom);
        return Db.update(sql) > 0;
    }

    /**
     * 保存客户标签
     *
     * @param id
     * @param labels
     */
    private void saveLabel(int id, List<String> labels) {
        for (String s : labels) {
            Label label = new Label();
            label.setCustom(id);
            label.setName(s);
            label.save();
        }
    }
}
