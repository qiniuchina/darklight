package com.dxc.darklight.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dxc.darklight.http.JsoupParse;
import com.dxc.darklight.model.StockCurrentPrice;
import com.dxc.darklight.model.StocksInfoEntity;

public class CommonUtil {
	
	public static boolean isEmpty(String str) {
		return (str == null) || (str.equals("")) || (str.equals("null")) || str.equals(" ") || str.equals("　");
	}

	public static boolean notEmpty(String str) {
		return (str != null && !str.equals("") && !str.equals("null") && !str.equals(" ") && !str.equals("　"));
	}
	/**
	 * Checks if specified link is full URL.
	 * 
	 * @param link
	 * @return True, if full URl, false otherwise.
	 */
	public static boolean isFullUrl(String link) {
		if (link == null) {
			return false;
		}
		link = link.trim().toLowerCase();
		return link.startsWith("http://") || link.startsWith("https://") || link.startsWith("file://");
	}
	
	public static String makeUrl(String pageUrl, String path) {
		try {
			URL base = new URL(pageUrl);
			StringBuilder sb = new StringBuilder();
			sb.append(base.getProtocol());
			sb.append("://");
			sb.append(base.getHost());
			if(base.getPort() != 80 && base.getPort() > 0) sb.append(":" + base.getPort());
			if(path.charAt(0) != '/') sb.append('/');
			sb.append(path);
			return sb.toString();
		} catch (Exception e) {
		}
		
		return "";
	}
	
	/**
	 * 考虑各种链接格式，构造正确的url地址
	 * @param pageUrl 当前页url地址
	 * @param link 链接属性 
	 * @author liupf@buge.cn
	 */
	public static String fullUrl(String pageUrl, String link) {
		if (link == null)
			return "";
		
		if (!isFullUrl(pageUrl)) {  //保证当前页链接是以http://开始
			pageUrl = "http://" + pageUrl;
		}
		
		if (isFullUrl(link)) { //link本来就是绝对地址
			return link;
		} else if (link != null && link.startsWith("?")) { //link是查询参数，"?name=lpf"
			int qindex = pageUrl.indexOf('?');
			if (qindex < 0) {  //pageUrl=http://www.test.com/user，返回"http://www.test.com/use?name=lpf"
				return pageUrl + link;
			} else {           //pageUrl=http://www.test.com/user?name=one，返回"http://www.test.com/use?name=lpf"
				return pageUrl.substring(0, qindex) + link;
			}
		} else if(link.startsWith("/")) { //link是从根目录开始的地址
			return makeUrl(pageUrl, link);
		} else if(link.startsWith("./")) { //link是从"./"开始的地址
			int lastSlashIndex = pageUrl.lastIndexOf('/');
			if(lastSlashIndex > 8) {
				return pageUrl.substring(0, lastSlashIndex) + link.substring(1);
			} else {
				return pageUrl + link.substring(1);
			}
		} else if(link.startsWith("../")) { //link是从"../"开始的地址，回退一级
			return makeAssembleUrl(pageUrl, link);
		} else {
			int lastSlashIndex = pageUrl.lastIndexOf('/');
			if(lastSlashIndex > 8) {
				return pageUrl.substring(0, lastSlashIndex) + "/" + link;
			} else {
				return pageUrl + "/" + link;
			}
		}
	}
	private static String makeAssembleUrl(String base, String multiOmission) {
		String OMISSION = "../";
		String relative = multiOmission;
		while(relative.startsWith(OMISSION)) {
			base = getParentUrl(base);
			relative = relative.substring(OMISSION.length());
		}
		return base + relative;
	}
	private static String getParentUrl(String url) {
		int lastSlashIndex = url.lastIndexOf('/');
		String rest = url.substring(0, lastSlashIndex);
		lastSlashIndex = rest.lastIndexOf('/');
		if(lastSlashIndex > 8) {
			return url.substring(0, lastSlashIndex+1);
		} else {
			return url + "/";
		}
	}
	
