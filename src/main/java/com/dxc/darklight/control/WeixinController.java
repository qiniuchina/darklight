package com.dxc.darklight.control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.mvc.View;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.dxc.darklight.util.CommonUtil;
import com.dxc.darklight.weixin.App;
import com.dxc.darklight.weixin.token.Token;
import com.dxc.darklight.weixin.token.TokenService;

@Controller
@RequestMapping("/weixin")
public class WeixinController {
	
	
	@RequestMapping(value = "/baseoauth", method = { RequestMethod.GET, RequestMethod.POST })
	public View getAuthBase(String action,String paramstr, HttpServletRequest request,HttpServletResponse response){		
		String redirectUrl = "http://"+App.APP_SERVER+"/"+App.APP_NAME+"/"+action+"?scope=base";
		if(!CommonUtil.isEmpty(paramstr))
		{
			redirectUrl += "&"+paramstr;
		}
		try {
			response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+App.APP_ID+"&redirect_uri="+
			URLEncoder.encode(redirectUrl, "utf-8")+"&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	

	
	@RequestMapping(value = "/oauth", method = { RequestMethod.GET, RequestMethod.POST })
	public View getAuthcode(String action,String paramstr, HttpServletRequest request,HttpServletResponse response){
	String redirectUrl = "http://"+App.APP_SERVER+"/"+App.APP_NAME+"/"+action+"?scope=userinfo";
		if(!CommonUtil.isEmpty(paramstr))
		{
			redirectUrl += "&"+paramstr;
		}
		
		try {
			response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+App.APP_ID+"&redirect_uri="+
		URLEncoder.encode(redirectUrl, "utf-8")+"&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/token", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getToken(String passwd) {
		ModelAndView mv = null;
		Map<String, Object> data = new HashMap<String, Object>();
		String tokenStr = "";
		if("qiniuno1".equalsIgnoreCase(passwd))
		{
			try {
				Token gtoken = TokenService.getTokenFromRedis();
				tokenStr = gtoken.getTokenStr();
			} catch (Exception e) {
				tokenStr = "";
				e.printStackTrace();
			}
			data.put("token", tokenStr);
		}
		else
		{
			data.put("token", "");
		}
	    mv = new ModelAndView(new MappingJackson2JsonView(), data);
		return mv;
	}

}
