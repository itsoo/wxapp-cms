package com.wxapp.cms.util;

import com.jfinal.plugin.ehcache.CacheKit;

/**
 * EhCache 工具类
 *
 * @author zxy
 */
public class EhCacheUtil {

    private static final String CACHE_NAME = "wxapp";

    /**
     * 放入缓存
     *
     * @param id    数据唯一标识
     * @param key   键
     * @param value 值
     */
    public static void put(String id, String key, Object value) {
        CacheKit.put(CACHE_NAME, id + "." + key, value);
    }

    /**
     * 获取缓存
     *
     * @param id  数据唯一标识
     * @param key 键
     * @param <T> 值
     * @return
     */
    public static <T> T get(String id, String key) {
        return CacheKit.get(CACHE_NAME, id + "." + key);
    }

    /**
     * 获取缓存
     *
     * @param id     数据唯一标识
     * @param key    键
     * @param defVal 默认值
     * @param <T>    值
     * @return
     */
    public static <T> T get(String id, String key, T defVal) {
        T val = CacheKit.get(CACHE_NAME, id + "." + key);
        return val == null ? defVal : val;
    }

    /**
     * 获取缓存
     *
     * @param id     数据唯一标识
     * @param key    键
     * @param defVal 默认值
     * @return
     */
    public static int getInt(String id, String key, int defVal) {
        Object val = CacheKit.get(CACHE_NAME, id + "." + key);
        return val == null ? defVal : Integer.parseInt(val.toString());
    }
}
