package com.dxc.darklight.service;

import java.util.Date;
import java.util.List;

import com.dxc.darklight.model.StockComment;

public interface StockCommentService {
	// 插入新的大盘评论
	public void insertStockComment(StockComment stockComment);

	// 获取最新的大盘评论
	public List<StockComment> listStockComments();
	
	// 获取更新的大盘评论
	public List<StockComment> refreshStockComments(Date pubDate);
	
	// 获取历史的大盘评论
	public List<StockComment> listStockCommentsHistory(Date pubDate);
	
	//根据URL查询StockComment
	public StockComment queryStockCommentByLink(String blogLink);
	
	//根据ID修改comment
	public void updateCommentById(String id, String correct);
	
	//新增大盘审核结果
	public Integer insertUserComments(String userId, String CommentsId, Integer reviewResult);
}