	public static boolean isMobile(String mobile) {
        Pattern pattern = Pattern.compile("1[3-5|8]\\d{9}");
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }
	
	public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
	
	public static boolean isMobileOrTel(String mobile) {
        if (mobile.startsWith("13") || mobile.startsWith("15") || mobile.startsWith("18"))
            return isMobile(mobile);
        Pattern pattern = Pattern.compile("(\\d{3,4}[-_－—]?)?\\d{7,8}([-_－—]?\\d{1,7})?");
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }
	public static String trim(String source) {
		if(null == source) return "";
		
		int start=0, end=source.length()-1;
		for(int i=0; i<source.length(); i++) {
			int code = source.charAt(i);
			if (code != ' ' && code != '　' && code != '\n' && code != '\r' && code != '\t') {
				start = i;
				break;
			}
		}
		
		for(int i=source.length()-1; i>=0; i--) {
			int code = source.charAt(i);
			if (code != ' ' && code != '　' && code != '\n' && code != '\r' && code != '\t' && code != 160) {
				end = i+1;
				break;
			}
		}
		
		if (start < end) {
			return source.substring(start, end);
		} else {
			return source;
		}
	}
	/**
	 * 提取原始字符串nodeValue中prefix以后的子串
	 * 
	 * @param nodeValue
	 * @param prefix
	 * @return
	 */
	public static String stripAfter(String nodeValue, String prefix) {
		if (isEmpty(nodeValue))
			return "";

		String tmp = nodeValue.trim();
		int begin = 0;
		if (!isEmpty(prefix)) {
			int index = tmp.indexOf(prefix);
			if (index >= 0) {
				begin = index + prefix.length();
				return trim(tmp.substring(begin).trim());
			}
		}

		return trim(tmp);
	}
	/**
	 * 提取原始字符串nodeValue中suffix以前的子串
	 * 
	 * @param nodeValue
	 * @param prefix
	 * @return
	 */
	public static String stripBefore(String nodeValue, String suffix) {
		if (isEmpty(nodeValue))
			return "";

		String tmp = nodeValue.trim();
		int end = tmp.length();
		if (!isEmpty(suffix)) {
			int index = tmp.indexOf(suffix);
			if (index >= 0) {
				end = index;
				return trim(tmp.substring(0, end).trim());
			}
		}

		return trim(nodeValue);
	}
	/**
	 * 切分空格字符串
	 * 
	 * @return
	 */
	public static String[] splitBlankStr(String source) {
		if (isEmpty(source))
			return null;
		if (source.indexOf("　") >= 0)
			source = source.replace("　", " ");
		source = trim(source);
		return source.split(" ");
	}
	
	/**
	 * 在字符串nodeValue中找"prefix"以后，"suffix"以前的字符串<br/>
	 * 如果prefix为空，则找end以前的字符串；如果suffix为空，则找before以后的字符串
	 * 
	 * @param nodeValue
	 * @param prefix
	 * @param suffix
	 * @return
	 */
	public static String strip(String nodeValue, String prefix, String suffix) {
		if (isEmpty(nodeValue))
			return "";

		String tmp = nodeValue.trim();
		int begin = 0;
		int end = tmp.length();

		if (!isEmpty(prefix)) {
			int index = tmp.indexOf(prefix);
			if (index >= 0)
				begin = index + prefix.length();
			else
				return ""; // 不包含前缀
		}

		if (!isEmpty(suffix)) {
			int index = tmp.indexOf(suffix, begin);
			if (index > 0)
				end = index;
			else
				return ""; // 不包含后缀
		}

		return trim(end > begin ? tmp.substring(begin, end).trim() : tmp);
	}
	
	/**
	 * 截取字符串，从开始索引到结束索引
	 * @param nodeValue 字符串
	 * @param start 开始索引
	 * @param end 结束索引
	 * @param length 所需要限制长度
	 * @return
	 */
	public static String subStrip(String nodeValue, int start, int end){
		if(isEmpty(nodeValue)){
			return "";
		}else{
			if(nodeValue.length() > end){
				return nodeValue.substring(start, end);
			}else{
				return nodeValue;
			}
		}
	}
	
