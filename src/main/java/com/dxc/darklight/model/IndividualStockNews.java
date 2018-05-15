package com.dxc.darklight.model;

import java.io.Serializable;
import java.util.Date;

import javax.swing.plaf.basic.BasicToolBarUI.DockingListener;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import com.dxc.darklight.datasource.solr.Indexable;

/**
 * 个股新闻
 * 
 * @author shexia
 * 
 */
@Table("individual_stock_news")
public class IndividualStockNews implements Indexable, Serializable {

	/**
	 * 
	 */
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
	/** 抓取时间 */
	@Column("fetch_date")
	private Date fetchDate;

	@Override
	public String toString() {
		return "IndividualStockNews [数据库主键=" + id + ", 新闻标题=" + title + ", 新闻链接=" + newsLink + ", 发布日期=" + pubDate
				+ ", 股票代码=" + stockCode + ", 抓取时间=" + fetchDate + "]";
	}

	@Override
	public SolrInputDocument toSolrInputDocument() {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", getId());
		doc.addField("title", getTitle());
		doc.addField("newsLink", getNewsLink());
		doc.addField("pubDate", getPubDate());
		doc.addField("stockCode", getStockCode());
		doc.addField("fetchDate", getFetchDate());
		return doc;
	}

	@Override
	public void readFromSolrDocument(SolrDocument doc) {
		setId((String) doc.getFieldValue("id"));
		setTitle((String) doc.getFieldValue("title"));
		setNewsLink((String) doc.getFieldValue("newsLink"));
		setPubDate((Date) doc.getFieldValue("pubDate"));
		setStockCode((String) doc.getFieldValue("stockCode"));
		setFetchDate((Date) doc.getFieldValue("fetchDate"));
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

	public Date getFetchDate() {
		return fetchDate;
	}

	public void setFetchDate(Date fetchDate) {
		this.fetchDate = fetchDate;
	}

}
