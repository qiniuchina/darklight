package com.dxc.darklight.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dxc.darklight.http.JsoupParse;
import com.dxc.darklight.model.BaseStocks;
import com.dxc.darklight.model.StockNews;
import com.dxc.darklight.model.UserStock;
import com.dxc.darklight.model.WeiXinTemplateModel;
import com.dxc.darklight.service.ServiceFactory;
import com.dxc.darklight.service.StockInfoService;
import com.dxc.darklight.service.UserStockService;
import com.dxc.darklight.util.BadNewsFilterUtil;
import com.dxc.darklight.util.CommonUtil;
import com.dxc.darklight.util.DateUtils;
import com.dxc.darklight.weixin.WeiXinUtil;

/**
 * @ClassName: FinanceNewsSpider
 * @Description: Finace Related News
 * @author: Wei Wei
 * @date: Apr 28, 2017 4:04:35 PM
 */
public class TopNewsSpider implements Runnable {
	private static final Logger log = LogManager
			.getLogger(TopNewsSpider.class);
	private static List<String> topNewsURLs = null;
	private List<BaseStocks> industryStocks;
	static {
		String today = DateUtils.getYYYYMMdd(new Date());
		topNewsURLs = new ArrayList<String>();
		topNewsURLs
				.add("http://top.finance.sina.com.cn/ws/GetTopDataList.php?top_type=day&top_cat=finance_0_suda&top_time="
						+ today
						+ "&top_show_num=20&top_order=DESC&js_var=all_1_data&get_new=1");
		topNewsURLs
				.add("http://top.finance.sina.com.cn/ws/GetTopDataList.php?top_type=day&top_cat=finance_news_0_suda&top_time="
						+ today
						+ "&get_new=1&top_show_num=20&top_order=DESC&js_var=all_1_data");
		topNewsURLs
				.add("http://top.finance.sina.com.cn/ws/GetTopDataList.php?top_not_url=/ustock/&top_type=day&top_cat=finance_stock_conten_suda&top_time="
						+ today
						+ "&top_show_num=20&top_order=DESC&get_new=1&js_var=stock_1_data");
		topNewsURLs
				.add("http://top.finance.sina.com.cn/ws/GetTopDataList.php?top_type=day&top_cat=finance_money_suda&top_time="
						+ today
						+ "&top_show_num=20&top_order=DESC&get_new=1&js_var=money_1_data");
	}

