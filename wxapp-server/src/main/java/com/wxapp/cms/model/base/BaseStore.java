package com.wxapp.cms.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseStore<M extends BaseStore<M>> extends Model<M> implements IBean {

	public M setId(java.lang.String id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.String getId() {
		return getStr("id");
	}

	public M setNowNum(java.lang.Integer nowNum) {
		set("now_num", nowNum);
		return (M)this;
	}
	
	public java.lang.Integer getNowNum() {
		return getInt("now_num");
	}

	public M setWarnNum(java.lang.Integer warnNum) {
		set("warn_num", warnNum);
		return (M)this;
	}
	
	public java.lang.Integer getWarnNum() {
		return getInt("warn_num");
	}

	public M setWarnText(java.lang.String warnText) {
		set("warn_text", warnText);
		return (M)this;
	}
	
	public java.lang.String getWarnText() {
		return getStr("warn_text");
	}

}