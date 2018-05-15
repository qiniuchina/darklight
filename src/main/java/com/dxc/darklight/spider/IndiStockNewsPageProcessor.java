package com.dxc.darklight.spider;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dxc.darklight.util.DateUtils;

public class IndiStockNewsPageProcessor {

	public Date getNewsPubDateCj(String pubTimeStr) throws Exception {
		Pattern pattern = Pattern.compile("\\d*年\\d*月\\d*日\\s\\S*\\d\\b\\s");
		Matcher matcher = pattern.matcher(pubTimeStr);
		Date date = null;
		String pubDate = null;
		if (matcher.find()) {
			pubDate = matcher.group(0).replace("年", "").replace("月", "").replace("日", "").replace(" ", "").replace(":",
					"");
			date = DateUtils.parseYYYYMMddHHmmss(pubDate);
			// System.out.println(date);
		}
		return date;
	}

	public Date getNewsPubDateFin(String pubTimeStr) throws Exception {
		Pattern pattern = Pattern.compile("\\d*年\\d*月\\d*日\\S*\\s");
		Matcher matcher = pattern.matcher(pubTimeStr);
		Date date = null;
		String pubDate = null;
		if (matcher.find()) {
			pubDate = matcher.group(0).replace("年", "").replace("月", "").replace("日", "").replace(" ", "").replace(":",
					"");
			// 201704250644
			date = DateUtils.parseYYYYMMddHHmm(pubDate);
		}
		return date;
	}

	public static void main(String[] args) throws Exception {
		// getNewsPubDateCj("2017年03月29日 08:20:03 经济观察网");
	}

	public boolean compare(Date lastDate, Date pubDate) {
		try {
			Date dt1 = lastDate;// 数据库中的最新时间
			Date dt2 = pubDate;// 新闻列表中的最新时间
			if (dt1.getTime() < dt2.getTime()) {
				System.out.println("lastDate 在pubDate前");
				return true;
			} else if (dt1.getTime() > dt2.getTime()) {
				System.out.println("lastDate在pubDate后");
				return false;
			} else {
				return false;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}
}
