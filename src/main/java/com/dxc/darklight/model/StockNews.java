package com.dxc.darklight.model;

import java.io.Serializable;
import java.util.Date;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;

/**
 * 
 * @author zhliu
 *
 */
@Table("stock_news")
@PK( {"id"} )
public class StockNews implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 新闻对象ID */
	@Column("id")
	private String id;
	/** 采集到的数据标题 */
	@Column("title")
	private String title;
	/** 采集到的新闻链接地址 */
	@Column("news_link")
	private String newsLink;
	/** 发布日期 */
	@Column("pub_date")
	private Date pubDate;
	/** 股票代码 */
	@Column("stock_code")
	private String stockCode;
	/** 点击量 */
	@Column("top_num")
	private Integer topNum;
	/** 涨跌幅 */
	@Column("change_rate")
	private Integer changeRate;
	/** 媒体来源  */
	@Column("media")
	private String media;
	/** 新闻来源-- 0为公司新闻; 1为Top News */
	@Column("news_source")
	private Integer newsSource;
	/** 利空标识--- 0 非利空;1为利空 */
	@Column("bear_flag")
	private Integer bearFlag;
	/** 抓取时间 */
	@Column("create_dt")
	private Date createDate;
	/** 新闻标识--- 0 未处理;1 已处理;2 黑天鹅;3 过期新闻 */
	@Column("status_flag")
	private Integer statusFlag;
	/** 更新时间 */
	@Column("update_dt")
	private Date updateDate;
	
	/** 股票名称 */
	@Column("stock_name")
	private String stockName;
	
	public StockNews() {
		super();
	}

	public StockNews(String id, String title, String newsLink, Date pubDate,
			String stockCode, Integer topNum, Integer changeRate, String media,
			Integer newsSource, Integer bearFlag, Date createDate,
			Integer statusFlag, Date updateDate,String stockName) {
		this.id = id;
		this.title = title;
		this.newsLink = newsLink;
		this.pubDate = pubDate;
		this.stockCode = stockCode;
		this.topNum = topNum;
		this.changeRate = changeRate;
		this.media = media;
		this.newsSource = newsSource;
		this.bearFlag = bearFlag;
		this.createDate = createDate;
		this.statusFlag = statusFlag;
		this.updateDate = updateDate;
		this.stockName=stockName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNewsLink() {
		return newsLink;
	}

	public void setNewsLink(String newsLink) {
		this.newsLink = newsLink;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public Integer getTopNum() {
		return topNum;
	}

	public void setTopNum(Integer topNum) {
		this.topNum = topNum;
	}

	public Integer getChangeRate() {
		return changeRate;
	}

	public void setChangeRate(Integer changeRate) {
		this.changeRate = changeRate;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public Integer getNewsSource() {
		return newsSource;
	}

	public void setNewsSource(Integer newsSource) {
		this.newsSource = newsSource;
	}

	public Integer getBearFlag() {
		return bearFlag;
	}

	public void setBearFlag(Integer bearFlag) {
		this.bearFlag = bearFlag;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Integer statusFlag) {
		this.statusFlag = statusFlag;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
}
