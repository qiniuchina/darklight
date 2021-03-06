package com.dxc.darklight.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.springframework.stereotype.Repository;

import com.dxc.darklight.model.StockModelMount;

@Repository
public class StockModelMountDao extends BaseDao {

	private static final Logger log = LogManager.getLogger(StockModelMountDao.class);
	
	public void saveAndUpateMount(StockModelMount stockModelMount){
		dao.delete(stockModelMount);
		dao.insert(stockModelMount);
		/*int count = dao.count(StockModelMount.class, Cnd.wrap("stock_code='"+stockModelMount.getStockCode()+"' AND stock_date='"+stockModelMount.getStockDate()+"'"));
		if(count==0)
		{
			dao.insert(stockModelMount);
		}*/
	}
	
	/**
	 * 当有USER_ID的时候，只访问该用户关注的,不包括大盘的股票代码
	 * @param id
	 * @param date
	 * @return
	 */
	public List<StockModelMount> getStockModelMountList(String userId){
		Sql sql = Sqls.queryEntity("select * from(select tstock.*,sm.stock_code scode,sm.stock_date,sm.avg_mount,sm.avg_weight,sm.avg_price,"
				+ "ifnull(sm.flag_phase,-1) flag_phase from "
				+ "(select DISTINCT si.stock_code, si.stock_name,us.user_id from base_stocks si, user_stock us where user_id=@userId"
				+ " and si.stock_code =us.stock_code and us.is_deleted = 0 and us.stock_code!='1A0001' and "
				+ " us.stock_code!=399001 and us.stock_code!=399006)tstock "
				+ "left join stock_model_mount sm on tstock.stock_code=sm.stock_code where sm.end_date is null ORDER BY sm.stock_code, sm.stock_date desc) tbstock "
				+ "GROUP BY stock_code");
		sql.params().set("userId", userId);
		List<StockModelMount> stockModelMountList=new ArrayList<StockModelMount>();
		sql.setEntity(dao.getEntity(Record.class));
		dao.execute(sql);
		List<Record> ls=sql.getList(Record.class);
		for(Record re:ls){
			StockModelMount stockModelMount=re.toEntity(dao.getEntity(StockModelMount.class));
			String stockName=re.getString("stock_name");
			stockModelMount.setStockName(stockName);
			stockModelMountList.add(stockModelMount);
		}
		return stockModelMountList;
	}
	
	/**
	 * 个股的基本信息
	 * @param id
	 * @param date
	 * @return
	 */
	public StockModelMount getStockModelResult(String stockCode){
		Sql sql = Sqls.queryEntity("select * from(select tstock.*,sm.stock_code scode,sm.stock_date,sm.avg_mount,"
				+ "sm.avg_weight,sm.avg_price, ifnull(sm.flag_phase,-1) flag_phase from base_stocks tstock  left join stock_model_mount sm "
				+ "on tstock.stock_code=sm.stock_code where sm.end_date is null "
+" ORDER BY sm.stock_code, sm.stock_date desc) tbstock where @stockCode like concat('%',tbstock.stock_code) GROUP BY stock_code");
		sql.params().set("stockCode", stockCode);
		sql.setEntity(dao.getEntity(Record.class));
		dao.execute(sql);
		List<Record> ls=sql.getList(Record.class);
		StockModelMount stockModelMount=null;
		if(ls!=null&&ls.size()>0){
			Record re=ls.get(0);
			stockModelMount=re.toEntity(dao.getEntity(StockModelMount.class));
			String stockName=re.getString("stock_name");
			stockModelMount.setStockName(stockName);
		}
		return stockModelMount;
	}
	/*
	 * 结束模型计算数据
	 */
	public void updateEndDate(String stockCode){
		Sql sql = Sqls
				.create("update stock_model_mount set end_date = NOW() where stock_code = @stockCode");
		sql.params().set("stockCode", stockCode);
		dao.execute(sql);
	}
}
