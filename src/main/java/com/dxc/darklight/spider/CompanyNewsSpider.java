package com.dxc.darklight.spider;

/**
 * All rights Reserved
 * @Title: 	CompanyNewsSpider.java 
 * @Package com.dxc.darklight.spider 
 * @Description: Page Downloader
 * @author:	 Wei Wei  
 * @date:	May 10, 2017 4:33:35 PM 
 * @version	V1.0   
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dxc.darklight.conf.ConfFactory;
import com.dxc.darklight.http.JsoupParse;
import com.dxc.darklight.model.StockNews;
import com.dxc.darklight.model.UserStock;
import com.dxc.darklight.model.WeiXinTemplateModel;
import com.dxc.darklight.service.ServiceFactory;
import com.dxc.darklight.service.UserStockService;
import com.dxc.darklight.util.BadNewsFilterUtil;
import com.dxc.darklight.util.CommonUtil;
import com.dxc.darklight.util.DateUtils;
import com.dxc.darklight.weixin.WeiXinUtil;

/**
 * @ClassName: CompanyNewsSpider
 * @Description: Page Downloader
 * @author: Wei Wei
 * @date: May 10, 2017 4:33:35 PM
 */
public class CompanyNewsSpider implements Runnable {
	private static final Logger log = LogManager
			.getLogger(CompanyNewsSpider.class);

	public static String entryUrl = ConfFactory.getConf().get(
			"CompanyNewsEntry",
			"http://roll.finance.sina.com.cn/finance/zq1/ssgs/index.shtml");

	@Override
	public void run() {
		log.info("------------start company news spider entryUrl:--------------");
		log.info(entryUrl);
		try {
			Document doc = JsoupParse.getDocument(entryUrl, "GB2312", 1000);
			Elements ulElements = JsoupParse.getElements(doc,
					"ul[class=list_009]");
			for (Element ulElement : ulElements) {
				Elements liElements = JsoupParse.getElements(ulElement, "li");
				for (Element liElement : liElements) {
					String newsTitle = JsoupParse.getText(JsoupParse
							.getElement(liElement, "a"));
					String newsLink = JsoupParse.getAttr(
							JsoupParse.getElement(liElement, "a"), "href");
					Pattern p = Pattern.compile("\\(.*?\\)");
					Matcher m = p.matcher(JsoupParse.getText(liElement));
					Date pubDate = null;
					while (m.find()) {
						pubDate = getNewsPubDate(m.group());
					}
					String stockCode = null;
					Document news = JsoupParse.getDocument(newsLink, "UTF-8",
							1000);
					Element divElements = JsoupParse.getElement(news,
							"div[class=hqimg_wrapper]");
					if (divElements != null) {
						Element element = JsoupParse.getElement(divElements,
								"img");
						String stockCodeString = JsoupParse.getAttr(element,
								"src");
						String[] s = stockCodeString.split("/");
						stockCode = CommonUtil.processStockCode(s[s.length - 1]
								.substring(0, s[s.length - 1].indexOf(".")));
					}
					if (getNewsKeywords(newsTitle) != null
							&& checkTopNews(newsLink) == null
							&& stockCode != null) {
						sendNewsToSubscriber(newsTitle, stockCode,newsLink, pubDate);
						saveCompanyNews(newsTitle, stockCode, newsLink,
								pubDate);
					}
				}
			}
			// Thread.sleep(1000*60*5);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Title: getNewsPubDate
	 * @Description: 获取新闻发布时间
	 * @param: Sting
	 * @return: Date
	 * @throws Exception
	 */
	public Date getNewsPubDate(String pubDate) throws Exception {
		String pubDateString = pubDate.replace("(", "").replace(")", "");
		String monthString = pubDateString.split(" ")[0].split("月")[0];
		String dayString = pubDateString.split(" ")[0].split("月")[1].split("日")[0];
		String timeString = pubDateString.split(" ")[1];
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String dateString = year + "-" + monthString + "-" + dayString + " "
				+ timeString;
		return DateUtils.parseMinuteTime(dateString);
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
	 * @Title: checkTopNews
	 * @Description: 检查top news是否已经存储到mysql
	 * @param: String
	 * @return: TopNewsStock
	 */
	public StockNews checkTopNews(String newsLink) {
		return ServiceFactory.createStockNewsService().queryStockNewsByLink(
				newsLink);
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
	public void saveCompanyNews(String newsTitle, String stockCode,
			String news_link, Date pubDate) {
		StockNews stockNews = new StockNews();
		stockNews.setId(CommonUtil.getGenerateId());
		stockNews.setTitle(newsTitle);
		stockNews.setNewsLink(news_link);
		stockNews.setPubDate(pubDate);
		stockNews.setStockCode(stockCode);
		stockNews.setMedia("新浪证券");
		stockNews.setNewsSource(0);
		stockNews.setBearFlag(1);
		stockNews.setCreateDate(new Date());
		stockNews.setStatusFlag(0);
		ServiceFactory.createStockNewsService().insertStockNews(stockNews);
	}

	/**
	 * @Title: sendNewsToSubscriber
	 * @Description: 推送即时新闻给收藏了该新闻关联的股票的用户
	 * @param String
	 * @param String
	 * @param Date
	 * @return
	 */
	public void sendNewsToSubscriber(String newsTitle, String stockCode,
			String news_link, Date pubDate) {
		UserStockService userStockService = ServiceFactory
				.createUserStockService();
		List<WeiXinTemplateModel> weixinData = new ArrayList<WeiXinTemplateModel>();
		List<UserStock> userStocks = userStockService
				.getUserIdByStockCd(stockCode);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar calTime = Calendar.getInstance();
		String sendTime = sf.format(calTime.getTime());
		for (UserStock userStock : userStocks) {
			WeiXinTemplateModel tempModel = new WeiXinTemplateModel();
			String userId = userStock.getUserId();
			tempModel.setToUser(userId);
			tempModel.setFirst("黑天鹅即时新闻");
			tempModel.setTime(sendTime);
			tempModel.setContent(newsTitle);
			tempModel.setRemark("新闻发布时间：" + sf.format(pubDate));
			tempModel.setUrl(news_link);
			weixinData.add(tempModel);
		}
		// 发送模板消息
		if (weixinData.size() > 0) {
			WeiXinUtil.sendWeixinMsg(weixinData);
		}
	}

	public static void work() {
		new Thread(new CompanyNewsSpider()).start();
	}

	public static void main(String[] args) throws Exception {
		work();
	}

}
