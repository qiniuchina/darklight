package com.dxc.darklight.service;

import java.util.List;

import com.dxc.darklight.model.BlackStock;

public interface BlackStockService {

	public String insertBlackStock(BlackStock blackStock);

	public boolean isValidNews(String id);

	public List<BlackStock> getAllBlackStocks();

	// 更新blackstock的end_date和removed标志
	public void updateFlag(String stockCode);
	
	//根据股票代码从stockBase 中取得名称
	public String getStockName(String stockCode);
}
