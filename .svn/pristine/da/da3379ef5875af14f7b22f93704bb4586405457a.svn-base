package com.dxc.darklight.spider;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.dxc.darklight.http.JsoupParse;
import com.dxc.darklight.model.StockModelMount;
import com.dxc.darklight.service.ServiceFactory;
import com.dxc.darklight.service.DealMountModelService;

public class StockTest {

	public StockTest() {
		// TODO Auto-generated constructor stub
	}
@Test
	public void paserData() {
		// TODO Auto-generated method stub
		try{
		System.out.println("entity href1:11111111111");
		String entryUrl="http://roll.finance.sina.com.cn/finance/zq1/ssgs/index_1.shtml";
		//entryUrl=ConfFactory.getConf().get("companyNewsEntry","");
//		Document doc = JsoupParse.getDocument(entryUrl, "gb2312",1000);
		
//		System.out.println("entity href:"+doc.toString());
//		Elements eles = SimpleParse.getElements(doc, "span#t_phone>img[src]");
//		String strval = SimpleParse.getAttr(doc, "span#t_phone>img","src");
//		System.out.println("mobil img url:"+strval);
		
	

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
@Test
public void testStockModel(){
	try{
	System.out.println("test stock model");

	DealMountModelService sm=ServiceFactory.createUserStockModelService();
	StockModelMount smm=sm.getStockModelResult("sz000558");
	System.out.println(smm.getStockName()+"--------"+smm.getPricePhase());

	}catch(Exception ex){
		ex.printStackTrace();
	}
}

}
