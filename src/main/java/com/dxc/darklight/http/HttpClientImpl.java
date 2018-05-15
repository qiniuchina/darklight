package com.dxc.darklight.http;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.dxc.darklight.conf.ConfFactory;
import com.dxc.darklight.util.CommonUtil;

/**
 * 基于org.apache.httpClient的Http实现类
 * 
 * @author fei
 */
@SuppressWarnings("deprecation")
public class HttpClientImpl implements Http {
	{
		httpParams = new BasicHttpParams();
		httpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		HttpConnectionParams.setConnectionTimeout(httpParams, 60000);
		HttpConnectionParams.setSoTimeout(httpParams, 60000);
		ConnManagerParams.setMaxTotalConnections(httpParams, 500);
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setUserAgent(httpParams, "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; EmbeddedWB 14.52 from: http://www.bsalsa.com/ EmbeddedWB 14.52; .NET CLR 2.0.50727)");

 String proxyHost = ConfFactory.getConf().get("http.proxy.host");
		if (CommonUtil.notEmpty(proxyHost)) {
			try {
				int proxyPort = ConfFactory.getConf().getInt("http.proxy.port",8080);
				HttpHost proxy = new HttpHost(proxyHost, proxyPort);
				httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", PlainSocketFactory.getSocketFactory(), 443));

		// 如果抓取HTTPS，放开此语句
		// schemeRegistry.register(new Scheme("https",
		// SSLSocketFactory.getSocketFactory(), 443));

		connectionManager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

	}
	private static ThreadSafeClientConnManager connectionManager;
	private static BasicHttpParams httpParams;
	/** httpclient默认对象*/
	private DefaultHttpClient client = new DefaultHttpClient(connectionManager,httpParams);
	/** 链接地址*/
	private String url;
	/** 头部信息*/
	private Map<String,String> headers = new HashMap<String,String>();
	/** cookie信息*/
	private Map<String,String> cookies = new HashMap<String,String>();
	/** 参数*/
	private Map<String,String> parameters = new HashMap<String,String>();
	/** 会话信息*/
	private BasicHttpContext basicHttpContext = new BasicHttpContext();
	/** 请求编码*/
	private String requestEncoding = "UTF-8";
	/** 是否保存cookie*/
	private boolean isSaveCookie = false;
	/** POST*/
	private HttpPost post = null;
	/** GET*/
	private HttpGet get = null;
	/** Host*/
	private HttpHost host = null;
	/** 文件*/
	private File file;
	/** 文件名*/
	private String fileName;

	public String getRequestEncoding() {
		return requestEncoding;
	}
	
	@Override
	public void setRequestEncoding(String requestEncoding) {
		this.requestEncoding = requestEncoding;
	}

	public DefaultHttpClient getClient() {
		return client;
	}

	public String getUrl() {
		return url;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}
	
	public boolean isSaveCookie() {
		return isSaveCookie;
	}

	public void setSaveCookie(boolean isSaveCookie) {
		this.isSaveCookie = isSaveCookie;
	}
	
	public HttpPost getPost() {
		return post;
	}

	public void setPost(HttpPost post) {
		this.post = post;
	}

	public HttpGet getGet() {
		return get;
	}

	public void setGet(HttpGet get) {
		this.get = get;
	}

	public void setClient(DefaultHttpClient client) {
		this.client = client;
	}
	
	public File getFile() {
		return file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** 
	 * get请求获取结果对象
	 */
	@Override
	public FetchResult get(String url) throws Exception {
		this.setUrl(url);
		return this.get();
	}
	/**
	 * 设置头部信息
	 * @param map
	 */
	@Override
	public void setHeaders(Map<String, String> map) {
		if(map != null && !map.isEmpty()){
			headers.putAll(map);
		}
	}

	@Override
	public void setCookies(Map<String, String> map) {
		if(map != null && !map.isEmpty()){
			cookies.putAll(map);
		}
		
	}

	@Override
	public void setBasicHttpContext(BasicHttpContext basicHttpContext) {
		this.basicHttpContext = basicHttpContext;
	}
	@Override
	public BasicHttpContext getBasicHttpContext() {
		return basicHttpContext;
	}

	@Override
	public void setParameters(Map<String, String> map) {
		if(map != null && !map.isEmpty()){
			parameters.putAll(map);
		}
	}
	/**
	 * 设置请求链接
	 * 
	 * @param url
	 */
	@Override
	public void setUrl(String url) {
		this.url = url;
	}
	/** 
	 * get请求获取结果对象
	 */
	@Override
	public FetchResult get() throws Exception {
		if (url == null) {
			throw new NullPointerException();
		}
		if (url.equals("") || (!url.startsWith("http") && !url.startsWith("https"))) {
			throw new Exception("the url isn't http request!");
		}
		get = new HttpGet(url);
		/** 判断是否自动提交cookie*/
		isCustomCookie();
		/** 添加头部信息*/
		addHeaders(get);
		/** 添加参数信息*/
		addParameters(get,url);
		/** 初始化Host*/
		initHost();
		/** 添加响应拦截器*/
		addResponseInterceptor();
		/** 执行请求*/
		HttpResponse response = execute(host,get);
		/** 恢复cookie兼容模式*/
		resumeCookiesJR();
		/** 保存会话*/
		saveContext();
		return responseToFetchResult(response);
	}
	
	/**
	 * 关闭连接
	 */
	@Override
	public void shutDown() {
		client.getConnectionManager().shutdown();
	}
	/**
	 * 执行http请求
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private HttpResponse execute(HttpHost host,HttpUriRequest request) throws Exception{
		return client.execute(host,request, basicHttpContext);
	}
	/**
	 * 判断是否要自己提交cookie
	 */
	private void isCustomCookie(){
		if(cookies != null && !cookies.isEmpty()){
			String cookiePolicy = CookiePolicy.BROWSER_COMPATIBILITY;
			cookiePolicy = CookiePolicy.IGNORE_COOKIES;
			HttpClientParams.setCookiePolicy(client.getParams(), cookiePolicy);
		}
	}
	/**
	 * 添加头部信息
	 * @param request HttpRequest 请求对象可以是HttpGet 也可以是HttpPost
	 */
	private void addHeaders(HttpRequest request){
		if (headers != null && !headers.isEmpty()) {
			Iterator<String> it = headers.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				String value = headers.get(key);
				request.addHeader(key,value);
			}
		}
	}
	/**
	 * 添加参数  主要针对HttpGet 和 HttpPost , post的方式目前只支持最普通的post
	 * @param request 请求对象implements HttpRequest
	 * @param url URL请求地址
	 * @throws Exception
	 */
	private void addParameters(HttpRequest request,String url) throws Exception{
		if(request instanceof HttpGet){
			if(parameters != null && !parameters.isEmpty()){
				Iterator<String> it = parameters.keySet().iterator();
				int i = 0;
				while(it.hasNext()){
					String key = it.next();
					String value = parameters.get(key);
					if(i == 0){
						url += "?" + key + "=" + URLEncoder.encode(value,getRequestEncoding());
					}else{
						url += "&" + key + "=" + URLEncoder.encode(value,getRequestEncoding());
					}
					i++;
				}
				this.setGet(new HttpGet(url));
			}
		}else if(request instanceof HttpPost){
			if(parameters != null && !parameters.isEmpty()){
				Iterator<String> it = parameters.keySet().iterator();
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				while(it.hasNext()){
					String key = it.next();
					String value = parameters.get(key);
					nvps.add(new BasicNameValuePair(key, value));
				}
				((HttpPost) request).setEntity(new UrlEncodedFormEntity(nvps, getRequestEncoding()));
			}
		}
	}
	private void addResponseInterceptor(){
		client.addResponseInterceptor(new HttpResponseInterceptor() {

			public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
				HttpEntity entity = response.getEntity();
				Header ceheader = entity.getContentEncoding();
				if (ceheader != null) {
					HeaderElement[] codecs = ceheader.getElements();
					for (int i = 0; i < codecs.length; i++) {
						if (codecs[i].getName().equalsIgnoreCase("gzip") || codecs[i].getName().equalsIgnoreCase("x-gzip")) {
							response.setEntity(new GzipDecompressingEntity(response.getEntity()));
							return;
						} else if (codecs[i].getName().equalsIgnoreCase("deflate")) {
							response.setEntity(new InflaterDecompressingEntity(response.getEntity()));
							return;
						}
					}
				}
			}

		});
	}
	/**
	 * 恢复cookie兼容模式
	 */
	private void resumeCookiesJR(){
		// 恢复浏览器兼容的cookie模式
		HttpClientParams.setCookiePolicy(client.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
	}
	/**
	 * 保存上下文
	 */
	private void saveContext(){
		// 保存http会话
		if(isSaveCookie){
			CookieStore cookieStore = client.getCookieStore();
			basicHttpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		}
	}
	/**
	 * httpClient响应结果Httpresponse转为FetchResult
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private FetchResult responseToFetchResult(HttpResponse response) throws Exception{
		HttpEntity entity = response.getEntity();
		/** 内容结果*/
		byte[] contentBytes = EntityUtils.toByteArray(entity);
		String encoding = EntityUtils.getContentCharSet(entity);
		/** 头部信息*/
		Header[] headers = response.getAllHeaders();
		if(headers != null && headers.length > 0){
			for(Header head:headers){
				String key = head.getName();
				String value = head.getValue();
				this.headers.put(key, value);
			}
		}
		StatusLine sl = response.getStatusLine();
		/** 响应码*/
		int code = sl.getStatusCode();
		/** 响应状态信息*/
		String statusLine = sl.getReasonPhrase();
		/** cookies*/
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
		fr.setUrl(this.url);
		fr.setHeaders(this.headers);
		fr.setHttpCode(code);
		fr.setStatusLine(statusLine);
		fr.setCookies(cookies);
		fr.setEncoding(encoding==null?requestEncoding:encoding);
		return fr;
	}
	/**
	 * post提交
	 */
	@Override
	public FetchResult post() throws Exception{
		post = new HttpPost(this.url);
		/** 判断是否自动提交cookie*/
		isCustomCookie();
		/** 添加头部信息*/
		addHeaders(post);
		if(this.getFile() != null){
			if(fileName != null){
				MultipartEntity entity = new MultipartEntity();
				FileBody fileBody = new FileBody(file);
				entity.addPart(fileName, fileBody);
				Iterator<String> itr = parameters.keySet().iterator();
				while(itr.hasNext()){
					String key = itr.next();
					String value = parameters.get(key);
					StringBody stringBody = new StringBody(value, Charset.forName(requestEncoding));
					entity.addPart(key, stringBody);
				}
				post.setEntity(entity);
			}else{
				HttpEntity entity = new FileEntity(file);
				post.setEntity(entity);
			}
		}else{
			/** 添加参数信息*/
			addParameters(post,url);
		}
		/** 初始化Host*/
		initHost();
		/** 添加响应拦截器*/
		addResponseInterceptor();
		/** 执行请求*/
		HttpResponse response = execute(host,post);
		/** 恢复cookie兼容模式*/
		resumeCookiesJR();
		/** 保存会话*/
		saveContext();
		return responseToFetchResult(response);
	}
	
	private void initHost(){
		try {
			URL u = new URL(url);
			host = new HttpHost(u.getHost(),u.getPort(),u.getProtocol());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void abort() {
		if(get != null){
			get.abort();
		}
		if(post != null){
			post.abort();
		}
	}

	@Override
	public void setFile(File file) {
		this.file = file;
	}
}
