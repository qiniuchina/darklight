package com.dxc.darklight.service;

import java.util.Date;
import java.util.List;

import com.dxc.darklight.model.StockNews;

public interface StockNewsService {
	//插入一条StockNews
	public void insertStockNews(StockNews stockNews);
	//根据URL查询StockNews
	public StockNews queryStockNewsByLink(String newsLink);
	//查询所有未处理的StockNews
	public List<StockNews> getUnprocessStockNews();
	//根据ID更新StockNews
	public void updateStockNewsById(double rate,String newsId, int flag);
	//获取最新的新闻列表
	public List<StockNews> listStockNews(Integer newsSource);
	//获取更新的新闻列表
	public List<StockNews> refreshStockNews(Date pubDate, Integer newsSource);
	//获取历史的新闻列表
	public List<StockNews> listStockNewsHistory(Date pubDate, Integer newsSource);
	//标记过期新闻
	public void expireNews(String stock);
}
