package com.dxc.darklight.http;

import java.io.File;
import java.util.Map;

import org.apache.http.protocol.BasicHttpContext;

/**
 * Http工具
 * @author fei
 *
 */
public interface Http {
	/** 
	 * get请求获取结果对象
	 */
	public FetchResult get(String url) throws Exception;
	/**
	 * 设置请求链接
	 * @param url
	 */
	public void setUrl(String url);
	/** 
	 * get请求获取结果对象
	 */
	public FetchResult get() throws Exception;
	/**
	 * 设置头部信息
	 * @param map
	 */
	public void setHeaders(Map<String,String> map);
	/** 
	 * 设置cookies信息
	 * @param map
	 */
	public void setCookies(Map<String,String> map);
	/**
	 * 设置会话信息
	 * @param basicHttpContext
	 */
	public void setBasicHttpContext(BasicHttpContext basicHttpContext);
	/**
	 * 
	 * @return
	 */
	public BasicHttpContext getBasicHttpContext();
	/**
	 * 设置请求参数
	 * @param map 
	 */
	public void setParameters(Map<String,String> map);
	/**
	 * 关闭连接
	 */
	public void shutDown();
	/**
	 * 设置请求编码
	 * @param encoding 编码
	 */
	public void setRequestEncoding(String encoding);
	/**
	 * post请求
	 */
	public FetchResult post() throws Exception;
	/**
	 * 设置是否本次请求的cookie是否保留到会话中
	 * @param isSaveCookie
	 */
	public void setSaveCookie(boolean isSaveCookie);
	/**
	 * 释放资源连接
	 */
	public void abort();
	/**
	 * http 添加上传的文件参数
	 * @param file
	 */
	public void setFile(File file);
	/**
	 * 设置上传文件的名称
	 * @param fileName
	 */
	public void setFileName(String fileName);
}
