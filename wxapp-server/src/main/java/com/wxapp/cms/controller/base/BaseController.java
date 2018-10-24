package com.wxapp.cms.controller.base;

import com.jfinal.core.Const;
import com.jfinal.core.Controller;
import com.jfinal.json.FastJson;
import com.jfinal.kit.Base64Kit;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BaseController
 *
 * @author zxy
 */
public class BaseController extends Controller {

    /**
     * 获取 openId
     *
     * @return
     */
    protected String getOpenId() {
        String sessionKey = getPara();
        if (sessionKey == null) {
            return null;
        }
        return Base64Kit.decodeToStr(sessionKey);
    }

    /**
     * 获取 openId
     *
     * @param i
     * @return
     */
    protected String getOpenId(int i) {
        String sessionKey = getPara(i);
        if (sessionKey == null) {
            return null;
        }
        return Base64Kit.decodeToStr(sessionKey);
    }

    /**
     * 获取解码参数
     *
     * @param i
     * @return
     */
    protected String getDecodePara(int i) {
        return getDecodePara(i, "");
    }

    /**
     * 获取解码参数
     *
     * @param i
     * @param defVal
     * @return
     */
    protected String getDecodePara(int i, String defVal) {
        try {
            return URLDecoder.decode(getPara(i, defVal), Const.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 获取请求 RequestBody
     *
     * @return
     */
    protected Map<String, Object> getRequestMap() {
        String para;
        try {
            para = IOUtils.toString(getRequest().getInputStream(), Const.DEFAULT_ENCODING);
        } catch (IOException e) {
            return null;
        }
        return FastJson.getJson().parse(para, HashMap.class);
    }

    /**
     * 获取请求 RequestBody
     *
     * @return
     */
    protected Map<String, Object> getRequestMap(String convertKey) {
        Map<String, Object> map = getRequestMap();
        map.put(convertKey, Base64Kit.decodeToStr((String) map.get(convertKey)));
        return map;
    }

    /**
     * 获取请求 RequestBody
     *
     * @return
     */
    protected List getRequestList() {
        String para;
        try {
            para = IOUtils.toString(getRequest().getInputStream(), Const.DEFAULT_ENCODING);
        } catch (IOException e) {
            return null;
        }
        return FastJson.getJson().parse(para, ArrayList.class);
    }
}
