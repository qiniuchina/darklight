package com.dxc.darklight.weixin.token;

import java.util.Date;

public class Token {
	
	/** token字符*/
	private String tokenStr;
	/** 获取时间*/
	private Date date;

	public String getTokenStr() {
		return tokenStr;
	}

	public void setTokenStr(String tokenStr) {
		this.tokenStr = tokenStr;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
