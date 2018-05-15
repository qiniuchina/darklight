package com.dxc.darklight.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.springframework.stereotype.Repository;

import com.dxc.darklight.model.IndividualStockNews;
import com.dxc.darklight.util.CommonUtil;

@Repository
public class IndividualStockNewsDao extends BaseDao {

	public void insertIndividualStockNews(IndividualStockNews stockNews) {
		stockNews.setId(CommonUtil.getGenerateId());
		dao.insert(stockNews);
	}

	public IndividualStockNews getNewsFowNewestDate(String stockCd) {
		Sql sql = Sqls.queryEntity(
				"SELECT * FROM individual_stock_news WHERE stock_code=@stockCd AND pub_date=(SELECT MAX(pub_date) FROM individual_stock_news WHERE stock_code=@stockCd)");
		sql.setEntity(dao.getEntity(Record.class));
		sql.params().set("stockCd", stockCd);
		dao.execute(sql);
		List<Record> ls = sql.getList(Record.class);
		IndividualStockNews stockNews = null;
		for (Record re : ls) {
			stockNews = re.toEntity(dao.getEntity(IndividualStockNews.class));
		}
		return stockNews;
	}

	public IndividualStockNews getNewsForSingleUrl(String newsLink) {
		List<IndividualStockNews> stockNews = dao.query(IndividualStockNews.class,
				Cnd.where("news_link", "=", newsLink));
		IndividualStockNews sn = null;
		if (stockNews != null && stockNews.size() > 0) {
			sn = stockNews.get(0);
		}
		return sn;
	}

	/**
	 * 获取最新的新闻列表
	 * 
	 * @param stockCode
	 * @param String
	 * @return List<StockNews>
	 */
	public List<IndividualStockNews> listIndiStockNews(String stockCode) {
		Sql sql = Sqls.queryEntity(
				"SELECT * FROM individual_stock_news WHERE "
				+ "stock_code=@stockCode ORDER BY pub_date DESC LIMIT 10");

		List<IndividualStockNews> newsList = new ArrayList<IndividualStockNews>();
		sql.setEntity(dao.getEntity(Record.class));
		sql.params().set("stockCode", stockCode);
		dao.execute(sql);
		List<Record> ls = sql.getList(Record.class);
		for (Record re : ls) {
			IndividualStockNews news = re.toEntity(dao.getEntity(IndividualStockNews.class));
			newsList.add(news);
		}
		return newsList;
	}

	public List<IndividualStockNews> refreshIndiStockNews(long newsId, Date pubDate, String stockCode) {
		Sql sql = Sqls.queryEntity(
				"SELECT * FROM individual_stock_news WHERE "
				+ "stock_code = @stockCode AND "
				+ "pub_date > @pub_date "
				+ "ORDER BY pub_date DESC");

		List<IndividualStockNews> newsList = new ArrayList<IndividualStockNews>();
		sql.setEntity(dao.getEntity(Record.class));
		sql.params().set("stockCode", stockCode);
		sql.params().set("pub_date", pubDate);
		sql.params().set("id", newsId);
		dao.execute(sql);
		List<Record> ls = sql.getList(Record.class);
		for (Record re : ls) {
			IndividualStockNews news = re.toEntity(dao.getEntity(IndividualStockNews.class));
			newsList.add(news);
		}
		return newsList;
	}

	public List<IndividualStockNews> stockNewsHistoryList(long newsId, Date pubDate, String stockCode) {
		Sql sql = Sqls.queryEntity(
				"SELECT * FROM individual_stock_news WHERE "
				+ "stock_code = @stockCode AND "
				+ "pub_date < @pub_date "
				+ "ORDER BY pub_date DESC LIMIT 5");

		List<IndividualStockNews> newsList = new ArrayList<IndividualStockNews>();
		sql.setEntity(dao.getEntity(Record.class));
		sql.params().set("stockCode", stockCode);
		sql.params().set("pub_date", pubDate);
		sql.params().set("id", newsId);
		dao.execute(sql);
		List<Record> ls = sql.getList(Record.class);
		for (Record re : ls) {
			IndividualStockNews news = re.toEntity(dao.getEntity(IndividualStockNews.class));
			newsList.add(news);
		}
		return newsList;
	}

	public List<String> getAllStockCodes() {
		Sql sql=Sqls.create("SELECT DISTINCT(stock_code) FROM user_stock where is_deleted = 0 UNION SELECT DISTINCT(stock_code) FROM black_stocks");
		sql.setCallback(new SqlCallback() {
			
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list=new LinkedList<>();
				while (rs.next())
	                list.add(rs.getString("stock_code"));
	            return list;
			}
		});
		dao.execute(sql);
	    return sql.getList(String.class);
	}

}