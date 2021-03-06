package com.dxc.darklight.spider;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

public class ExceptionNoticeMsg {

	/**
	 * 计算股票的交易量权值
	 */
	//上证指数
	private static String SH000001 = "sh000001";
	//创业板指数
	private static String SZ399006 = "sz399006";
	
	public static void work() {
		
		//当前时间必须在工作日的上午9点20到下午3点，否则不执行此方法
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		double hTime = hour + minute/60f;
		
		if(hTime<9.3 || hTime>15 || cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
		{
			System.out.println("此时间段不是交易时间!");
			return;
		}
		UserStockService userStockService = ServiceFactory.createUserStockService();
		//判断是否为节日
		boolean isholiday=userStockService.isHoliday(new Date(cal.getTimeInMillis()));
		if(isholiday){
			System.out.println("此时间段不是交易时间!");
			return;
		}
		
		//删除过去12小时的临时数据
		userStockService.deletePreviousData(-12);
		//得到全部的股票代码
		List<String> usrStocks = userStockService.getAllUserStockCodes();
		
		//获取数据
		String url = "http://hq.sinajs.cn/list=";
		if(usrStocks !=null && usrStocks.size() > 0)
		{
			//把上证和创业板加上
			usrStocks.add(SH000001);
			usrStocks.add(SZ399006);
			//拼接请求参数
			String para = getPara(usrStocks);
			url = url + para;
			
			//得到股票当前的价格信息
			List<StockCurrentPrice> stockCurrentPrice = getCurrentPrice(url);
			
			List<WeiXinTemplateModel> weixinData = new ArrayList<WeiXinTemplateModel>();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar calTime = Calendar.getInstance();
			String wtime = sf.format(calTime.getTime());
			
			//把当前价格的数据存入数据库
			for(StockCurrentPrice sc : stockCurrentPrice)
			{
				double cPrice = sc.getStockUpdownPercent();
				//有前缀的股票代码
				String preStockCd = sc.getStockCode();
				//没有前缀的股票代码
				String stockCd = preStockCd.substring(2);
				
				if(SH000001.equalsIgnoreCase(preStockCd) || SZ399006.equalsIgnoreCase(preStockCd))
				{
					//上证指数浮动0.5个点或者创业板指数浮动1.0
					if((Math.abs(cPrice) >= 0.5f && SH000001.equalsIgnoreCase(preStockCd)) 
							|| (Math.abs(cPrice) >= 1.0f && SZ399006.equalsIgnoreCase(preStockCd)))
					{
						if(SH000001.equalsIgnoreCase(preStockCd)){
							stockCd="1A0001";
						}
						List<UserStock> userStockNews = userStockService.getUserIdByStockCd(stockCd);
						if(userStockNews != null && userStockNews.size() > 0)
						{
							for(UserStock usrNew : userStockNews)
							{
								WeiXinTemplateModel tModel = daPanStocksMsg(usrNew, sc, userStockService, wtime);
								weixinData.add(tModel);
							}
						}		
					}
				}
				else
				{
					//其他所有股票涨幅有2个点的时候
					if(Math.abs(cPrice) >= 2.0f)
					{
						List<UserStock> userStockNews = userStockService.getUserIdByStockCd(stockCd);
						if(userStockNews != null && userStockNews.size() > 0)
						{
							for(UserStock usrNew : userStockNews)
							{
								WeiXinTemplateModel tModel = allStocksMsg(usrNew, sc, userStockService, wtime);
								if(tModel!=null){
								weixinData.add(tModel);
								}
							}
						}						
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
	
	//拼接请求参数
	private static String getPara(List<String> usrStocks)
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
					usrStock = CommonUtil.formatStockCode(usrStock);
					para += ("s_"+usrStock+",");
				}
			}
		}
		return para;
	}
	
	//通过参数得到返回结果
	private static List<StockCurrentPrice> getCurrentPrice(String url)
	{
		Document doc = null;
		try {
			doc = JsoupParse.getDocument(url, "gb2312");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String data = doc.body().html();
		List<StockCurrentPrice> stockCurrentPrice= new ArrayList<StockCurrentPrice>();
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
							stockCurrentPrice.add(sc);
							
						}
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		
		return stockCurrentPrice;
	}
	
	//其他所有股票的信息
	private static WeiXinTemplateModel allStocksMsg(UserStock usrNew, StockCurrentPrice sc, UserStockService userStockService, String wtime)
	{
		double cPrice = sc.getStockUpdownPercent();
		double cp = sc.getStockPrice();
		//有前缀的股票代码
		String preStockCd = sc.getStockCode();
		//没有前缀的股票代码
		String stockCd = preStockCd.substring(2);
		String stockName = sc.getStockName();
		String usrId = usrNew.getUserId();
		StockCurrentPrice preSc = userStockService.getExistedStockPrice(stockCd, usrId);
		WeiXinTemplateModel tempModel = null;
		if(preSc == null)
		{
			//如果消息不存在，则插入到数据库里面
			sc.setUserId(usrId);
			sc.setStockCode(stockCd);
			userStockService.saveUserStockCodes(sc);
			sc.setStockCode(preStockCd);
			//并且发送微信消息给用户
			tempModel = new WeiXinTemplateModel();
			tempModel.setToUser(usrId);
			tempModel.setFirst("股票异动提醒");
			tempModel.setTime(wtime);
			if(cPrice>=0)
			{
				tempModel.setColor("#ff0000");
				tempModel.setContent("您关注的股票("+stockName+")截止目前已经上涨了"+cPrice+"%, 请随时关注此股票!");
			}
			else
			{
				tempModel.setColor("#008000");
				tempModel.setContent("您关注的股票("+stockName+")截止目前已经下跌了"+cPrice+"%, 请随时关注此股票!");
			}
			tempModel.setRemark("当前价格为:"+cp);
			tempModel.setUrl(App.STOCK_NEWS_URL_PRE+stockCd+"&userId="+usrId);
		}
		else
		{
			//如果今天消息已经发送过了的, 但是股票价格又有较大的波动，再次发送消息给用户
			double prePrice = preSc.getStockUpdownPercent();
			if(Math.abs(cPrice - prePrice) >= 1.5f)
			{
				//存入数据库
				sc.setUserId(usrId);
				sc.setStockCode(stockCd);
				userStockService.saveUserStockCodes(sc);
				sc.setStockCode(preStockCd);
				//并且发送微信消息给用户
				tempModel = new WeiXinTemplateModel();
				tempModel.setToUser(usrId);
				tempModel.setFirst("股票异动提醒");
				tempModel.setTime(wtime);
				if(cPrice>=0)
				{
					tempModel.setColor("#ff0000");
					tempModel.setContent("您关注的股票("+stockName+")截止目前已经上涨了"+cPrice+"%, 请随时关注此股票!");
				}
				else
				{
					tempModel.setColor("#008000");
					tempModel.setContent("您关注的股票("+stockName+")截止目前已经下跌了"+cPrice+"%, 请随时关注此股票!");
				}
				tempModel.setRemark("当前价格为:"+cp);
				tempModel.setUrl(App.STOCK_NEWS_URL_PRE+stockCd+"&userId="+usrId);
			}
		}
		
		return tempModel;
	}
	
	//其他所有股票的信息
	private static WeiXinTemplateModel daPanStocksMsg(UserStock usrNew, StockCurrentPrice sc, UserStockService userStockService, String wtime)
	{
		double cPrice = sc.getStockUpdownPercent();
		double cp = sc.getStockPrice();
		//有前缀的股票代码
		String preStockCd = sc.getStockCode();
		//没有前缀的股票代码
		String stockCd = null;
		if("sh000001".equalsIgnoreCase(preStockCd)){
			stockCd = "1A0001";
		}else{
			stockCd = preStockCd.substring(2);
		}
		String stockName = sc.getStockName();
		String usrId = usrNew.getUserId();
		StockCurrentPrice preSc = userStockService.getExistedStockPrice(stockCd, usrId);
		WeiXinTemplateModel tempModel = null;
		if(preSc == null)
		{
			//如果消息不存在，则插入到数据库里面
			sc.setUserId(usrId);
			if("sh000001".equalsIgnoreCase(preStockCd)){
				stockCd = "1A0001";
			}
			sc.setStockCode(stockCd);
			userStockService.saveUserStockCodes(sc);
			sc.setStockCode(preStockCd);
			//并且发送微信消息给用户
			tempModel = new WeiXinTemplateModel();
			tempModel.setToUser(usrId);
			tempModel.setFirst("大盘异动提醒");
			tempModel.setTime(wtime);
			if(cPrice>=0)
			{
				tempModel.setColor("#ff0000");
				tempModel.setContent("您关注的大盘("+stockName+")截止目前已经上涨了"+cPrice+"%, 请随时关注!");
			}
			else
			{
				tempModel.setColor("#008000");
				tempModel.setContent("您关注的大盘("+stockName+")截止目前已经下跌了"+cPrice+"%, 请随时关注!");
			}
			tempModel.setRemark("当前指数为:"+cp);
			tempModel.setUrl(App.STOCK_LIST_URL_PRE+"&userId="+usrId);
		}
		else
		{
			//如果今天消息已经发送过了的, 但是股票价格又有较大的波动，再次发送消息给用户
			double prePrice = preSc.getStockUpdownPercent();
			if((Math.abs(cPrice - prePrice) >= 0.3f && SH000001.equalsIgnoreCase(preStockCd))
					|| (Math.abs(cPrice - prePrice) >= 0.5f && SZ399006.equalsIgnoreCase(preStockCd)))
			{
				//存入数据库
				sc.setUserId(usrId);
				if("sh000001".equalsIgnoreCase(preStockCd)){
					stockCd = "1A0001";
				}
				sc.setStockCode(stockCd);
				userStockService.saveUserStockCodes(sc);
				sc.setStockCode(preStockCd);
				//并且发送微信消息给用户
				tempModel = new WeiXinTemplateModel();
				tempModel.setToUser(usrId);
				tempModel.setFirst("大盘异动提醒");
				tempModel.setTime(wtime);
				if(cPrice>=0)
				{
					tempModel.setColor("#ff0000");
					tempModel.setContent("您关注的大盘("+stockName+")截止目前已经上涨了"+cPrice+"%, 请随时关注!");
				}
				else
				{
					tempModel.setColor("#008000");
					tempModel.setContent("您关注的大盘("+stockName+")截止目前已经下跌了"+cPrice+"%, 请随时关注!");
				}
				tempModel.setRemark("当前指数为:"+cp);
				tempModel.setUrl(App.STOCK_LIST_URL_PRE+"&userId="+usrId);
			}
		}
		
		return tempModel;
	}
	
	
	
	public static void main(String[] args) throws Exception
	{
		work();
	}

}
