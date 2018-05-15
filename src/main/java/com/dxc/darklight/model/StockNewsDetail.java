package com.dxc.darklight.model;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("stock_news")
public class StockNewsDetail {
	
	@Column("id")
	private String id;
	/**  股票代码 */
	@Column("stock_code")
	private String stockCode;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	
	

}
