package com.dxc.darklight.service.impl;


import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dxc.darklight.dao.IndividualStockNewsDao;
import com.dxc.darklight.model.IndividualStockNews;
import com.dxc.darklight.service.BaseService;
import com.dxc.darklight.service.IndividualStockNewsService;

@Service
public class IndividualStockNewsServiceImpl extends BaseService implements IndividualStockNewsService {
	
	private static final Logger log=LogManager.getLogger(IndividualStockNewsServiceImpl.class);
	
	@Autowired
	private IndividualStockNewsDao stockNewsDao;
	
	/**
	 * 插入个股新闻
	 * 
	 */
	@Override
	public void insertIndividualStockNews(IndividualStockNews stockNews) {
		stockNewsDao.insertIndividualStockNews(stockNews);
	}

	/**
	 * 获取数据库中某支股票新闻的最新发布时间(pub_date)
	 */
	@Override
	public Date getNewestDate(String stockCd) {
		IndividualStockNews news=new IndividualStockNews();
		news=stockNewsDao.getNewsFowNewestDate(stockCd);
		log.info("news from database: "+news);
		Date date=null;
		if (news!=null) {
			date=news.getPubDate();
		}
		return date;
	}

	/**
	 * 判断数据库中某支股票新闻的URL是否唯一
	 * 
	 */
	@Override
	public boolean isSingleUrl(String newsLink) {
		IndividualStockNews news=new IndividualStockNews();
		news=stockNewsDao.getNewsForSingleUrl(newsLink);
		if (news==null) {
			return true;
		}
		return false;
	}

	@Override
	public List<IndividualStockNews> listIndiStockNews(String stockCode) {
		return stockNewsDao.listIndiStockNews(stockCode);
	}

	@Override
	public List<IndividualStockNews> refreshIndiStockNews(long newsId, Date pubDate, String stockCode) {
		return stockNewsDao.refreshIndiStockNews(newsId,pubDate,stockCode);
	}

	@Override
	public List<IndividualStockNews> stockNewsHistoryList(long newsId, Date pubDate, String stockCode) {
		return stockNewsDao.stockNewsHistoryList(newsId,pubDate,stockCode);
	}

	@Override
	public List<String> getAllStockCodes() {
		return stockNewsDao.getAllStockCodes();
	}


}