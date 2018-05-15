
package com.dxc.darklight.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dxc.darklight.dao.StockCommentDao;
import com.dxc.darklight.model.StockComment;
import com.dxc.darklight.service.StockCommentService;

@Service
public class StockCommentServiceImpl implements StockCommentService {
	private static final Logger log = LogManager.getLogger(StockCommentServiceImpl.class);

	@Autowired
	StockCommentDao dao;
	
	public void insertStockComment(StockComment stockComment) {
		// TODO Auto-generated method stub
		log.info("insertStockComment");
		dao.insertStockComment(stockComment);
	}

	@Override
	public List<StockComment> listStockComments() {
		// TODO Auto-generated method stub
		return dao.listStockComments();
	}

	@Override
	public StockComment queryStockCommentByLink(String blogLink) {
		return dao.queryStockCommentByLink(blogLink);
	}

	@Override
	public List<StockComment> refreshStockComments(Date pubDate) {
		return dao.refreshStockComments(pubDate);
	}

	@Override
	public List<StockComment> listStockCommentsHistory(Date pubDate) {
		return dao.listStockCommentsHistory(pubDate);
	}

	@Override
	public void updateCommentById(String id, String correct) {
		dao.updateCommentsById(id, correct);
	}

	@Override
	public Integer insertUserComments(String userId, String CommentsId, Integer reviewResult) {
		return dao.insertUserComments(userId, CommentsId, reviewResult);
	}	
}
