package com.dxc.darklight.service;

import java.util.List;

import com.dxc.darklight.model.StockModelMount;
import com.dxc.darklight.model.StocksInfoEntity;

public interface DealMountModelService {

	/**
	 * 计算股票的交易量权值
	 */
	/*没有计算结果阶段*/
	public static int STOCK_ERROR_PHASE = -1;
	public static String STOCK_ERROR_PHASE_STR = "非黑天鹅股票";
	/*快速下跌阶段*/
	public static int STOCK_DROP_PHASE = 0;
	public static String STOCK_DROP_PHASE_STR = "快速下跌阶段";
	
	/*震荡阶段*/
	public static int STOCK_SHOCK_PHASE = 1;
	public static String STOCK_SHOCK_PHASE_STR = "震荡阶段";
	
	/*平稳阶段*/
	public static int STOCK_STABLE_PHASE = 2;
	public static String STOCK_STABLE_PHASE_STR = "平稳阶段";
	
	/*上涨阶段*/
	public static int STOCK_RISE_PHASE = 3;
	public static String STOCK_RISE_PHASE_STR = "上涨阶段";
	
	/*结束阶段*/
	public static int STOCK_END_PHASE = 4;
	public static String STOCK_END_PHASE_STR = "结束阶段";
	
	//web
	List<StockModelMount> getStockModelMountList(String userId);
	StockModelMount getStockModelResult(String stockCode);
	public int processModel(List<StocksInfoEntity> dealInfo, String stockCd) throws Exception;
}
