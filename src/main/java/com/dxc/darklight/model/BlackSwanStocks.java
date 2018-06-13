package com.dxc.darklight.model;
import java.io.Serializable;
import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("kpi_morning_star")
public class BlackSwanStocks implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column("id")
	private String id;
	/** 股票代码 */
	@Column("stock_code")
	private String stockCode;
	/** 模型生成时间 */
	@Column("stage_date_str")
	private String stageDateStr;
	/** 模型综合描述 */
	@Column("stage_comments")
	private String stageComments;
	/** 预测模型最后收益 */
	@Column("day_cut_off_yield")
	private double dayCutOffYield;
	/** 标记黑天鹅阶段是否结束 */
	@Column("stage_end")
	private String stageEnd;
	
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
	public String getStageDateStr() {
		return stageDateStr;
	}
	public void setStageDateStr(String stageDateStr) {
		this.stageDateStr = stageDateStr;
	}
	public String getStageComments() {
		return stageComments;
	}
	public void setStageComments(String stageComments) {
		this.stageComments = stageComments;
	}
	public double getDayCutOffYield() {
		return dayCutOffYield;
	}
	public void setDayCutOffYield(double dayCutOffYield) {
		this.dayCutOffYield = dayCutOffYield;
	}
	public String getStageEnd() {
		return stageEnd;
	}
	public void setStageEnd(String stageEnd) {
		this.stageEnd = stageEnd;
	}
	
}
