package com.dxc.darklight.service;

import java.sql.Date;
import java.util.List;

import com.dxc.darklight.model.StockCurrentPrice;
import com.dxc.darklight.model.UserStock;

public interface UserStockService {

	public boolean saveFavoriteStock(String userId, String stockCode);
	public void deleteFavoriteStock(String stockCode, String userId);
	public List<String> getAllUserStockCodes();
	public List<String> getAllUsers();
	public void saveUserStockCodes(StockCurrentPrice sc);
	public void deletePreviousData(int hours);
	public List<StockCurrentPrice> getStockPricesByNumber(String stockCd, int minuts);
	public StockCurrentPrice getExistedStockPrice(String stockCd, String usrId);
	public List<UserStock> getUserIdByStockCd(String stockCd);
	public boolean isHoliday(Date date);
	public int checkStock(String userId, String stockCode);//查询用户是否已关注的股票
	public List<UserStock> getStockCdByUsrId(String usrId);
}
