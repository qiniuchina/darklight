package com.dxc.darklight.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.dxc.darklight.model.BlackSwanStocks;
import com.dxc.darklight.model.WeiXinTemplateModel;
import com.dxc.darklight.service.BlackStockService;
import com.dxc.darklight.service.ServiceFactory;
import com.dxc.darklight.service.BlackSwanStocksService;
import com.dxc.darklight.weixin.App;
import com.dxc.darklight.weixin.WeiXinUtil;

public class BlackSwanStockMsg {
	
	/**
	 * 每天下午搜寻黑天鹅股票，发送黑天鹅股票去公众号
	 */
	public static void work() {
		
		//除了周末，每天下午都执行.
		Calendar cal = Calendar.getInstance();
		
		if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
		{
			System.out.println("此时间段不是交易时间!");
			return;
		}
		List<WeiXinTemplateModel> weixinData = new ArrayList<WeiXinTemplateModel>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar calTime = Calendar.getInstance();
		String wtime = sf.format(calTime.getTime());
		BlackSwanStocksService blackSwanStocksService = ServiceFactory.createBlackSwanStocksService();
		BlackStockService blackStockService = ServiceFactory.createBlackStockService();
		List<BlackSwanStocks> blackSwanStocks = blackSwanStocksService.getAllBlackSwanStocks();
		List<String> userList=new ArrayList<String>();
		userList.add("oxw8iwedH7J7IFR4xV5qOpsxaHrc");
		userList.add("oxw8iwfXtg2FTeDUQCZOUk82FgiQ");
		userList.add("oxw8iwQ_DPNKHlfM6MRUp7n2sFcI");
		userList.add("oxw8iwTNSBnMFnSJ2kz4RPo7V9Bk");
		userList.add("oxw8iwUfyZ_NeQ-RRELTA5oEtPwU");
		userList.add("oxw8iwZ9w0RCrnCGs5xPk3-cmp2Y");
		
		
		if(blackSwanStocks !=null && blackSwanStocks.size() > 0)
		{
			//当取得的黑天鹅数据大于三条时，只取最前面的三条
			int length=0;
			if(blackSwanStocks.size()<=3){
				length=blackSwanStocks.size();
			}
			else{
				length=3;
			}
			
			for(int index=0;index<length;index++){	
				for(String usr : userList ){
					WeiXinTemplateModel tempModel = null;
					String stockCd=blackSwanStocks.get(index).getStockCode();
					String stockName=blackStockService.getStockName(stockCd);
					tempModel = new WeiXinTemplateModel();
					tempModel.setToUser(usr);
					tempModel.setFirst("今日黑天鹅预测");
					tempModel.setTime(wtime);
					tempModel.setColor("#ff0000");
					tempModel.setContent("股票("+stockName+")出现了"+blackSwanStocks.get(index).getStageComments()+",未来几天极有可能上涨！");
					tempModel.setRemark("点击查看更多早晨之星股票");
					tempModel.setUrl(App.MOR_STOCK_LIST_URL_PRE);
					weixinData.add(tempModel);		
				}
				
			}
				
		}	
			//发送模板消息
			if(weixinData.size()>0)
			{
				WeiXinUtil.sendWeixinMsg(weixinData);
			}		
	}
	
	public static void main(String[] args) throws Exception
	{
		work();
	}

}
