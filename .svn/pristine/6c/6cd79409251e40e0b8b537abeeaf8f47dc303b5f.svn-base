package com.dxc.darklight.spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dxc.darklight.conf.ConfFactory;
import com.dxc.darklight.datasource.solr.SolrFactory;
import com.dxc.darklight.http.JsoupParse;
import com.dxc.darklight.model.IndividualStockNews;
import com.dxc.darklight.service.BlackStockService;
import com.dxc.darklight.service.IndividualStockNewsService;
import com.dxc.darklight.service.ServiceFactory;
import com.dxc.darklight.service.UserStockService;
import com.dxc.darklight.service.impl.BlackStockServiceImpl;
import com.dxc.darklight.util.CommonUtil;

public class IndividualStockNewsSpider {
	private static final Logger log = LogManager.getLogger(IndividualStockNewsSpider.class);

	public String process(String stockCd, String url) {
		String urlStockCode=CommonUtil.formatStockCode(stockCd);
		IndiStockNewsPageProcessor pageProcessor = new IndiStockNewsPageProcessor();
		url = url.replace("stockCd", urlStockCode);
		log.info("----start stock news spider entryUrl:----" + url);
		String nextPageUrl = null;// 下一页链接地址
		IndividualStockNewsService stockNewsService = ServiceFactory.createIndiStockNewsService();
		try {
			Document doc = JsoupParse.getDocument(url, "gb2312", 1000);
			// 获取新闻列表
			Elements divElements = JsoupParse.getElements(doc, "div[class=datelist] ul a");
			IndividualStockNews stockNews;
			// 拿出每一条新闻
			for (Element element : divElements) {
				stockNews = new IndividualStockNews();
				String newsTitle = JsoupParse.getText(element, "a");// 新闻标题
				String newsLink = element.attr("href");// 新闻链接
				Date pubDate = null;// 发布时间

				// 数据库中是否存在该URL,不存在为true；不存在的话，再去拉去新闻发布时间,并保存入库，以节省时间、资源
				boolean singleUrl = stockNewsService.isSingleUrl(newsLink);
				if (singleUrl) {
					if (newsLink != null && (newsLink.startsWith("http://finance.sina.com.cn")
							|| newsLink.startsWith("http://cj.sina.com.cn"))) {
						// get pubdate
						Element timeSource = null;
						String pubTimeStr = null;
						Document detail = JsoupParse.getDocument(newsLink, "utf-8");
						timeSource = JsoupParse.getElement(detail, "#wrapOuter >div >div.page-info > span");
						pubTimeStr = JsoupParse.getText(timeSource, "span");
						if (newsLink.startsWith("http://cj.sina.com.cn")) {
							pubDate = pageProcessor.getNewsPubDateCj(pubTimeStr);
						} else {
							pubDate = pageProcessor.getNewsPubDateFin(pubTimeStr);
						}
					}
					stockNews.setPubDate(pubDate);
					stockNews.setStockCode(stockCd);
					stockNews.setTitle(newsTitle);
					stockNews.setNewsLink(newsLink);
					stockNews.setFetchDate(new Date());

					// stockNewsService.getNewestDate(stockCd);
					// 数据库中最新发布的新闻时间,非第一次可以考虑
					// 最新日期大于数据库中的最大日期，并且新闻URL唯一
					saveIndividualStockNews(stockNews);
					System.out.println(stockNews);
				} else {

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		log.info("stop new URL: " + url);
		return nextPageUrl;
	}

	private static void saveIndividualStockNews(IndividualStockNews stockNews) {
		ServiceFactory.createIndiStockNewsService().insertIndividualStockNews(stockNews);
		log.info("stocknews title: " + stockNews.getTitle());
		SolrClient client = SolrFactory.getClient();
		try {
			client.add(stockNews.toSolrInputDocument());
			client.commit();
			client.close();
		} catch (SolrServerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void work() {
		IndividualStockNewsSpider spider = new IndividualStockNewsSpider();
		String nextPageUrl = null;
		IndividualStockNewsService indiStockCdService=ServiceFactory.createIndiStockNewsService();
		//获取用户关注股票(user_stock)和黑天鹅池中的股票(black_stock)
		List<String> stockCodes = indiStockCdService.getAllStockCodes();
		if (stockCodes == null || stockCodes.size() <= 0) {
			return;
		}

		// 爬取每支股票的新闻
		for (int i = 0; i < stockCodes.size(); i++) {
			String stockCd = stockCodes.get(i);
			if (stockCd != null) {
				log.info("Stock Sum :" + stockCodes.size() + "  current :" + i + " Stock code: " + stockCd);
				nextPageUrl = "http://vip.stock.finance.sina.com.cn/corp/view/vCB_AllNewsStock.php?symbol=stockCd&Page=1";
				do {
					nextPageUrl = spider.process(stockCd, nextPageUrl);
				} while (nextPageUrl != null);
			}
		}

	}

	public static void main(String args[]) {
		work();
	}

}
