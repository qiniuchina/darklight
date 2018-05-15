package com.dxc.darklight.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dxc.darklight.dao.BaseStocksDao;
import com.dxc.darklight.dao.BlackStockDao;
import com.dxc.darklight.model.BaseStocks;
import com.dxc.darklight.model.BlackStock;
import com.dxc.darklight.model.StockNews;
import com.dxc.darklight.model.StocksInfoEntity;
import com.dxc.darklight.model.UserStock;
import com.dxc.darklight.model.WeiXinTemplateModel;
import com.dxc.darklight.service.BaseService;
import com.dxc.darklight.service.BlackStockService;
import com.dxc.darklight.service.ServiceFactory;
import com.dxc.darklight.service.StockInfoService;
import com.dxc.darklight.service.StockNewsService;
import com.dxc.darklight.service.UserService;
import com.dxc.darklight.service.UserStockService;
import com.dxc.darklight.util.CommonUtil;
import com.dxc.darklight.util.DateUtils;
import com.dxc.darklight.weixin.App;
import com.dxc.darklight.weixin.WeiXinUtil;

@Service
public class BlackStockServiceImpl extends BaseService implements BlackStockService {
	private static final Logger log = LogManager.getLogger(BlackStockServiceImpl.class);

	@Autowired
	private BlackStockDao blackStockDao;
	
	@Autowired
	private BaseStocksDao baseStocksDao;

	public static void work() {
		StockNewsService topNewsStockService = ServiceFactory.createStockNewsService();
		BlackStockService blackStockService = ServiceFactory.createBlackStockService();
		UserStockService userStockService=ServiceFactory.createUserStockService();
		
		//封装模板消息
		List<WeiXinTemplateModel> weixinData = new ArrayList<WeiXinTemplateModel>();
		
		
		List<StockNews> topNewsList = topNewsStockService.getUnprocessStockNews();
		if (topNewsList == null || topNewsList.size() <= 0) {
			return;
		}
		StockNews topNewsStock = null;
		BlackStock blackStock = null;
		// 处理每一条新闻
		for (int i = 0; i < topNewsList.size(); i++) {
			topNewsStock = topNewsList.get(i);
			String newsId = topNewsStock.getId();
			log.info("deal with " + newsId);
			String stockCode = topNewsStock.getStockCode();
			Date endDate = topNewsStock.getCreateDate();
			int flag = 0;// stockNews是否处理标志位
			// 利空标志位判断,0为非利空，1为利空
			double rate = changeRate(stockCode, endDate);
			if (topNewsStock.getBearFlag() == 1) {
				// 判断black_stock中是否已经存在记录并且有效,
				// 返回true,则为不存在或已存在但无效;返回false,已存在并且有效
				Boolean existedStock = blackStockService.isValidNews(stockCode);
				if (existedStock) {
					// 判断下跌趋势是否满足要求,<-20%=true
					boolean fallFlag = isFall(rate);
					if (fallFlag) {
						// true说明有下跌趋势
						flag = 2;
						// 更新top_new的状态为2，代表已处理并且是黑天鹅股票
						topNewsStockService.updateStockNewsById(rate,newsId, flag);
						// 池中已存在但无效，并且有下跌趋势 或者池中不存在，并且有下跌趋势,则加入池中
						blackStock = new BlackStock();
						blackStock.setStockCode(stockCode);
						blackStock.setStartDate(new Date());
						blackStock.setCreateDt(new Date());
						blackStockService.insertBlackStock(blackStock);
						
						List<String> userStockNews = userStockService.getAllUsers();
						String stockName=blackStockService.getStockName(stockCode);
						for (String userId : userStockNews) {
							WeiXinTemplateModel tModel =blackStockMsg(userId,stockCode,stockName);
							weixinData.add(tModel);
						}
						
					} else {
						// 无下跌趋势，只做新闻标记处理
						flag = 1;
						topNewsStockService.updateStockNewsById(rate,newsId, flag);
					}
				} else {
					// 已存在并且有效,只需要将该新闻标志位黑天鹅相关状态;
					flag = 2;
					topNewsStockService.updateStockNewsById(rate,newsId, flag);
				}
			} else {
				// 非利空股票新闻处理,直接做已处理标识
				flag = 1;
				topNewsStockService.updateStockNewsById(rate,newsId, flag);
			}
		}
		//发送模板消息
		if(weixinData.size()>0)
		{
			WeiXinUtil.sendWeixinMsg(weixinData);
		}
	}

