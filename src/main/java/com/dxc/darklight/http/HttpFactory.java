package com.dxc.darklight.http;

public class HttpFactory {
	/**
	 * 获得httpclient对象
	 * @return Http
	 */
	public static Http getHttpClient(){
		return new HttpClientImpl();
	}
}
