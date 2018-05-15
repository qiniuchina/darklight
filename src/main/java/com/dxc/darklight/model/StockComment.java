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
@Table("stock_comments")
@PK({ "id" })
public class StockComment implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 新闻对象ID */
	@Column("id")
	private String id;

	/** author */
	@Column("author")
	private String author;

	/** 采集到的数据标题 */
	@Column("title")
	private String title;

	/** 采集到的新闻链接地址 */
	@Column("news_link")
	private String newsLink;

	/** 预判是否正确 */
	@Column("correct")
	private String correct;

	/** 简明概要的评论 */
	@Column("key_comments")
	private String keyComments;

	/** 发布日期 */
	@Column("pub_date")
	private Date pubDate;

	/** 获取日期 */
	@Column("fetch_date")
	private Date fetchDate;
	
	/** 预测准确率 */
	private Integer accuracy;
	
	public StockComment() {
		super();
	}

	public StockComment(String id, String author, String title,
			String newsLink, String correct, String keyComments, Date pubDate,
			Date fetchDate, Integer accuracy) {
		this.id = id;
		this.author = author;
		this.title = title;
		this.newsLink = newsLink;
		this.correct = correct;
		this.keyComments = keyComments;
		this.pubDate = pubDate;
		this.fetchDate = fetchDate;
		this.accuracy = accuracy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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

	public String getCorrect() {
		return correct;
	}

	public void setCorrect(String correct) {
		this.correct = correct;
	}

	public String getKeyComments() {
		return keyComments;
	}

	public void setKeyComments(String keyComments) {
		this.keyComments = keyComments;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public Date getFetchDate() {
		return fetchDate;
	}

	public void setFetchDate(Date fetchDate) {
		this.fetchDate = fetchDate;
	}

	public Integer getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Integer accuracy) {
		this.accuracy = accuracy;
	}
	
}
