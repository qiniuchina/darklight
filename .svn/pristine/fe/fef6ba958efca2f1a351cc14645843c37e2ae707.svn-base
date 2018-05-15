package com.dxc.darklight.spider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dxc.darklight.http.JsoupParse;
import com.dxc.darklight.model.StockComment;
import com.dxc.darklight.service.ServiceFactory;
import com.dxc.darklight.util.CommonUtil;
import com.dxc.darklight.util.DateUtils;

/**
 * @ClassName: StockCommentSpider
 * @Description: blog
 * @author: Wei Wei
 * @date: June 2, 2017 2:04:35 PM
 */
public class StockCommentSpider implements Runnable {
	private static final Logger log = LogManager
			.getLogger(StockCommentSpider.class);
	private static List<String> blogURLs = null;
	static {
		blogURLs = new ArrayList<String>();
		blogURLs.add("http://blog.sina.com.cn/jinyanshi");
		blogURLs.add("http://blog.sina.com.cn/shuipi");
		blogURLs.add("http://blog.sina.com.cn/liaonanguwang");
		blogURLs.add("http://blog.sina.com.cn/ixmg");
	}
	@Override
	public void run() {
		log.info("------------start blog spider--------------");
		for(String blogUrl: blogURLs){
			log.info("start blog URL " + blogUrl);
			Document dynamic_doc = JsoupParse.getDocument(blogUrl, "utf-8", 1000);
			String blogAuthor = JsoupParse.getOwnText(dynamic_doc, "#ownernick");
			Element blogList = JsoupParse.getElement(dynamic_doc, ".bloglist");
			Elements blogs = JsoupParse.getElements(blogList, ".blog_title_h");
			for(Element blog: blogs){
				Element titleAndLink = JsoupParse.getElement(blog, ".blog_title");
				String blogTitle = JsoupParse.getText(JsoupParse.getElement(titleAndLink, "a"));
				String blogLink = JsoupParse.getAttr(JsoupParse.getElement(titleAndLink, "a"), "href");
				String blogPubDate = JsoupParse.getText(JsoupParse.getElement(blog, ".time*")).replace("(", "").replace(")", "");
				log.info(blogAuthor+"---"+blogTitle+"---"+blogLink+"---"+blogPubDate);
				if(checkStockComment(blogLink)==null){
					saveStockComment(blogAuthor, blogTitle, blogLink, blogPubDate);
				}
			}
		}
		log.info("------------stop blog spider--------------");
	}

	/**  
	 * @Title: checkStockComment
	 * @Description: 检查blog是否已经存储到mysql
	 * @param: String
	 * @return: StockComment
	 */
	public StockComment checkStockComment(String blogLink) {
		return ServiceFactory.createStockCommentService().queryStockCommentByLink(blogLink);
	}
	
	/**
	 * @Title: saveStockComment
	 * @Description: 存储StockComment到mysql
	 * @param String
	 * @param String
	 * @param String
	 * @param String
	 */
	public void saveStockComment(String blogAuthor, String blogTitle, String blogLink, String blogPubDate){
		StockComment stockComment = new StockComment();
		stockComment.setId(CommonUtil.getGenerateId());
		stockComment.setAuthor(blogAuthor);
		stockComment.setTitle(blogTitle);
		stockComment.setNewsLink(blogLink);
		try {
			stockComment.setPubDate(DateUtils.parseMinuteTime(blogPubDate));
		} catch (Exception e) {
			e.printStackTrace();
		}
		stockComment.setFetchDate(new Date());
		ServiceFactory.createStockCommentService().insertStockComment(stockComment);
	}
	
	public static void work() {
		new Thread(new StockCommentSpider()).start();
	}

	public static void main(String[] args) throws Exception {
		work();
	}
}
