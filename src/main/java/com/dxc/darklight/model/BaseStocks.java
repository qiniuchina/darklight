package com.dxc.darklight.model;

import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;


/**
 * 行业信息
 * 
 */
@Table("base_stocks")
public class BaseStocks{
	@Column("industry_cd")
	private int industryCd;
	@Column("stock_code")
	private String stockCode;
	public int getIndustryCd() {
		return industryCd;
	}
	public void setIndustryCd(int industryCd) {
		this.industryCd = industryCd;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public String getStockWin() {
		return stockWin;
	}
	public void setStockWin(String stockWin) {
		this.stockWin = stockWin;
	}
	public String getTotalStock() {
		return totalStock;
	}
	public void setTotalStock(String totalStock) {
		this.totalStock = totalStock;
	}
	public String getPassStock() {
		return passStock;
	}
	public void setPassStock(String passStock) {
		this.passStock = passStock;
	}
	public Timestamp getCreateDtm() {
		return createDtm;
	}
	public void setCreateDtm(Timestamp createDtm) {
		this.createDtm = createDtm;
	}
	@Column("stock_name")
	private String stockName;
	@Column("stock_win")
	private String stockWin;
	@Column("total_stock")
	private String totalStock;
	@Column("pass_stock")
	private String passStock;
	@Column("create_dtm")
	private Timestamp createDtm;
	@Column("upd_dtm")
	private Timestamp updDtm;
	public Timestamp getUpdDtm() {
		return updDtm;
	}
	public void setUpdDtm(Timestamp updDtm) {
		this.updDtm = updDtm;
	}
	
	public void print()
	{
		System.out.println("行业代码:"+industryCd+"---股票代码:"+stockCode+"---股票名称:"+stockName+"---换手率:"+stockWin+"---总股本:"+totalStock+"---流通股本:"+passStock);
	}
	
	@Column("short_name")
	private String shortName;
	
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	@Column("key_words")
	private String keyWords;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "行业代码:"+industryCd+"---股票代码:"+stockCode+"---股票名称:"+stockName+"---换手率:"+stockWin+"---总股本:"+totalStock+"---流通股本:"+passStock;
	}
}