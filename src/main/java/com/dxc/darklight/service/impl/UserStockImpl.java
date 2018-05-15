package com.dxc.darklight.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dxc.darklight.dao.UserStockDao;
import com.dxc.darklight.model.StockCurrentPrice;
import com.dxc.darklight.model.UserStock;
import com.dxc.darklight.service.UserStockService;

@Service
public class UserStockImpl implements UserStockService {
	
	@Autowired
	UserStockDao dao;
	@Override
	public boolean saveFavoriteStock(String userId, String stockCode) {
		// TODO Auto-generated method stub
		int i = dao.checkExistStock(userId, stockCode);
		if(i == 0) {
			dao.insertUserStock(userId, stockCode);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void deleteFavoriteStock(String stockCode, String userId) {
		// TODO Auto-generated method stub
		dao.deleteFavoriteStock( stockCode,  userId);
	}
	
	@Override
	public List<String> getAllUserStockCodes() {
		return dao.getAllUserStockCodes();
	}

	@Override
	public void saveUserStockCodes(StockCurrentPrice sc) {
		dao.saveUserStockCodes(sc);
	}

	@Override
	public void deletePreviousData(int hours) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, hours);
		String time = sf.format(cal.getTime());
		dao.deletePreviousDataForPrice(time);
	}

	@Override
	public List<StockCurrentPrice> getStockPricesByNumber(String stockCd, int minuts) {
		return dao.getStockPricesByNumber(stockCd, minuts);
	}

	@Override
	public StockCurrentPrice getExistedStockPrice(String stockCd, String usrId) {
		return dao.getExistedStockPrice(stockCd, usrId);
	}

	@Override
	public List<UserStock> getUserIdByStockCd(String stockCd) {
		return dao.getUserIdByStockCd(stockCd);
	}
	
	@Override
	public List<UserStock> getStockCdByUsrId(String usrId) {
		return dao.getStockCdByUsrId(usrId);
	}
	
	public boolean isHoliday(Date date){
		return dao.isHoliday(date);
	}

	/**
	 * @Title: checkStock
	 * @Description: 查询用户是否已关注的股票
	 * @param String
	 * @param String
	 * @return int
	 */
	public int checkStock(String userId, String stockCode) {
		return dao.checkExistStock(userId, stockCode);
	}

	@Override
	public List<String> getAllUsers() {
		return dao.getAllUsers();
	}

}
