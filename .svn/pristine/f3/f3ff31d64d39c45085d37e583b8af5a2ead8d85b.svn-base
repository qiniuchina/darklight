package com.dxc.darklight.datasource.redis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 各种页面访问计数的缓存方法。页面访问计数放在redis里，需要持久化存储。
 * 
 * @author fei
 * 
 */
public class RedisCache {

	private static final Logger loger = LogManager.getLogger(RedisCache.class);

	/**
	 * 给指定的主键的值增加value
	 * 
	 * @param key
	 * @param value
	 * @throws RedisException
	 */
	public void incrby(String key, int value) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			jedis.incrBy(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
	}

	/**
	 * 给指定的主键的值增加value
	 * 
	 * @param key
	 * @param value
	 * @throws RedisException
	 */
	public String lindex(String key, int index) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			String data = jedis.lindex(key, index);
			return data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
		return null;
	}

	/**
	 * 给主键对应的值+1
	 * 
	 * @param key
	 * @throws RedisException
	 */
	public void incr(String key) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			jedis.incr(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
	}

	/**
	 * 返点指定的主键对应的长整数
	 * 
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 * @throws RedisException
	 */
	public long getLong(String key) {
		long ret = 0;
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			ret = Long.parseLong(jedis.get(key));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

		return ret;
	}

	/**
	 * 重新设置一个新的long型的缓存
	 * 
	 * @param key
	 * @param value
	 * @throws RedisException
	 */
	public void setLong(String key, long value) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			jedis.set(key, "" + value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
	}

	/**
	 * 将取得的字符串以UTF-8编码
	 * 
	 * @param key
	 * @return
	 * @throws RedisException
	 */
	public String get(String key) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			String ret = jedis.get(key);
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

		return null;
	}

	/**
	 * 将value默认以UTF-8编码，按字节存储
	 * 
	 * @param key
	 * @param value
	 * @throws RedisException
	 */
	public void set(String key, String value) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			jedis.set(key, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

	}

	/**
	 * 设置key=value，并指定主键key的过期时间
	 * 
	 * @param key
	 *            主键
	 * @param value
	 *            值
	 * @param seconds
	 *            过期时间，单位秒
	 */
	public void setex(String key, String value, int seconds) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			jedis.setex(key, seconds, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

	}

	public boolean exists(String key) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			boolean exist = jedis.exists(key);
			return exist;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
		return false;
	}

	public String hget(String key, String field) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			String value = jedis.hget(key, field);
			return value;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

		return null;
	}

	public void hset(String key, String field, String value) {
		loger.debug("hset - key : " + key + " field : " + field + " value : "
				+ value);
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			jedis.hset(key, field, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

	}

	public Set<String> hkeys(String key) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			Set<String> set = jedis.hkeys(key);
			return set;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

		return null;
	}

	public boolean hexists(String key, String field) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			boolean exist = jedis.hexists(key, field);
			return exist;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
		return false;

	}

	public void hdel(String key, String field) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			jedis.hdel(key, field);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
	}

	public void hincrby(String key, String field, int increment) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			jedis.hincrBy(key, field, increment);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
	}

	public int sadd(String key, String value) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			int ret = (int) jedis.sadd(key, value).intValue();
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

		return 0;
	}

	public Long scard(String key) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			Long value = jedis.scard(key);
			return value;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

		return 0L;
	}

	public int srem(String key, String value) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			int ret = (int) jedis.srem(key, value).intValue();
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
		return 0;
	}

	public boolean sismember(String key, String value) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			boolean ret = jedis.sismember(key, value);
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

		return false;
	}

	public Set<String> sismembers(String key) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			Set<String> smembers = jedis.smembers(key);
			return smembers;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	/**
	 * 设置一个redis的list主键
	 * 
	 * @param key
	 *            list主键
	 * @param value
	 *            值
	 * @return
	 */
	public int lpush(String key, String value) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			int ret = jedis.lpush(key, value).intValue();
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
		return 0;
	}

	/**
	 * 设置一个redis的list主键中往后添加一条记录
	 * 
	 * @param key
	 *            list主键
	 * @param value
	 *            值
	 * @return 插入的index值
	 */
	public int rpush(String key, String value) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			int ret = jedis.rpush(key, value).intValue();
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

		return 0;
	}

	/**
	 * redis中指定的list中设置一个元素
	 * 
	 * @param list
	 *            主键
	 * @param index
	 *            索引
	 * @param value
	 *            值
	 * @return 设置是否成功
	 */
	public String lset(String list, int index, String value) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			String ret = jedis.lset(list, index, value);
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

		return null;
	}

	/**
	 * 得到redis中list主键的元素值,按时间倒序排列,start=end定位到某个元素值
	 * 
	 * @param key
	 *            list主键
	 * @param start
	 *            开始index
	 * @param end
	 *            结束index
	 * @return 默认返回前100个，即start=0,end=99
	 */
	public List<String> lrange(String key, int start, int end) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			List<String> list = jedis.lrange(key, start, end);
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

		return null;
	}

	/**
	 * 得到redis中list主键的元素长度
	 * 
	 * @param key
	 *            list主键
	 * @return
	 */
	public int llen(String key) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			int ret = jedis.llen(key).intValue();
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

		return 0;
	}

	/**
	 * 移除redis中list主键的第一个元素的值,即最开始设置的元素值
	 * 
	 * @param key
	 */
	public String rpop(String key) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			String ret = jedis.rpop(key);
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
		return null;
	}

	public String lpop(String key) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			String ret = jedis.lpop(key);
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
		return null;
	}

	public void ltrim(String key, int start, int end) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			jedis.ltrim(key, start, end);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

	}

	/**
	 * 删除redis中list中的某个值
	 * 
	 * @param key
	 *            list
	 * @param value
	 *            值
	 */
	public void lrem(String key, String value) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			jedis.lrem(key, -1, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
	}

	/**
	 * redis中移除一个键
	 * 
	 * @param key
	 *            键的名称
	 * @return 影响记录行数
	 */
	public long del(String key) {
		JedisPool pool = RedisClient.getPool();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(RedisClient.PASS);
			long ret = jedis.del(key);
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}
		return 0L;
	}

	/**
	 * redis中的所有key
	 * 
	 * @param pattern
	 *            满足所需key的正则表达式
	 * @return Set<String> key Set
	 */
	public Set<String> keys(String pattern) {
		Jedis jedis = null;
		try {
			jedis = new Jedis(RedisClient.HOST, RedisClient.PORT);
			Set<String> set = jedis.keys(pattern);
			return set;
		} catch (Exception e) {
		}
		return null;
	}
}
