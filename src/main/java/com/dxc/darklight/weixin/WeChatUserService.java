package com.dxc.darklight.weixin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.dxc.darklight.conf.ConfFactory;
import com.dxc.darklight.model.User;
import com.dxc.darklight.util.CommonUtil;
import com.dxc.darklight.weixin.token.Token;
import com.dxc.darklight.weixin.token.TokenService;
import com.google.gson.Gson;

/**
 * 微信用户业务逻辑
 * @author fei
 */
public class WeChatUserService {
	
	private static final Log log = LogFactory.getLog(WeChatUserService.class);	
	
	

	/**新接口获取用户信息*/
	private static final String GET_USER1 = "https://api.weixin.qq.com/cgi-bin/user/info";

	
	/**
	 * 新版获取用户信息，试用前，该公众号必须在微信开放平台绑定过,需要是关注了公众号的用户。
	 * wiki
     * http://mp.weixin.qq.com/wiki/14/bb5031008f1494a59c6f71fa0f319c66.html
     * @param appId
     * @param appsecret
     * @param code
     * @return
	 * @throws Exception 
     */
    public static User getUserNew(String openId) throws Exception{
        //获取总token
        Token token = TokenService.getTokenFromRedis();
        if(token == null) return null;
        String access_token = token.getTokenStr();
        
        String userStr = getUserInfo1(access_token, openId);
        log.debug("wecharuser json : " + userStr);
        User wecharUser = strToWeChatUser(userStr);
        return wecharUser;
    }
	
	
	/**
     * 获取一个微信用户的用户信息
     * @return
     */
    public static String getUserInfo1(String access_token,String openid) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(GET_USER1 + "?access_token="+access_token+"&openid="+openid);
        try {
        	String proxyHost = ConfFactory.getConf().get("http.proxy.host");
			if (CommonUtil.notEmpty(proxyHost))
			{
				HttpHost proxy = new HttpHost(proxyHost,8080);    
				httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy); 
			}
            URL u = new URL(GET_USER1);
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
	
	@SuppressWarnings("rawtypes")
	public static User strToWeChatUser(String json){
		User weChatUser = new User();
		Map map = new Gson().fromJson(json, Map.class);
		String openid = (String)map.get("openid");
		weChatUser.setUserId(openid);
		int subscribe = map.get("subscribe") ==null ? 0 : ((Double)map.get("subscribe")).intValue();
		if(subscribe!=0){
		weChatUser.setSubscribe(subscribe);
		String nickname = (String)map.get("nickname");
		weChatUser.setNickName(nickname);
		int sex = map.get("sex") ==null ? 0 : ((Double)map.get("sex")).intValue();		
		weChatUser.setSex(sex);
		String city = (String)map.get("city");
		weChatUser.setCity(city);
		String province = (String)map.get("province");
		weChatUser.setProvince(province);
	
		String country = (String)map.get("country");
		weChatUser.setCountry(country);
		String headimgurl = (String)map.get("headimgurl");
		weChatUser.setHeadImgUrl(headimgurl);
		String unionid = (String)map.get("unionid");
		weChatUser.setUnionId(unionid);
		}else{
			return null;
		}
		return weChatUser;
	}
}
