package com.dxc.darklight.service;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用的业务处理类
 * @author fei
 *
 */
public class BaseService {

	/** 所有基础BaseService的Service类中方法返回的Map指定返回码key值*/
	public static final String KEY_CODE = "code";
	/** 所有基础BaseService的Service类中方法返回的Map指定提示信息key值*/
	public static final String KEY_MESSAGE = "message";
	/** 返回码-成功*/
	public static final int CODE_SUCCESS = 1;
	/** 返回码-失败*/
	public static final int CODE_FAILURE = -1;
	
	/**
	 * 返回失败Map结果集合
	 * @param message 提示信息
	 * @return Map<String, Object> {"code":CODE_FAILURE, "message":"提示信息"}
	 */
	public static Map<String, Object> returnFailureResult(String message){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(KEY_CODE, CODE_FAILURE);
		result.put(KEY_MESSAGE, message);
		return result;
	}
	
	/**
	 * 返回失败Map结果集合
	 * @param message 提示信息
	 * @return Map<String, Object> {"code":CODE_FAILURE, "message":"提示信息"}
	 */
	public static Map<String, Object> returnFailureResult(int code,String message){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(KEY_CODE, code);
		result.put(KEY_MESSAGE, message);
		return result;
	}
	
	/**
	 * 返回成功Map结果集合
	 * @param message 提示信息
	 * @return Map<String, Object> {"code":CODE_SUCCESS, "message":"提示信息"}
	 */
	public static Map<String, Object> returnSuccessResult(String message){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(KEY_CODE, CODE_SUCCESS);
		result.put(KEY_MESSAGE, message);
		return result;
	}
	
	/**
	 * 返回数据库操作失败Map结果集合
	 * @return Map<String, Object> {"code":CODE_FAILURE, "message":"提示信息"}
	 */
	public static Map<String, Object> returnDBExceptionResult(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(KEY_CODE, CODE_FAILURE);
		result.put(KEY_MESSAGE, "服务器忙，请稍后再试");
		return result;
	}
}
