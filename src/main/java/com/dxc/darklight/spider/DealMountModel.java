package com.dxc.darklight.spider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.dxc.darklight.model.BlackStock;
import com.dxc.darklight.model.StocksInfoEntity;
import com.dxc.darklight.service.BlackStockService;
import com.dxc.darklight.service.DealMountModelService;
import com.dxc.darklight.service.ServiceFactory;
import com.dxc.darklight.util.CommonUtil;

public class DealMountModel {
	
	public static void work() {

		//排除周末两天的批处理
		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
		{
			System.out.println("此时间段不是交易时间!");
			return;
		}
		
		DealMountModelService dealMountModelService = ServiceFactory.createDealMountModelService();
		BlackStockService blackStockService = ServiceFactory.createBlackStockService();
		List<BlackStock> blackStocks = blackStockService.getAllBlackStocks();
		if(blackStocks==null || blackStocks.size()<=0)
		{
			return;
		}
		String begin = null, end = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		end = sf.format(cal.getTime());
		cal.add(Calendar.DATE, -200);
		begin = sf.format(cal.getTime());
		for(int i=0; i<blackStocks.size(); i++)
		{
			BlackStock sn = blackStocks.get(i);
			String stockCd = sn.getStockCode();
			if(stockCd!=null)
			{
				try
				{
					List<StocksInfoEntity> dealInfo = CommonUtil.getStockDealInfo(stockCd, begin, end, false);
					//计算交易量的模型
					int phase = dealMountModelService.processModel(dealInfo, stockCd);
					System.out.println("phase = "+phase);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws Exception
	{
//		work();
		DealMountModelService dealMountModelService = ServiceFactory.createDealMountModelService();
		System.out.print("111111111111111111");
		BlackStockService blackStockService = ServiceFactory.createBlackStockService();
		System.out.print("1111111111222222222222222");
		String stockCd="000063";
		try
		{
			List<StocksInfoEntity> dealInfo = CommonUtil.getStockDealInfo(stockCd, "20170101", "20170517", false);
			//计算交易量的模型
			int phase = dealMountModelService.processModel(dealInfo, stockCd);
			System.out.println("phase = "+phase);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