	public static String encodeUTF8(String text){
		if(notEmpty(text)){
			try {
				return URLEncoder.encode(text, "utf-8");
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		}else{
			return "";
		}
	}
	
	public static String decodeUTF8(String text){
		if(notEmpty(text)){
			try {
				return URLDecoder.decode(text, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return ""; 
	}
	
	/**
	 * 根据类型，推算时间
	 * 
	 * @param type
	 *            0:天数，1:月数
	 * @param number
	 *            数量
	 * @param asc
	 *            升序
	 * @param now
	 *            当前时间
	 * @return
	 */
	public static Date getNeedDate(int type, int number, boolean asc, Date now) {
		Date fromDate = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int date = cal.get(Calendar.DATE);
		if (asc) {
			if (type == 0) {
				date = date + number + 1;
			} else if (type == 1) {
				month = month + number;
			}
		} else {
			if (type == 0) {
				date = date - number - 1;

			} else if (type == 1) {
				month = month + number;
			}
		}
		cal.set(year, month, date);
		fromDate = cal.getTime();
		return fromDate;
	}
	
	/**
	 * 判断字符串是否是A~Z中的一个
	 * @param str
	 * @return true:是字母 false:不是字母
	 */
	public static boolean isChar(String str) {
		if (str.length() == 1 && str.charAt(0) >= 'A' && str.charAt(0) <= 'Z') {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 正则匹配获取字符串
	 * @param source 来源
	 * @param regex 正则
	 * @return String
	 */
	public static String regexText(String source, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		boolean b = matcher.find();
		if (b) {
			return matcher.group();
		}
		return null;
	}
	
	public static String regexText(String source, String regex, int group) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		boolean b = matcher.find();
		if (b) {
			return matcher.group(group);
		}
		return null;
	}
	
	/**
	 * 清空特殊符号
	 * @param text 字符串
	 * @return String
	 */
	public static String clearFuHao(String text){
		if(CommonUtil.notEmpty(text)){
			String regex = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
			Pattern p = Pattern.compile(regex);     
	        Matcher m = p.matcher(text);     
	        return m.replaceAll("").trim();
		}
		return "";
	}
	
	/**
	 * 文本字符串是否纯数字
	 * @param text 字符串
	 * @return Boolean
	 */
	public static boolean isNumber(String text){
		if(CommonUtil.notEmpty(text)){
			return text.matches("[0-9]+");
		}
		return false;
	}
	
	/**
	 * 文本字符串是否纯英文
	 * @param text 字符串
	 * @return Boolean
	 */
	public static boolean isAlpha(String text){
		if(CommonUtil.notEmpty(text)){
			return text.matches("[a-zA-Z]+");
		}
		return false;
	}
	
	/**
	 * 文本字符串是否纯数字+字母+符号
	 * @param text 字符串
	 * @return Boolean
	 */
	public static boolean isNumberAndAlpha(String text){
		if(CommonUtil.notEmpty(text)){
			return text.matches("[\\w]+");
		}
		return false;
	}
	
	/**
	 * 文本字符是否纯中文
	 * @param text 字符串
	 * @return Boolean
	 */
	public static boolean isChinese(String text){
		if(CommonUtil.notEmpty(text)){
			return text.matches("[\u4e00-\u9fa5]+");
		}
		return false;
	}

	/**
	 * 切分关键词字符串为字符串集合
	 * @param keyword 关键词文本
	 * @return String[]
	 */
	public static String[] splitKeyword(String keyword){
		if(CommonUtil.notEmpty(keyword)){
			return keyword.split(",|，| |　|\\s");
		}
		return null;
	}
	
	/**
	 * 格式化double类型的值，去除结尾".0"
	 * @param value
	 * @return
	 */
	public static String formatDoubleAsInt(Double value){
		if(null == value) return "0";
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		String str = String.valueOf(nf.format(value));
		if(str.endsWith(".0")) str = str.substring(0, str.indexOf(".0"));
		return str;
	}
	
	/**
	 * 格式化float类型的值，去除结尾".0"
	 * @param value
	 * @return
	 */
	public static String formatFloatAsInt(Float value){
		if(null == value) return "0";
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		String str = String.valueOf(nf.format(value));
		if(str.endsWith(".0")) str = str.substring(0, str.indexOf(".0"));
		return str;
	}
	
	/**
	 * 格式化double类型的值，等于0时设置为空字符串
	 * @param value
	 * @return
	 */
	public static String formatDoubleAsString(Double value){
		if(null == value) return "";
		String str = formatDoubleAsInt(value);
		if(str.equals("0")) return "";
		else return str;
	}
	
	/**
	 * 格式化int类型值，等于0时设置为空字符串
	 * @param value
	 * @return
	 */
	public static String formatIntAsString(Integer value){
		if(null == value) return "";
		if(0 == value) return "";
		else return String.valueOf(value);
	}
	/**  
	 * @Title: processStockCode
	 * @Description: 格式化股票代码
	 * @param: String 
	 * @return: String
	 */
	public static String processStockCode(String stockCode){
		if(stockCode!=null && !"".equals(stockCode)&&stockCode.length()>=6 && (!stockCode.startsWith("sh")||!stockCode.startsWith("sz"))){
			stockCode = stockCode.substring(stockCode.length()-6, stockCode.length());
		}
		return stockCode;
	}
	/**  
	 * @Title: formatStockCode
	 * @Description: 格式化股票代码
	 * @param: String 
	 * @return: String
	 */
	public static String formatStockCode(String stockCode){
		if(stockCode!=null && !"".equals(stockCode)&&stockCode.length()>=6 && (!stockCode.startsWith("sh")||!stockCode.startsWith("sz"))){
			String sc = stockCode.substring(stockCode.length()-6, stockCode.length());
			if(sc.startsWith("6")){
				stockCode = "sh" + sc;
			}
			if(sc.startsWith("0")||sc.startsWith("3")){
				stockCode = "sz" + sc;
			}
		}
		return stockCode;
	}
	/**
	 * 格式化int类型值，等于0时设置为空字符串
	 * @param value
	 * @return
	 */
	public static StockCurrentPrice getCurrentPriceByStockCode(String stockCode)
	{
		String url = "http://hq.sinajs.cn/list=s_"+stockCode;
		Document doc = null;
		try {
			doc = JsoupParse.getDocument(url, "gb2312");
			String data = doc.body().html();
			if(data != null)
			{
				String[] twoPart = data.split("=");
				if(twoPart==null || twoPart.length!=2 || twoPart[1].length()<5)
				{
					return null;
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
				return sc;
			}
			else
			{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**  
     * 生成主键(16位数字) 
     * 主键生成方式,年月日时分秒毫秒的时间戳+四位随机数保证不重复 
     */    
    public static String getGenerateId() {  
        //当前系统时间戳精确到毫秒  
        Long simple=System.currentTimeMillis();  
        //三位随机数  
        int random=new Random().nextInt(900)+100;//为变量赋随机值100-999;  
        return simple.toString()+random;
    }
    /**
	 * 获得一个double数值的 在允许上下浮动固定数值范围内的数组
	 * @param doubleValue 原始值
	 * @param numerical 允许上下浮动的数值 ±x
	 * @return double[] double[0] 最小值 double[1] 最大值
	 */
	public static double[] doubleUpDownByNumerical(double doubleValue,double numerical){
		double up = doubleValue + numerical;
		double down = doubleValue - numerical;
		double[] array = new double[2];
		array[0] = down;
		array[1] = up;
		return array;
	}
	/**
	 * 获得一个double数值的 在允许上下浮动百分比范围内的数组
	 * @param doubleValue 原始值
	 * @param percentage 允许上下浮动的百分比 0.8 or 1.2 or ...
	 * @return double[] double[0] 最小值 double[1] 最大值
	 */
	public static double[] doubleUpDwonByPercentage(double doubleValue,double percentage){
		double up = doubleValue*(1+percentage);
		double down = doubleValue*(1-percentage);
		double[] array = new double[2];
		array[0] = down;
		array[1] = up;
		return array;
	}
	
	/**
	 * 获得区间bDate和eDate的交易记录
	 * @param stockCd
	 * @param bDate
	 * @return eDate
	 */
	public static List<StocksInfoEntity> getStockDealInfo(String stockCd, String bDate, String eDate, boolean flag)
			throws Exception {
		List<StocksInfoEntity> dealInfo =  new ArrayList<StocksInfoEntity>();
		//如果股票代码为空，就返回
		if(stockCd==null)
		{
			return dealInfo;
		}
		try
		{
			StocksInfoEntity se= null;
			String stockStr = null;
			
			//获取今天的数据
			if(!stockCd.startsWith("sh") && !stockCd.startsWith("sz"))
			{
				stockStr = CommonUtil.formatStockCode(stockCd);
			}
			//获取当前日期
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String cDate = sf.format(cal.getTime());
			
			StockCurrentPrice cPrice = getCurrentPriceByStockCode(stockStr);
			if(cPrice != null)
			{
				se = new StocksInfoEntity();
				se.setDateStr(cDate);
				se.setOpenPrice(0);
				se.setClosePrice(0);
				se.setUpDownMoney(cPrice.getStockUpdownPrice());
				se.setUpDownMoneyPercent(cPrice.getStockUpdownPercent());
				se.setLowestPrice(0);
				se.setHighestPrice(0);
				se.setDealMount(cPrice.getStockDealMount());
				se.setDealMoney(0);
				se.setDealPercent(0);
				
				//根据传进来的flag确定是否排除异常值
				if(flag == true)
				{
					if(Math.abs(se.getUpDownMoneyPercent())<9)
					{
						dealInfo.add(se);
					}
				}
				else
				{
					dealInfo.add(se);
				}
			}
			
			//获取过去bDate和eDate之间的数据
			String url = "http://q.stock.sohu.com/hisHq?code=cn_stockcd&start=bdate&end=edate&order=D&period=d";
			url = url.replace("stockcd", stockCd);
			url = url.replace("bdate", bDate);
			url = url.replace("edate", eDate);
			Document doc = JsoupParse.getDocument(url, "utf-8");
			if(doc!=null && doc.data()!=null)
			{
				if("{}".equalsIgnoreCase(doc.body().text()))
				{
					return dealInfo;
				}
				JSONArray jsonArray = JSONObject.parseArray(doc.body().text());
				JSONObject object = jsonArray.getJSONObject(0);
				JSONArray hq = object.getJSONArray("hq");
				JSONArray row = null;
				for(int i=0; i<hq.size(); i++)
				{
					se = new StocksInfoEntity();
					row = hq.getJSONArray(i);
					se.setDateStr(row.getString(0));
					se.setOpenPrice(row.getDoubleValue(1));
					se.setClosePrice(row.getDoubleValue(2));
					se.setUpDownMoney(row.getDoubleValue(3));
					String upDownPercent = row.getString(4);
					upDownPercent = upDownPercent.substring(0, upDownPercent.length()-1);
										
					se.setUpDownMoneyPercent(Double.parseDouble(upDownPercent));
					se.setLowestPrice(row.getDoubleValue(5));
					se.setHighestPrice(row.getDoubleValue(6));
					se.setDealMount(row.getDoubleValue(7));
					se.setDealMoney(row.getDoubleValue(8));
					String change = row.getString(9);
					change = change.substring(0, change.length()-1);
					double dChange = Double.parseDouble(change);
					se.setDealPercent(dChange);
					//根据传进来的flag确定是否排除异常值
					if(flag == true)
					{
						if(Math.abs(se.getUpDownMoneyPercent())<9)
						{
							dealInfo.add(se);
						}
					}
					else
					{
						dealInfo.add(se);
					}
					
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return dealInfo;
	}
}