	@Override
	public void run() {
		log.info("------------start top news spider--------------");
		try {
			for (int i = 0; i < topNewsURLs.size(); i++) {
				log.info("start top news URL " + topNewsURLs.get(i));
				Document dynamic_doc = JsoupParse.getDocument(
						topNewsURLs.get(i), "utf-8", 1000);
				String jsonString = dynamic_doc.body().html().split("=")[1]
						.toString();
				JSONObject jsonObject = JSONObject.parseObject(jsonString
						.substring(1, jsonString.length() - 1));
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				log.info("has gotten the JSON data");
				for (int j = 0; j < jsonArray.size(); j++) {
					String newsTitle = jsonArray.getJSONObject(j)
							.get("title").toString();
					String news_link = jsonArray.getJSONObject(j).get("url")
							.toString();
					Date pubDate = DateUtils.parseFullTime(jsonArray
							.getJSONObject(j).get("create_date").toString() +" "+jsonArray
							.getJSONObject(j).get("create_time").toString());
					String media = jsonArray.getJSONObject(j).get("media")
							.toString();
					String stockCode = newsMatch(newsTitle);
					String topNum = jsonArray.getJSONObject(j).get("top_num").toString();
					if (stockCode != null
							&& checkTopNews(news_link) == null) {
						sendNewsToSubscriber(newsTitle, stockCode, news_link, pubDate);
						saveTopNews(newsTitle, stockCode, topNum, news_link, pubDate, media);
					}
				}
				log.info("stop new URL");
			}
			// Thread.sleep(1000 * 60 * 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**  
	 * @Title: checkTopNews
	 * @Description: 检查top news是否已经存储到mysql
	 * @param: String
	 * @return: TopNewsStock
	 */
	public StockNews checkTopNews(String newsLink) {
		return ServiceFactory.createStockNewsService().queryStockNewsByLink(newsLink);
	}
	
	/**
	 * @Title: getNewsKeywords
	 * @Description: 获取新闻关键字
	 * @param: String
	 * @return: String
	 * @throws
	 */
	public String getNewsKeywords(String newsTitle) {
		return BadNewsFilterUtil.getInstance().isBadNews(newsTitle);
	}
	
	/**
	 * @Title: newsMatch
	 * @Description: 检查热点新闻是否和股票名字以及股票相关的热点关键词关联并获取股票代码
	 * @param String
	 * @return String
	 */
	public String newsMatch(String newsTitle) {
		// 获取数据库中已有股票代码和股票名称，简称， 常用名
		StockInfoService stockInfoService = ServiceFactory.createStockInfoService();
		industryStocks = stockInfoService.getAllBaseStocks();
		String stockCodeString=null;
		if(industryStocks!=null && industryStocks.size()>0){
			for(int i=0; i<industryStocks.size(); i++){
				BaseStocks industryStock = industryStocks.get(i);
				String stockName = industryStock.getStockName();
				stockName = stockName.replaceAll("股份", "");
				stockName = stockName.replace("*ST", "");
				stockName = stockName.replace("ST", "");
				stockName = stockName.replace("集团", "");
				if((newsTitle.contains(stockName) && newsTitle.contains(stockName))
				|| (industryStock.getShortName()!=null && newsTitle.contains(industryStock.getShortName()))){
					if(stockCodeString!=null){
						stockCodeString = stockCodeString + "," + industryStock.getStockCode();
					} else {
						stockCodeString = industryStock.getStockCode();
					}	
				}
				String keywords = industryStock.getKeyWords();
				if (keywords != null) {
					String[] keywords_list = keywords.split(",");
					for (int j = 0; j < keywords_list.length; j++) {
						if (newsTitle.contains(keywords_list[j])){
							if(stockCodeString!=null){
								stockCodeString = stockCodeString + "," + industryStock.getStockCode();
							} else {
								stockCodeString = industryStock.getStockCode();
							}
						}		
					}
				}
			}
		}
		return stockCodeString;
	}

	/**
	 * @Title: saveTopNews
	 * @Description: 存储stocknews到mysql
	 * @param String
	 * @param String
	 * @param Date
	 * @param String
	 * @return
	 */
	public void saveTopNews(String newsTitle, String stockCode, String topNum, String news_link, Date pubDate, String media) {
		for(int i = 0; i < stockCode.split(",").length; i++){
			StockNews stockNews = new StockNews();
			stockNews.setId(CommonUtil.getGenerateId());
			stockNews.setTitle(newsTitle);
			stockNews.setNewsLink(news_link);
			stockNews.setPubDate(pubDate);
			stockNews.setStockCode(stockCode.split(",")[i]);
			stockNews.setTopNum(Integer.parseInt(topNum.replaceAll(",","")));
			stockNews.setMedia(media);
			stockNews.setNewsSource(1);
			if(getNewsKeywords(newsTitle) != null){
				stockNews.setBearFlag(1);
			}else{
				stockNews.setBearFlag(0);
			}
			stockNews.setCreateDate(new Date());
			stockNews.setStatusFlag(0);
			ServiceFactory.createStockNewsService().insertStockNews(stockNews);
		}
	}
	
	/**
	 * @Title: sendNewsToSubscriber
	 * @Description: 推送即时新闻给收藏了该新闻关联的股票的用户
	 * @param String
	 * @param String
	 * @param Date
	 * @return
	 */
	public void sendNewsToSubscriber(String newsTitle, String stockCode, String news_link, Date pubDate){
		UserStockService userStockService = ServiceFactory.createUserStockService();
		List<WeiXinTemplateModel> weixinData = new ArrayList<WeiXinTemplateModel>();
		for(int i = 0; i < stockCode.split(",").length; i++){
			List<UserStock> userStocks = userStockService.getUserIdByStockCd(stockCode.split(",")[i]);
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar calTime = Calendar.getInstance();
			String sendTime = sf.format(calTime.getTime());
			for(UserStock userStock : userStocks){
				WeiXinTemplateModel tempModel = new WeiXinTemplateModel();
				String userId = userStock.getUserId();
				tempModel.setToUser(userId);
				tempModel.setFirst("黑天鹅即时新闻");
				tempModel.setTime(sendTime);
				tempModel.setContent(newsTitle);
				tempModel.setRemark("新闻发布时间："+sf.format(pubDate));
				tempModel.setUrl(news_link);
				weixinData.add(tempModel);
			}
		}
		//发送模板消息
		if(weixinData.size()>0)
		{
			WeiXinUtil.sendWeixinMsg(weixinData);
		}
	}
	
	public static void work() {
		new Thread(new TopNewsSpider()).start();
	}

	public static void main(String[] args) throws Exception {
		work();
	}

}
