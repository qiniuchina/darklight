package com.dxc.darklight.dao;

import java.util.Date;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.springframework.stereotype.Repository;

import com.dxc.darklight.model.StockComment;

@Repository
public class StockCommentDao extends BaseDao {
	
	/**
	 * @Description 新增StockNews
	 * @param StockComment
	 */
	public String insertStockComment(StockComment stockComment){
		dao.insert(stockComment);
		return "success";
	}
	
	/**
	 * @Description 通过blogUrl查找StockComment
	 * @param String
	 * @return StockComment
	 */
	public StockComment queryStockCommentByLink(String blogLink) {
		List<StockComment> stockCommentList = dao.query(StockComment.class,
				Cnd.where("news_link", "=", blogLink));
		StockComment stockComment = null;
		if (stockCommentList != null && stockCommentList.size() > 0) {
			stockComment = stockCommentList.get(0);
		}
		return stockComment;
	}
	
	/**
	 * @Description 获取最新的大盘综述
	 * @return List<StockComment>
	 */
	public List<StockComment> listStockComments(){
		List<StockComment> commentsAccuracy=getCommentsAccuracy();
		Sql sql = Sqls.queryEntity("select * from stock_comments group by id order by pub_date desc limit 10");
		sql.setEntity(dao.getEntity(StockComment.class));
		dao.execute(sql);
		List<StockComment> commentList = sql.getList(StockComment.class);
		for(StockComment accuracy:commentsAccuracy){
			for(StockComment comment: commentList){
				if(accuracy.getAuthor().equals(comment.getAuthor())){
					comment.setAccuracy(Integer.parseInt(accuracy.getCorrect()));
				}
			}
		}
		return commentList;
	}
	
	/**
	 * @Description 获取更新的大盘综述
	 * @param Date
	 * @return List<StockComment>
	 */
	public List<StockComment> refreshStockComments(Date pubDate) {
		List<StockComment> commentsAccuracy=getCommentsAccuracy();
		Sql sql = Sqls.queryEntity("select * from stock_comments where date_format(pub_date,'%Y-%m-%d %H:%i') >= @pub_date group by id order by pub_date desc limit 10");
		sql.setEntity(dao.getEntity(StockComment.class));
		sql.params().set("pub_date", pubDate);
		dao.execute(sql);
		List<StockComment> commentList = sql.getList(StockComment.class);
		for(StockComment accuracy:commentsAccuracy){
			for(StockComment comment: commentList){
				if(accuracy.getAuthor().equals(comment.getAuthor())){
					comment.setAccuracy(Integer.parseInt(accuracy.getCorrect()));
				}
			}
		}
		return commentList;
	}

	/**
	 * @Description 获取历史的大盘综述列表
	 * @param Date
	 * @return List<StockComment>
	 */
	public List<StockComment> listStockCommentsHistory(Date pubDate) {
		List<StockComment> commentsAccuracy=getCommentsAccuracy();
		Sql sql = Sqls.queryEntity("select * from stock_comments where date_format(pub_date,'%Y-%m-%d %H:%i') <= @pub_date group by id order by pub_date desc limit 10");
		sql.setEntity(dao.getEntity(StockComment.class));
		sql.params().set("pub_date", pubDate);
		dao.execute(sql);
		List<StockComment> commentList = sql.getList(StockComment.class);
		for(StockComment accuracy:commentsAccuracy){
			for(StockComment comment: commentList){
				if(accuracy.getAuthor().equals(comment.getAuthor())){
					comment.setAccuracy(Integer.parseInt(accuracy.getCorrect()));
				}
			}
		}
		return commentList;
	}
	
	/**
	 * @Description 获取每个博主预测的准确率
	 * @return List<StockComment>
	 */
	public List<StockComment> getCommentsAccuracy(){
		String sqlString = "SELECT DISTINCT(T1.author) as author, ROUND(T1.correct / T2.total * 100, 0) as correct FROM"
				+ " (SELECT author, SUM(CAST(ifnull(correct,'1') AS SIGNED)) AS correct FROM stock_comments GROUP BY author) T1,"
				+ " (SELECT author,count(*) AS total FROM stock_comments GROUP BY author) T2"
				+ " WHERE T1.author = T2.author";
		Sql sql = Sqls.queryEntity(sqlString);
		sql.setEntity(dao.getEntity(StockComment.class));
		dao.execute(sql);
		return sql.getList(StockComment.class);
	}
	
	/**
	 * @Description 通过ID更新大盘综述
	 * @param String
	 * @param String
	 */
	public void updateCommentsById(String id, String correct) {
		Sql sql = Sqls.create("update stock_comments set correct=@correct where id = @id");
		sql.params().set("correct", correct);
		sql.params().set("id", id);
		dao.execute(sql);
	}
	
	/**
	 * @Description 新增大盘审核结果
	 * @param String
	 * @param String
	 * @param Integer
	 */
	public Integer insertUserComments(String userId, String CommentsId, Integer reviewResult){
		Sql sql = Sqls.create("insert into user_comments (user_id,comments_id,review_result,create_dtm,create_by) values (@userId,@CommentsId,@reviewResult,@createTime,@createBy)");
		sql.params().set("userId", userId);
		sql.params().set("CommentsId", CommentsId);
		sql.params().set("reviewResult", reviewResult);
		sql.params().set("createTime", new Date());
		sql.params().set("createBy", userId);
		dao.execute(sql);
		List<Record> list = queryAllReviewResult(CommentsId);
		if(list.size()==4){
			String correct="1";
			for(Record record:list){
				reviewResult=reviewResult+record.getInt("review_result");
			}
			if(reviewResult<3){
				correct="0";
			}else{
				correct="1";
			}
			updateCommentsById(CommentsId,correct);
		}
		return list.size();
	}
	
	/**
	 * @Description 查询某条股评的审核记录数量
	 * @param String
	 */
	public List<Record> queryAllReviewResult(String CommentsId){
		Sql sql = Sqls.create("select comments_id, review_result"
				+ " from user_comments where comments_id=@CommentsId");
		sql.params().set("CommentsId", CommentsId);
		sql.setCallback(Sqls.callback.records());
		dao.execute(sql);
		return sql.getList(Record.class);
	}
}