	private static WeiXinTemplateModel blackStockMsg(String userId, String stockCode,String stockName) {
		WeiXinTemplateModel tempModel=new WeiXinTemplateModel();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar calTime = Calendar.getInstance();
		String wtime = sf.format(calTime.getTime());
		tempModel.setToUser(userId);
		tempModel.setFirst("惊现黑天鹅");
		tempModel.setTime(wtime);
		tempModel.setColor("#ff0000");
		tempModel.setContent("股票("+stockName+"),成为新一轮的黑天鹅，恭喜");
		tempModel.setRemark("点击查看详情信息");
		tempModel.setUrl(App.STOCK_NEWS_URL_PRE+stockCode+"&userId="+userId);
		return tempModel;
	}

	public static Boolean isFall(double rate) {
		Double MaxDownCount = -20d;
		if (rate <= MaxDownCount) {
			log.debug("true:" + rate + "<" + MaxDownCount);
			return true;
		} else {
			log.debug("false:" + rate + ">" + MaxDownCount);
			return false;
		}
	}

	public static double stockPriceWave(List<StocksInfoEntity> dealInfo) {
		Double downCount = 0d;
		Double wave = 0d;
		int count = 0;
		for (StocksInfoEntity stock : dealInfo) {
			count++;
			downCount = downCount + stock.getUpDownMoneyPercent();
			if (downCount < wave) {
				log.debug("wave1==" + wave + "------" + count);
				wave = downCount;
			}
		}
		log.debug("wave2==" + wave + "------" + count);
		return wave;
	}

	private static double changeRate(String stockCode, Date endDate) {

		String end = DateUtils.getYYYYMMdd(endDate);
		String begin = DateUtils.getYYYYMMdd(DateUtils.getBeginDateByParam(endDate, 100));// 得到结束前的前N天日期
		double rate = 0;
		try {
			List<StocksInfoEntity> dealInfo = CommonUtil.getStockDealInfo(stockCode, begin, end, false);
			rate = stockPriceWave(dealInfo);// 下跌20%与否。。
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rate;
	}

	@Override
	public String insertBlackStock(BlackStock blackStock) {
		return blackStockDao.insertBlackStock(blackStock);
	}

	// 返回true（不存在或者已存在但是无效），则加入池中;返回false，已存在并且有效
	@Override
	public boolean isValidNews(String stockCd) {
		List<BlackStock> blackStocks = blackStockDao.getNewsByStockCd(stockCd);
		// 不为空，则池中已存在该股票的历史黑天鹅数据，则需要根据标志位removed判断其中是否有目前正处于黑天鹅声明周期的数据，
		if (blackStocks != null && blackStocks.size() > 0) {
			for (BlackStock blackStock : blackStocks) {
				int removed = blackStock.getRemoved();
				if (removed == 1) {
					log.info(true + ":池中已存在" + stockCd + "并且无效");
					return true;
				}
			}
			log.info(false + "：池中已存在" + stockCd + "并且有效");
			return false;
		} else {
			//
			log.info(true + "池中不存在 " + stockCd);
			return true;
		}
	}

	@Override
	public List<BlackStock> getAllBlackStocks() {
		return blackStockDao.getAllBlackStocks();
	}

	/**
	 * @Description: 根据stock code更新end_data和标志字段removed
	 * @param stockCode
	 */
	@Override
	public void updateFlag(String stockCode) {
		blackStockDao.updateFlag(stockCode);
	}

	public static void main(String[] args) {
		 BlackStockServiceImpl.work();

		// System.out.println(BlackStockServiceImpl.isFall("300104", new
		// Date()));
		// System.out.println(BlackStockServiceImpl.isFall("300104", new
		// Date()));

		// System.out.println(BlackStockServiceImpl.isFall("300104", new
		// Date()));

	}

	@Override
	public String getStockName(String stockCode) {
		BaseStocks baseStocks=baseStocksDao.getIndustryStocksByCode(stockCode);
		return baseStocks.getStockName();
	}

}