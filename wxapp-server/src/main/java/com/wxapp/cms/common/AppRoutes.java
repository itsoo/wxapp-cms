package com.wxapp.cms.common;

import com.jfinal.config.Routes;
import com.wxapp.cms.controller.*;

/**
 * AppRoutes
 *
 * @author zxy
 */
class AppRoutes {

    void configRoute(Routes routes) {
        routes.add(new ApiRoutes());
        routes.add(new AdminRoutes());
    }

    /**
     * API路由
     */
    class ApiRoutes extends Routes {
        @Override
        public void config() {
            add("/", IndexController.class);
            add("/custom", CustomController.class);
            add("/setting", SettingController.class);
            add("/store", StoreController.class);
            add("/user", UserController.class);
        }
    }

    /**
     * 后端管理路由
     */
    class AdminRoutes extends Routes {
        @Override
        public void config() {
            add("/admin", AdminController.class, "/");
        }
    }
}
