package com.dxc.darklight.service.impl;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dxc.darklight.dao.BaseStocksDao;
import com.dxc.darklight.model.BaseStocks;
import com.dxc.darklight.model.Industry;
import com.dxc.darklight.service.BaseService;
import com.dxc.darklight.service.StockInfoService;

@Service
public class StockInfoServiceImpl extends BaseService implements StockInfoService {

	private static final Logger log = LogManager.getLogger(StockInfoServiceImpl.class);
	@Autowired
	BaseStocksDao baseStocksDao;
	@Override
	public int insertOrUpdateIndustryInfo(Industry industryInfo)
			throws Exception {
		int rev = -1;
		Industry ii = baseStocksDao.getInsustryInfoByName(industryInfo.getIndustryNm());
		if(ii == null)
		{
			baseStocksDao.insertIndustryInfo(industryInfo);
			Industry revIi = baseStocksDao.getInsustryInfoByName(industryInfo.getIndustryNm());
			rev = revIi.getIndustryCd();
		}
		else
		{
			rev = ii.getIndustryCd();
			industryInfo.setIndustryCd(rev);
			baseStocksDao.updateIndustryInfo(industryInfo);
		}
		
		return rev;
	}

	@Override
	public void insertOrUpdateBaseStocks(BaseStocks industryStocks)
			throws Exception {
		
		BaseStocks is = baseStocksDao.getIndustryStocksByCode(industryStocks.getStockCode());
		if(is == null)
		{
			baseStocksDao.insertIndustryStocks(industryStocks);
		}
		else
		{
			baseStocksDao.updateIndustryStocks(industryStocks);
		}
	}

	/**
	 * 获取所有股票基础信息
	 * @param
	 * @return List<IndustryStocks>
	 */
	public List<BaseStocks> getAllBaseStocks() {
		return baseStocksDao.getAllIndustryStocks();
	}
	
}
