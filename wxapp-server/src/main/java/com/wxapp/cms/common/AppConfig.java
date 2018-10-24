package com.wxapp.cms.common;

import com.jfinal.config.*;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.TxByMethodRegex;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;
import com.wxapp.cms.model.Setting;
import com.wxapp.cms.model.User;
import com.wxapp.cms.model._MappingKit;
import com.wxapp.cms.util.EhCacheUtil;

import java.util.List;

/**
 * AppConfig
 *
 * @author zxy
 */
public class AppConfig extends JFinalConfig {

    /**
     * 配置常量
     */
    @Override
    public void configConstant(Constants constants) {
        PropKit.use("config.properties");
        constants.setDevMode(PropKit.getBoolean("devMode"));
    }

    /**
     * 配置路由
     */
    @Override
    public void configRoute(Routes routes) {
        new AppRoutes().configRoute(routes);
    }

    /**
     * 配置共享模板
     */
    @Override
    public void configEngine(Engine engine) {}

    /**
     * 配置插件
     */
    @Override
    public void configPlugin(Plugins plugins) {
        // 配置 Druid 数据库连接池插件
        DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("url"), PropKit.get("username"), PropKit.get("password"));
        plugins.add(druidPlugin);
        // 配置 ehCache 缓存插件
        plugins.add(new EhCachePlugin());
        // 配置 ActiveRecord 插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        // MappingKit 配置所有映射
        _MappingKit.mapping(arp);
        // 配置 SQL 模板路径
        arp.setBaseSqlTemplatePath(PathKit.getRootClassPath() + "/mapper");
        arp.addSqlTemplate("all.sql");
        arp.setShowSql(PropKit.getBoolean("devMode"));
        plugins.add(arp);
    }

    /**
     * 配置全局拦截器
     */
    @Override
    public void configInterceptor(Interceptors interceptors) {
        // 配置事务拦截
        interceptors.add(new TxByMethodRegex("(.*add.*|.*update.*|.*delete.*)"));
    }

    /**
     * 配置处理器
     */
    @Override
    public void configHandler(Handlers handlers) {
        handlers.add(new ContextPathHandler("basePath"));
    }

    /**
     * 应用启动后回调
     */
    @Override
    public void afterJFinalStart() {
        User userDao = new User().dao();
        // 用户列表
        List<User> userList = userDao.find(Db.getSqlPara("user.query"));
        Setting settingDao = new Setting().dao();
        Setting setting;
        String id;
        // 初始化用户设置缓存
        for (User user : userList) {
            id = user.getId();
            // 用户设置
            setting = settingDao.findById(id);
            if (setting == null) {
                setting = new Setting().setId(id)
                        .setListSize(Setting.LIST_SIZE)
                        .setTime(Setting.ENABLED);
                setting.save();
            }
            EhCacheUtil.put(id, "list_size", setting.getListSize());
            EhCacheUtil.put(id, "time", setting.getTime());
            EhCacheUtil.put(id, "free", setting.getFree());
        }
    }
}
