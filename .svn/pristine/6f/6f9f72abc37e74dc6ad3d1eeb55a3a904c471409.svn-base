package com.dxc.darklight.datasource.solr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dxc.darklight.util.CommonUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

public class SolrUtil {

	/**
	 * 获取solr价格段拼接的字符串
	 * @param from_price 起始价格
	 * @param to_price 结束价格
	 * @return String
	 */
	public String toStringPrice(int from_price, int to_price){
		if(from_price > 0 && to_price > 0) return " AND price:[" + from_price + " TO " + to_price + "]";
		else if(from_price > 0 && to_price <= 0) return " AND price:[" + from_price + " TO *]";
		else if(from_price <= 0 && to_price > 0) return " AND price:[0 TO "+ to_price +"]";
		else return "";
	}
	
	/**
	 * 获取solr面积段拼接的字符串，主要应用在供方的房源或合作信息
	 * @param from_square 起始面积
	 * @param to_square 结束面积
	 * @return String
	 */
	public String toStringSquare(int from_square, int to_square){
		if(from_square > 0 && to_square > 0) return " AND square:[" + from_square + " TO " + to_square + "]";
		else if(from_square > 0 && to_square <= 0) return " AND square:[" + from_square + " TO *]";
		else if(from_square <= 0 && to_square > 0) return " AND square:[0 TO "+ to_square +"]";
		else return "";
	}
	
	/**
	 * 获取solr均价段拼接的字符串
	 * @param from_average 起始均价
	 * @param to_average 结束均价
	 * @return String
	 */
	public String toStringAverage(double from_average, double to_average){
		if(from_average > 0 && to_average > 0) return " AND average:[" + from_average + " TO " + to_average + "]";
		else if(from_average > 0 && to_average <= 0) return " AND average:[" + from_average + " TO *]";
		else if(from_average <= 0 && to_average > 0) return " AND average:[0 TO "+ to_average +"]";
		else return "";
	}
	
	/**
	 * 获取solr时间段拼接的字符串
	 * @param from_fetchDate 起始时间
	 * @param to_fetchDate 结束时间
	 * @return String
	 */
	public String toStringFetchDate(Date from_fetchDate, Date to_fetchDate) {
		int from_days = 0;
		int to_days = 0;
		if (from_fetchDate != null && to_fetchDate != null) {
			if (from_fetchDate.getTime() > to_fetchDate.getTime()) {
				Date temp_fetchDate = to_fetchDate;
				to_fetchDate = from_fetchDate;
				from_fetchDate = temp_fetchDate;
			}
			from_days = countDays(from_fetchDate);
			to_days = countDays(to_fetchDate);
		}else if (from_fetchDate != null && to_fetchDate == null){
			from_days = countDays(from_fetchDate);
		}else if(from_fetchDate == null && to_fetchDate != null){
			to_days = countDays(to_fetchDate);
		}
		if(from_days > 0 && to_days > 0) return " AND fetchDate:{NOW/DAY-" + from_days +"DAY TO NOW/DAY-" + to_days + "DAY}";
		else if(from_days > 0 && to_days == 0) return " AND fetchDate:{NOW/DAY-" + from_days +"DAY TO *}";
		else if(from_days == 0 && to_days > 0) return " AND fetchDate:{* TO NOW/DAY-" + to_days + "DAY}";
		else return "";
	}

	/**
	 * 切分中文分词，返回集合
	 * @param word 词
	 * @return List<String>
	 */
	public static List<String> parseKeyword(String word){
		List<Term> tokens = HanLP.segment(word);//IndexTokenizer.segment(word);
		
		List<String> words = new ArrayList<String>();
		for(Term term : tokens) {
			words.add(term.word);
		}
		return words;
	}
	
	/**
	 * 获取solr关键字拼接的字符串
	 * @param keyword 关键字
	 * @param fieldName 字段名称
	 * @return String
	 */
	public String toStringKeyword(String keyword, String fieldName){
		if(CommonUtil.notEmpty(keyword)){
			List<String> words = new ArrayList<String>();
			
			String input = keyword.replaceAll("　| ", "\\s");
			String[] words1 = input.split("\\s");
			if(words1.length > 1) {
				for(String word : words1) if(CommonUtil.notEmpty(word)) words.add(word);
			} else {
				words = parseKeyword(keyword);
			}

			if(words.size() > 1){
                String temp = "";
                for(String word : words){
                	if(CommonUtil.notEmpty(word)){
                		if(temp.equals("")){
                        	temp += " ("+ fieldName +":" + word;
                        }
                        else{
                        	temp += " AND " +fieldName +":" + word;
                        }
                	}
                }
                temp += ")";
                return " AND " + temp;
            }else{
                return " AND " + fieldName + ":" + words.get(0);
            }
		}else{
			return "";
		}
	}
	
	/**
	 * 获取solr关键字拼接的字符串
	 * @param keyword 关键字
	 * @param fieldName 字段名称
	 * @return String
	 */
	public String toStringKeyword(String keyword, String fieldName,String fuhao){
		if(CommonUtil.notEmpty(keyword)){
			List<String> words = new ArrayList<String>();
			
			String input = keyword.replaceAll("　| ", "\\s");
			String[] words1 = input.split("\\s");
			if(words1.length > 1) {
				for(String word : words1) if(CommonUtil.notEmpty(word)) words.add(word);
			} else {
				words = parseKeyword(keyword);
			}

			if(words.size() > 1){
                String temp = "";
                for(String word : words){
                	if(CommonUtil.notEmpty(word)){
                		if(temp.equals("")){
                        	temp += " ("+ fieldName +":" + word;
                        }
                        else{
                        	temp += " "+fuhao+" " +fieldName +":" + word;
                        }
                	}
                }
                temp += ")";
                return " AND " + temp;
            }else{
                return " AND " + fieldName + ":" + words.get(0);
            }
		}else{
			return "";
		}
	}
	
	/**
	 * 整型数组字段拼接Solr条件字符串
	 * @param fieldName 字段名称
	 * @param array 整型数组
	 * @return String
	 */
	public String toStringIntegerArray(String fieldName, List<Integer> array){
		String tempString = "";
		if(CommonUtil.notEmpty(fieldName) && null != array && array.size() > 0){
			if(array.size() == 1) {
				tempString = " AND " + fieldName + ":" + array.get(0);
			}else {
				tempString = " AND (";
				for(int i=0; i<array.size(); i++){
					if(i == 0) tempString += fieldName + ":" + array.get(i);
					else tempString += " OR " + fieldName + ":" + array.get(i);
				}
				tempString += ")";
			}
		}
		return tempString;
	}
	/**
	 * 计算某个时间与当前时间间隔天数
	 * @param date 时间
	 * @return Integer
	 */
	private int countDays(Date date) {
		Calendar cal = Calendar.getInstance();
		int now = cal.get(Calendar.DAY_OF_YEAR);
		cal.setTime(date);
		int before = cal.get(Calendar.DAY_OF_YEAR);
		return (now - before);
	}
}
