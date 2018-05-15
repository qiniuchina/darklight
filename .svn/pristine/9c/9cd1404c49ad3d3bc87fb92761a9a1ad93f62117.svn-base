package com.dxc.darklight.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class FetchResult {
	/** url地址*/
	private String url;
	/** 网页内容编码*/
	private String encoding;
	/** 网页内容，二进制 */
	private byte[] content;
	/** 网页http响应代码 */
	private int httpCode;
	/** http头部状态行 */
	private String statusLine;
	/** http响应头 */
	private Map<String, String> headers = new HashMap<String, String>();
	/** http响应的cookies*/
	private Map<String, String> cookies = new HashMap<String, String>();
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public int getHttpCode() {
		return httpCode;
	}
	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}
	public String getStatusLine() {
		return statusLine;
	}
	public void setStatusLine(String statusLine) {
		this.statusLine = statusLine;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public Map<String, String> getCookies() {
		return cookies;
	}
	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}
	
	/**
	 * httpClient响应结果Httpresponse转为FetchResult
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static FetchResult responseToFetchResult(String url,String encod,DefaultHttpClient client,HttpResponse response) throws Exception{
		HttpEntity entity = response.getEntity();
		/** 内容结果*/
		byte[] contentBytes = EntityUtils.toByteArray(entity);
		String encoding = EntityUtils.getContentCharSet(entity);
		/** 头部信息*/
		Map<String,String> headers = new HashMap<String,String>();
		Header[] hdader = response.getAllHeaders();
		if(hdader != null && hdader.length > 0){
			for(Header head:hdader){
				String key = head.getName();
				String value = head.getValue();
				headers.put(key, value);
			}
		}
		StatusLine sl = response.getStatusLine();
		/** 响应码*/
		int code = sl.getStatusCode();
		/** 响应状态信息*/
		String statusLine = sl.getReasonPhrase();
		/** cookies*/
		Map<String,String> cookies = new HashMap<String,String>();
		CookieStore cs = client.getCookieStore();
		List<Cookie> cookieList = cs.getCookies();
		if(cookieList != null && !cookieList.isEmpty()){
			for(Cookie cookie:cookieList){
				String key = cookie.getName();
				String value = cookie.getValue();
				cookies.put(key, value);
			}
		}
		/** 结果转型赋值*/
		FetchResult fr = new FetchResult();
		fr.setContent(contentBytes);
		fr.setUrl(url);
		fr.setHeaders(headers);
		fr.setHttpCode(code);
		fr.setStatusLine(statusLine);
		fr.setCookies(cookies);
		fr.setEncoding(encoding==null?encod:encoding);
		return fr;
	}
}
