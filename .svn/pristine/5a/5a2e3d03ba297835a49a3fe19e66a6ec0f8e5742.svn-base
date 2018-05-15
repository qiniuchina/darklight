package com.dxc.darklight.dao;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.springframework.stereotype.Repository;

import com.dxc.darklight.model.BlackStock;
import com.dxc.darklight.util.CommonUtil;

@Repository
public class BlackStockDao extends BaseDao {


	public String insertBlackStock(BlackStock blackStock) {
		blackStock.setId(CommonUtil.getGenerateId());
		dao.insert(blackStock);
		return "success";
	}

	public List<BlackStock> getNewsByStockCd(String stockCd) {
		List<BlackStock> blackStocks = dao.query(BlackStock.class, Cnd.where("stock_code", "=", stockCd));
		return blackStocks;
	}
	
	public List<BlackStock> getAllBlackStocks()
	{
		return dao.query(BlackStock.class, null);
	}

	/**
	 * @Description: 根据stock code更新end_data和标志字段removed
	 * @param stockCode
	 */
	public void updateFlag(String stockCode) {
		Sql sql = Sqls.create("UPDATE black_stocks SET end_date=NOW(),removed=1 WHERE stock_code=@stockCode AND removed!=1");
		sql.params().set("stockCode", stockCode);
		dao.execute(sql);
	}
}
