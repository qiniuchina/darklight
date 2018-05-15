package com.dxc.darklight.service;

import com.dxc.darklight.model.User;

public interface UserService {
	
	public User getUserById(String userId);
	public void insertStockUser(User user);
	public User getUser(String userId);
}
