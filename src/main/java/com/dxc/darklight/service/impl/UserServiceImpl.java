package com.dxc.darklight.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.dxc.darklight.dao.UserDao;
import com.dxc.darklight.model.User;
import com.dxc.darklight.service.UserService;
import com.dxc.darklight.weixin.WeChatUserService;

@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	UserDao dao;
	
	@Override
	public User getUserById(String userId) {
		// TODO Auto-generated method stub
		log.debug("queryUserId is " + userId);
		return dao.getUserById(userId);
	}
	@Override
	public void insertStockUser(User user) {
		// TODO Auto-generated method stub
		dao.insertStockUser(user);
	}
	
	public User getUser(String userId){
		User user=dao.getUserById(userId);
		try{		
		if(user==null){
			//是否关注公众号
			user =WeChatUserService.getUserNew(userId);
			if(user==null){//未关注
				return  null;
			}else{
				dao.insertStockUser(user);
			}
		}
			
		}
		catch(Exception ex){
		ex.printStackTrace();	
		}
		return user;
	}
}
