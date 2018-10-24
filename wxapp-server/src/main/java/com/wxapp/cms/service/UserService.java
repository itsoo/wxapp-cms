package com.wxapp.cms.service;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;
import com.wxapp.cms.model.User;
import com.wxapp.cms.util.AesCbcUtil;

import java.util.Date;
import java.util.Map;

/**
 * 用户业务层
 *
 * @author zxy
 */
public class UserService {

    private static final String URL = "https://api.weixin.qq.com/sns/jscode2session?appid=${appId}&secret=${appSecret}&js_code=${jsCode}&grant_type=authorization_code";

    public static final UserService me = new UserService();

    private User dao = new User().dao();

    /**
     * 用户登录
     * 1.验证系统中是否存在该用户
     * 2.存在验证用户是否可用
     * 3.不存在用户情况将用户入库
     *
     * @param map
     * @return
     */
    public String login(Map<String, Object> map) {
        Date date = new Date();
        // 授权
        String appId = PropKit.get("appId");
        String appSecret = PropKit.get("appSecret");
        // js_code
        String jsCode = (String) map.get("jsCode");
        // URL
        String url = URL.replace("${appId}", appId)
                .replace("${appSecret}", appSecret)
                .replace("${jsCode}", jsCode);
        // 发送请求
        String res = HttpKit.get(url);
        // 解析相应内容
        JSONObject resJson = JSONObject.parseObject(res);
        // 用户的唯一标识
        String id = resJson.getString("openid");
        String sessionKey = resJson.getString("session_key");
        String encryptedData = (String) map.get("encryptedData");
        String iv = (String) map.get("iv");
        String result = AesCbcUtil.decrypt(sessionKey, encryptedData, iv);
        String name = JSONObject.parseObject(result).getString("nickName");
        // 数据库查询用户
        User user = dao.findById(id);
        // 用户不存在
        if (user == null) {
            // 插入用户信息
            User insert = new User().setId(id)
                    .setName(name)
                    .setState(User.TRYOUT)
                    .setLastTime(date);
            insert.save();
            // 插入用户设置
            SettingService.me.add(id);
            return "null-" + id;
        } else {
            String state = user.getState();
            // 已禁用或已过试用期
            if (User.DISABLED.equals(state)) {
                return User.DISABLED;
            } else if (User.TRYOUT.equals(state)) {
                int days = (int) ((date.getTime() - user.getLastTime().getTime()) / (24 * 3600000));
                if (days > User.TRYDAYS) {
                    user.setName(name)
                            .setState(User.DISABLED)
                            .setLastTime(date)
                            .update();
                    return User.DISABLED;
                }
                return id;
            }
        }
        // 更新登录用户信息
        user.setName(name).setLastTime(date).update();
        return id;
    }

    /**
     * 查询备忘录
     *
     * @param id
     * @return
     */
    public String queryMemo(String id) {
        User user = dao.findById(id);
        if (user != null) {
            return user.getMemo();
        }
        return null;
    }

    /**
     * 修改备忘录
     *
     * @return
     */
    public boolean saveMemo(Map<String, Object> map) {
        return Db.update("update t_user set memo = ? where id = ?", map.get("memo"), map.get("id")) > 0;
    }

    /**
     * 查询用户列表
     *
     * @param name
     * @return
     */
    public Page<User> queryListPage(String name) {
        SqlPara sql = Db.getSqlPara("user.query", name);
        return dao.paginate(1, 10, sql);
    }

    /**
     * 修改用户
     */
    public void update() {
        new User().update();
    }

    /**
     * 删除用户
     *
     * @param openId
     */
    public void delete(String openId) {
        dao.deleteById(openId);
    }
}
