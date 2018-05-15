package com.dxc.darklight.model;

import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;


/**
 * 行业信息
 * 
 */
@Table("stock_model_mount")
@PK( {"stockCode", "stockDate"} )
public class StockModelMount{
	@Column("stock_code")
	private String stockCode;
	@Column("stock_date")
	private String stockDate;
	@Column("avg_mount")
	private double avgMount;
	@Column("avg_price")
	private double avgPrice;
	@Column("flag_phase")
	private int flagPhase;
	
	private String pricePhase;
	private String currPrice;
	private String currUpDown;
	
	
	public String getPricePhase() {
		return pricePhase;
	}
	public void setPricePhase(String pricePhase) {
		this.pricePhase = pricePhase;
	}
	public String getCurrPrice() {
		return currPrice;
	}
	public void setCurrPrice(String currPrice) {
		this.currPrice = currPrice;
	}
	public String getCurrUpDown() {
		return currUpDown;
	}
	public void setCurrUpDown(String currUpDown) {
		this.currUpDown = currUpDown;
	}
	
	
	public int getFlagPhase() {
		return flagPhase;
	}
	public void setFlagPhase(int flagPhase) {
		this.flagPhase = flagPhase;
	}
	public double getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}
	@Column("avg_weight")
	private double avgWeight;
	@Column("create_dtm")
	private Timestamp createDtm;
	
	//新加属性，非字段
	private String stockName;
	
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public String getStockDate() {
		return stockDate;
	}
	public void setStockDate(String stockDate) {
		this.stockDate = stockDate;
	}
	public double getAvgMount() {
		return avgMount;
	}
	public void setAvgMount(double avgMount) {
		this.avgMount = avgMount;
	}
	public double getAvgWeight() {
		return avgWeight;
	}
	public void setAvgWeight(double avgWeight) {
		this.avgWeight = avgWeight;
	}
	public Timestamp getCreateDtm() {
		return createDtm;
	}
	public void setCreateDtm(Timestamp createDtm) {
		this.createDtm = createDtm;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	
}
