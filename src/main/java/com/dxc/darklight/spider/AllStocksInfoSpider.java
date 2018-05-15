package com.dxc.darklight.spider;

import java.sql.Timestamp;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dxc.darklight.http.JsoupParse;
import com.dxc.darklight.model.Industry;
import com.dxc.darklight.model.BaseStocks;
import com.dxc.darklight.service.ServiceFactory;
import com.dxc.darklight.service.StockInfoService;

public class AllStocksInfoSpider {

	private static String INDUSTRY_URL = "http://quote.cfi.cn/cache_image/hy_avgzdf_desc.htm";
	
	/**
	 * 爬取每个行业下面的股票信息，分行业爬取
	 */
	public static void work() {
		try {
			Document doc = JsoupParse.getDocument(INDUSTRY_URL, "utf-8");
			Elements eles= JsoupParse.getElements(doc, "td.tbhead a");
			Industry industryInfo = null;
			for(Element ele : eles)
			{
				String url = JsoupParse.getAttr(ele, "href");
				String industry = JsoupParse.getText(ele);
				industryInfo = new Industry();
				industryInfo.setIndustryNm(industry);
				industryInfo.setCreateDtm(new Timestamp(System.currentTimeMillis()));
				industryInfo.setUpdDtm(new Timestamp(System.currentTimeMillis()));
				StockInfoService is = ServiceFactory.createStockInfoService();
				//插入或者更新行业信息，返回行业代码
				int code = is.insertOrUpdateIndustryInfo(industryInfo);
				process(industry, url, code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void process(String industry, String url, int code)
	{
		try
		{
			do
			{
				url = processStock(industry, url, code);
				
			}while(url!=null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static String processStock(String industry, String url, int code) throws Exception
	{
		//System.out.println(industry+":"+url);
		Document doc = JsoupParse.getDocument(url, "utf-8");
		//做解析的代码
		Elements trs= JsoupParse.getElements(doc, ".table_data tr:not(.tbcaption)");
		
		//定义所要取的内容变量
		String stockId = null;
		String stockName = null;
		String stockWin = null;
		String totalStock = null;
		String passStock = null;
		BaseStocks industryStocks = null;
		for(Element tr: trs)
		{
			stockId = JsoupParse.getElements(tr.child(0), "a").text();
			stockName = JsoupParse.getElements(tr.child(1), "a").text();
			stockWin = JsoupParse.getElements(tr.child(11), "nobr").text();
			totalStock = JsoupParse.getElements(tr.child(13), "nobr").text();
			passStock = JsoupParse.getElements(tr.child(14), "nobr").text();
			industryStocks = new BaseStocks();
			industryStocks.setIndustryCd(code);
			industryStocks.setStockCode(stockId);
			industryStocks.setStockName(stockName);
			industryStocks.setStockWin(stockWin);
			industryStocks.setTotalStock(totalStock);
			industryStocks.setPassStock(passStock);
			industryStocks.setCreateDtm(new Timestamp(System.currentTimeMillis()));
			industryStocks.setUpdDtm(new Timestamp(System.currentTimeMillis()));
			StockInfoService is = ServiceFactory.createStockInfoService();
			//插入或者更新股票信息
			is.insertOrUpdateBaseStocks(industryStocks);
			System.out.println(industryStocks);
		}
		
		//判断是否有分页，并把下一页的url地址返回
		Element pagestr= JsoupParse.getElement(doc, ".pagestr");
		//System.out.println(pagestr);
		if(pagestr==null)
		{
			return null;
		}
		else
		{
			Element next = JsoupParse.getElement(pagestr, "a:containsOwn(下一页)");
			if(next == null)
			{
				return null;
			}
			else
			{
				return "http://quote.cfi.cn/quotelist.aspx"+JsoupParse.getAttr(next, "href");
			}			
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		work();
	}

}
