package com.dxc.darklight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dxc.darklight.dao.BlackSwanStocksDao;
import com.dxc.darklight.model.BlackSwanStocks;
import com.dxc.darklight.service.BlackSwanStocksService;
@Service
public class BlackSwanStocksServiceImpl implements BlackSwanStocksService {
	@Autowired
	BlackSwanStocksDao blackSwanStockDao;
	//web	
	@Override
	public List<BlackSwanStocks> getAllBlackSwanStocks() {
		return blackSwanStockDao.getAllBlackSwanStocks();
	}
	

}
