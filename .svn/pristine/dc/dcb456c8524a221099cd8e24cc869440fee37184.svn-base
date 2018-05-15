package com.dxc.darklight.datasource.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.dxc.darklight.conf.ConfFactory;

/**
 * RedisClient提供链接Redis服务器的方法，该类不能直接实例化，只能通过{@link #connect()}链接到JRedis服务器
 * 
 * @author fei
 * 
 */
public class RedisClient {

	
	private RedisClient(){
		
	}
	
	private static final Logger loger = LogManager.getLogger(RedisClient.class);

	public static String HOST = ConfFactory.getConf().get("redis.host", "16.187.105.52");
	public static int PORT = ConfFactory.getConf().getInt("redis.port", 7480);
	public static String PASS = ConfFactory.getConf().get("redis.pass", "qiniuno1");
	private static JedisPool pool = null;

	private static void init(){
		loger.debug("HOST : " + HOST + " PORT : " + PORT);
		pool = new JedisPool(new JedisPoolConfig(), HOST, PORT, 2000);
	}
	
	public static JedisPool getPool() {
		if (pool == null) {
			init();
		}
		return pool;
	}
	//redis密码模式，暂时没有使用
	public static Jedis getJedis() {
		if (pool == null) {
			init();
		}
		Jedis jedis = pool.getResource();
			jedis.auth(PASS);
		return jedis;
	}
}
