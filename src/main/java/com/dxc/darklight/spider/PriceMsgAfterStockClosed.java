package com.dxc.darklight.spider;


import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.dxc.darklight.http.JsoupParse;
import com.dxc.darklight.model.StockCurrentPrice;
import com.dxc.darklight.model.UserStock;
import com.dxc.darklight.model.WeiXinTemplateModel;
import com.dxc.darklight.service.ServiceFactory;
import com.dxc.darklight.service.UserStockService;
import com.dxc.darklight.util.CommonUtil;
import com.dxc.darklight.weixin.App;
import com.dxc.darklight.weixin.WeiXinUtil;

public class PriceMsgAfterStockClosed {
	
	/**
	 * 每天下午发送关注了股票的价格信息给关注者，除了周末
	 */
	public static void work() {
		
		//除了周末，每天下午都执行.
		Calendar cal = Calendar.getInstance();
		
		if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
		{
			System.out.println("此时间段不是交易时间!");
			return;
		}
		UserStockService userStockService = ServiceFactory.createUserStockService();
		boolean isholiday=userStockService.isHoliday(new Date(cal.getTimeInMillis()));
		if(isholiday){
			System.out.println("此时间段不是交易时间!");
			return;
		}
		List<String> usrStocks = userStockService.getAllUserStockCodes();
		String url = "http://hq.sinajs.cn/list=";
		if(usrStocks !=null && usrStocks.size() > 0)
		{
			String para = "";
			for(String usrStock : usrStocks)
			{
				if(usrStock != null)
				{
					if(usrStock.startsWith("sh") || usrStock.startsWith("sz"))
					{
						para += ("s_"+usrStock+",");
					}
					else
					{
						if("1A0001".equalsIgnoreCase(usrStock)){
							usrStock = "sh000001";
						}else{
							usrStock = CommonUtil.formatStockCode(usrStock);
						}
						para += ("s_"+usrStock+",");
					}
				}
			}
			url = url + para;
			Document doc = null;
			try {
				doc = JsoupParse.getDocument(url, "gb2312");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String data = doc.body().html();
			Map<String, StockCurrentPrice> stockCurrentPrice = new HashMap<String, StockCurrentPrice>();
			if(data != null)
			{
				String [] arr = data.split(";");
				if(arr!=null && arr.length>0)
				{
					for(int i=0; i<arr.length; i++)
					{
						try
						{
							String str = arr[i];
							System.out.println(str);
							//解析每一个字符串，得到股票代码和名称等信息
							if(str!=null && !"".equals(str))
							{
								//如果是非法的返回就略过
								String[] twoPart = str.split("=");
								if(twoPart==null || twoPart.length!=2 || twoPart[1].length()<5)
								{
									continue;
								}
								
								//解析股票代码
								String partOne = twoPart[0];
								String stockCd = partOne.substring(partOne.indexOf("_str_s_")+7);
								
								//解析当前价格以及涨跌幅，交易量等信息
								String partTwo = twoPart[1];
								partTwo = partTwo.substring(1, partTwo.length()-1);
								String[] partTwoArr = partTwo.split(",");
								String stockName = partTwoArr[0];
								double currentPrice = Double.parseDouble(partTwoArr[1]);
								double upDownPrice = Double.parseDouble(partTwoArr[2]);
								double upDownPencent = Double.parseDouble(partTwoArr[3]);
								int dealMount = Integer.parseInt(partTwoArr[4]);
								
								if(dealMount == 0)
								{
									continue;
								}
								
								//生成当前价格对象
								StockCurrentPrice sc = new StockCurrentPrice();
								sc.setStockCode(stockCd);
								sc.setStockName(stockName);
								sc.setStockPrice(currentPrice);
								sc.setStockUpdownPrice(upDownPrice);
								sc.setStockUpdownPercent(upDownPencent);
								sc.setStockDealMount(dealMount);
								Timestamp ts = new Timestamp(System.currentTimeMillis());
								sc.setCreateDtm(ts);
								stockCurrentPrice.put(stockCd, sc);
								
							}
						}catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
			
			List<WeiXinTemplateModel> weixinData = new ArrayList<WeiXinTemplateModel>();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar calTime = Calendar.getInstance();
			String wtime = sf.format(calTime.getTime());
			
			List<String> usrs = userStockService.getAllUsers();
			if(usrs != null && usrs.size()>0)
			{
				for(String usr : usrs)
				{
					List<UserStock> userStock = userStockService.getStockCdByUsrId(usr);
					if(userStock != null && userStock.size() > 0)
					{
						String content = "";
						for(UserStock usrNew : userStock)
						{
							String stockCode = usrNew.getStockCode();
							if("1A0001".equalsIgnoreCase(stockCode)){
								stockCode = "sh000001";
							}else{
								stockCode = CommonUtil.formatStockCode(stockCode);
							}
							StockCurrentPrice sc = stockCurrentPrice.get(stockCode);
							if(sc != null)
							{
								double cPrice = sc.getStockUpdownPercent();
								String stockName = sc.getStockName();
								content+=(stockName+"("+cPrice+"%), ");
							}
						}
						content = content.substring(0, content.length()-2);
						WeiXinTemplateModel tempModel = null;
						tempModel = new WeiXinTemplateModel();
						tempModel.setToUser(usr);
						tempModel.setFirst("股票收盘价格");
						tempModel.setTime(wtime);
						tempModel.setContent("您关注的股票("+content);
						tempModel.setRemark("您可以在公众号中关注/取消关注此股票!");
						tempModel.setUrl(App.STOCK_LIST_URL_PRE+"userId="+usr);
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
	}
	
	public static void main(String[] args) throws Exception
	{
		work();
	}

}
