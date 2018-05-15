package com.dxc.darklight.model;

import java.io.Serializable;
import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("black_stocks")
public class BlackStock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column("id")
	private String id;
	/** 股票代码 */
	@Column("stock_code")
	private String stockCode;
	/** 追踪开始时间 */
	@Column("start_date")
	private Date startDate;
	/** 追踪结束时间 */
	@Column("end_date")
	private Date endDate;
	/** 是否已经洗白了,0:balck,1:white */
	@Column("removed")
	private int removed;
	/** 最后模型收益 */
	@Column("yield_rate")
	private double yieldRate;
	/** 创建时间 */
	@Column("create_dt")
	private Date createDt;
	/** 更新时间 */
	@Column("update_dt")
	private Date updateDt;

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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getRemoved() {
		return removed;
	}

	public void setRemoved(int removed) {
		this.removed = removed;
	}

	public double getYieldRate() {
		return yieldRate;
	}

	public void setYieldRate(double yieldRate) {
		this.yieldRate = yieldRate;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}
}
