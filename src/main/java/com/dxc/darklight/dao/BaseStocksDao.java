package com.dxc.darklight.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.springframework.stereotype.Repository;

import com.dxc.darklight.model.BaseStocks;
import com.dxc.darklight.model.Industry;

@Repository
public class BaseStocksDao extends BaseDao {

	private static final Logger log = LogManager.getLogger(BaseStocksDao.class);
	/**
	 * 插入mysql
	 * @param industryInfo
	 * @return
	 * @throws Exception
	 */
	public void insertIndustryInfo(Industry industryInfo) throws Exception{		
		dao.insert(industryInfo);
	}
	
	public void insertIndustryStocks(BaseStocks industryStocks) throws Exception{		
		dao.insert(industryStocks);
	}
	
	public Industry getInsustryInfoByName(String name)
	{
		return dao.fetch(Industry.class, Cnd.wrap("industry_nm = '"+name+"'"));
	}
	
	public void updateIndustryInfo(Industry industryInfo) throws Exception{		
		dao.update(Industry.class, Chain.make("industry_nm", industryInfo.getIndustryNm()).add("upd_dtm", industryInfo.getUpdDtm()), 
				Cnd.wrap("industry_cd = "+industryInfo.getIndustryCd()));
	}
	
	public BaseStocks getIndustryStocksByCode(String code)
	{
		return dao.fetch(BaseStocks.class, Cnd.wrap("stock_code = '"+code+"'"));
	}
	
	public void updateIndustryStocks(BaseStocks industryStock) throws Exception{		
		dao.update(BaseStocks.class, Chain.make("stock_name", industryStock.getStockName()).add("upd_dtm", industryStock.getUpdDtm())
				.add("total_stock", industryStock.getTotalStock()).add("stock_win", industryStock.getStockWin()).add("pass_stock", industryStock.getPassStock()), 
				Cnd.wrap("stock_code = '"+industryStock.getStockCode()+"'"));
	}
	
	/**
	 * 获取所有股票基础信息
	 * @param
	 * @return List<IndustryStocks>
	 */
	public List<BaseStocks> getAllIndustryStocks(){
		return dao.query(BaseStocks.class, null);
	}
}