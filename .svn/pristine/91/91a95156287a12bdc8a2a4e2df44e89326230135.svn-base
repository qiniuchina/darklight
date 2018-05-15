package com.dxc.darklight.weixin.token;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.dxc.darklight.weixin.App;
import com.google.gson.Gson;

/**
 * 通过用户认证
 * https://api.weixin.qq.com/sns/oauth2/access_token
 * @author fei
 */
public class TokenWebService {
	
	private static final String GETTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
	
	private static final String VALIDATETOKEN = "https://api.weixin.qq.com/sns/auth";
	
	private static final String REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	
	
	private static final String TOKEN_STR_KEY = "access_token";
	
	private static final String TOKEN_STR_REFRESH = "refresh_token";
	
	private static final String OPEN_ID = "openid";
	
	private static final String LAST_DATE = "lastdate";
	
	private static final String USER_WXTOKEN = "user_wxtoken";
	
	
	/**
	 * 
	 * @param appid
	 * @param appsecret
	 * @param code
	 * @return
	 */
	public static TokenWeb getNewTokenWebObject(String code){
		String str = getNewTokenWeb(App.APP_ID, App.APP_SECRET, code);
		return strToNewTokenWeb(str);
	}
	
	/**
	 * 获取一个tokenweb json Str
	 * @return
	 */
	public static String getNewTokenWeb(String appid,String appsecret,String code) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(GETTOKEN + "?appid="+appid+"&secret="+appsecret+"&code="+code+"&grant_type=authorization_code");
		try {

			URL u = new URL(GETTOKEN);
			HttpHost host = new HttpHost(u.getHost(), u.getPort(), u.getProtocol());
			HttpResponse response = httpclient.execute(host, httpget);
			HttpEntity entity = response.getEntity();
			/** 内容结果 */
			byte[] contentBytes = EntityUtils.toByteArray(entity);
			String content = new String(contentBytes, "utf-8");
			return content;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpget.abort();
			httpclient.getConnectionManager().shutdown();
		}
		return null;
	}
	/**
	 * 新的tokenWeb
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TokenWeb strToNewTokenWeb(String str){
		Map<String,Object> map = new Gson().fromJson(str, Map.class);
		String tokenStr = (String)map.get(TOKEN_STR_KEY);
		String refreshToken = (String)map.get(TOKEN_STR_REFRESH);
		String openId = (String)map.get(OPEN_ID);
		TokenWeb tokenweb = new TokenWeb();
		tokenweb.setTokenStr(tokenStr);
		tokenweb.setRefreshToken(refreshToken);
		tokenweb.setDate(new Date());
		tokenweb.setOpenId(openId);
		return tokenweb;
	}
	
	/**
	 * tokenWeb对象转换成json
	 * @param tokenWeb 
	 * @return String
	 */
	public static String tokenWebToStr(TokenWeb tokenWeb){
		Map<String,Object> map = new HashMap<String,Object>();
		Date date = tokenWeb.getDate();
		String tokenStr = tokenWeb.getTokenStr();
		String refreshTokenStr = tokenWeb.getRefreshToken();
		String openId = tokenWeb.getOpenId();
		map.put(TOKEN_STR_KEY, tokenStr);
		map.put(TOKEN_STR_REFRESH, refreshTokenStr);
		map.put(OPEN_ID, openId);
		map.put(LAST_DATE, date.getTime());
		String value = new Gson().toJson(map);
		return value;
	}
	
	/**
	 * 新TokenWeb的json
	 * @param tokenWebstr tokenweb,json字符串
	 * @return TokenWeb
	 */
	@SuppressWarnings("unchecked")
	public static TokenWeb strToTokenWeb(String tokenWebstr){
		Map<String,Object> map = new Gson().fromJson(tokenWebstr, Map.class);
		long lastDate = ((Double)map.get(LAST_DATE)).longValue();
		Date date = new Date(lastDate);
		String tokenStr = (String)map.get(TOKEN_STR_KEY);
		String refreshTokenStr = (String)map.get(TOKEN_STR_REFRESH);
		String openId = (String)map.get(OPEN_ID);
		TokenWeb tokenWeb = new TokenWeb();
		tokenWeb.setDate(date);
		tokenWeb.setRefreshToken(refreshTokenStr);
		tokenWeb.setTokenStr(tokenStr);
		tokenWeb.setOpenId(openId);
		return tokenWeb;
	}
	
	/**
	 * 验证tokenweb是否有效
	 * @param tokenWeb
	 * @return true 有效 false 无效
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isValid(TokenWeb tokenWeb){
		//https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(VALIDATETOKEN + "?access_token="+tokenWeb.getTokenStr()+"&openid="+tokenWeb.getOpenId());
		try {

			URL u = new URL(VALIDATETOKEN);
			HttpHost host = new HttpHost(u.getHost(), u.getPort(), u.getProtocol());
			HttpResponse response = httpclient.execute(host, httpget);
			HttpEntity entity = response.getEntity();
			/** 内容结果 */
			byte[] contentBytes = EntityUtils.toByteArray(entity);
			String content = new String(contentBytes, "utf-8");
			Map map = new Gson().fromJson(content, Map.class);
			int code = ((Double)map.get("errcode")).intValue();
			if(code != 0){
				return false;
			}else{
				return true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpget.abort();
			httpclient.getConnectionManager().shutdown();
		}
		return false;
	}
	
	/**
	 * 用refresh_token刷新
	 * @param appid
	 * @param tokenWeb
	 * @return
	 */
	//https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
	public static String getRefreshToken(String appid,TokenWeb tokenWeb){
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(REFRESH_TOKEN + "?appid="+appid+"&refresh_token="+tokenWeb.getRefreshToken()+"&grant_type=refresh_token");
		try {

			URL u = new URL(REFRESH_TOKEN);
			HttpHost host = new HttpHost(u.getHost(), u.getPort(), u.getProtocol());
			HttpResponse response = httpclient.execute(host, httpget);
			HttpEntity entity = response.getEntity();
			/** 内容结果 */
			byte[] contentBytes = EntityUtils.toByteArray(entity);
			String content = new String(contentBytes, "utf-8");
			return content;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpget.abort();
			httpclient.getConnectionManager().shutdown();
		}
		return null;
	}
	/**
	 * 获得有一个效果的tokenWeb
	 * @param appid
	 * @param openId
	 * @return
	 */
	public static TokenWeb getValidTokenWeb(String appid,String openId){
		TokenWeb tokenWeb = TokenWebService.getfromRedis(openId);
		//缓存中没有 该openId 的tokenweb信息 需要 用code 获取
		if(tokenWeb == null) return null;
		boolean isValid = TokenWebService.isValid(tokenWeb);
		//查看tokenweb是否有效
		if(!isValid){
			//如果无效 尝试刷新一下
			TokenWebService.getRefreshToken(appid, tokenWeb);
			//刷新后还是无效，那就真是过期好久了
			//由于access_token拥有较短的有效期，当access_token超时后，
			//可以使用refresh_token进行刷新，refresh_token拥有较长的有效期（7天、30天、60天、90天），
			//当refresh_token失效的后，需要用户重新授权。 
			if(!TokenWebService.isValid(tokenWeb)) return null;
		}
		return tokenWeb;
	}
	/**
	 * 保存
	 * @param tokenweb
	 */
	public static void saveTokenWeb(TokenWeb tokenweb){
//		if(tokenweb.getOpenId() == null || "".equals(tokenweb.getOpenId())) return;
//		String redisStr = tokenWebToStr(tokenweb);
//		RedisCache rc = new RedisCache();
//		rc.hset(USER_WXTOKEN, tokenweb.getOpenId(),redisStr);
	}
	
	/**
	 * 从redis中获取token
	 * @param appId
	 * @param appsecret
	 * @return
	 */
	public static TokenWeb getfromRedis(String openId){
//		RedisCache rc = new RedisCache();
//		String tokenstr = rc.hget(USER_WXTOKEN, openId);
//		if(tokenstr == null) return null;
//		TokenWeb tokenWeb = strToTokenWeb(tokenstr);
//		return tokenWeb;
		return null;
	}
}
