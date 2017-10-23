package cn.easier.brow.comm.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

public class AliOcsMemcachedUtil {
	private static Logger logger = LoggerFactory.getLogger(AliOcsMemcachedUtil.class);


	// public static final String host = "apptest.0085.com"; //控制台上的“内网地址”
//	public static final String host = "112.124.49.40";
	//public static final String host = "647217d133be46f0.m.cnszalist3pub001.ocs.aliyuncs.com";
//	public static final String port = "11211"; // 默认端口 11211，不用改

	private static MemcachedClient client;

	private AliOcsMemcachedUtil() {
	}
	
	public static void initMemcached(){
	    try {
            client = new MemcachedClient(new BinaryConnectionFactory(),
                    AddrUtil.getAddresses(PropUtil.get("memcached.host") + ":" + PropUtil.get("memcached.port")));
            logger.info("memcached初始化成功,ip地址:"+PropUtil.get("memcached.host")+",端口:"+PropUtil.get("memcached.port"));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	/**
	 * 连接时间
	 */
	public static final int CONNECT_TIME_OUT = 30;

	/**
	 * 缓存数据时间
	 */
	public static final int CACHED_TIME_OUT = 24 * 60 * 60;

	/**
	 * 缓存数据最长时间
	 */
	public static final int CACHED_TIME_OUT_MAX = 60 * 60 * 24 * 30;

	/**
	 * 新增键值对，如果存在，则不做任何操作
	 * 
	 * @param key
	 * @param value
	 */
	public static void add(String key, String value) {
		client.add(key, 0, value);
	}

	/**
	 * 设置键值对，如果存在，则覆盖原来的value
	 * 
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value) {
		client.set(key, 0, value);
	}

	public static void update(String key, String value) {
		while (true) {
			CASValue<Object> cas = client.gets(key);
			CASResponse response = client.cas(key, cas.getCas(), value);
			if (CASResponse.OK.name().equals(response.name())) {
				break;
			}
		}
	}

	/**
	 * 获取key所对应的value
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		CASValue<Object> cas = client.gets(key);
		if (cas != null) {
			Object valObj = cas.getValue();
			if (valObj != null) {
				return valObj.toString();
			}
		}
		return "";
	}

	/**
	 * 根据请求URL从缓存中获取数据
	 * 
	 * @param request
	 * @return
	 */
	public String getFromCache(HttpServletRequest request) {
		String rep = "";
		String fullPath = request.getRequestURI();
		String queryString = request.getQueryString();

		logger.info("fullPath:" + fullPath);
		if (StringUtils.isNotBlank(queryString)) {
			fullPath += queryString;
		}
		logger.info("queryString:" + fullPath);
		try {
			rep = get(fullPath); // 获取缓存数据并更新超时时间,这个等待时间默认是5秒(1.3.8开始改为5秒，之前是1秒
			if (!ValidUtil.anyEmpty(rep)) {
				return rep;
			}
		} catch (Exception e) {
		}
		return rep;
	}

	/**
	 * 路径
	 * 
	 * @param path
	 * @param page
	 * @return
	 */
	public String getFromCache(HttpServletRequest request, String page) {
		String rep = "";
		String path = request.getRequestURI();
		logger.info("path:" + path);
		if (StringUtils.isNotBlank(page)) {
			path += page;
		}

		logger.info("queryString:" + path);

		try {
			rep = get(path); // 获取缓存数据并更新超时时间,这个等待时间默认是5秒(1.3.8开始改为5秒，之前是1秒
			if (!ValidUtil.anyEmpty(rep)) {
				return rep;
			}
		} catch (Exception e) {
		}
		return rep;
	}

	/**
	 * 
	 * @param request
	 * @param cacheStr
	 */
	public static void savaToCache(HttpServletRequest request, String cacheStr) {
		String rep = "";
		String fullPath = request.getRequestURI();
		String queryString = request.getQueryString();
		if (StringUtils.isNotBlank(queryString)) {
			fullPath += queryString;
		}
		try {
			client.set(fullPath, 0, rep);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 保存缓存或者更新缓存
	 * 
	 * @param caCheKey
	 * @param obj
	 */
	public static boolean setToCache(String caCheKey, Object obj) {
		return setToCache(caCheKey, obj, CACHED_TIME_OUT);
	}

	/**
	 * 保存缓存或者更新缓存
	 * 
	 * @param caCheKey
	 * @param obj
	 * @param time
	 */
	public static boolean setToCache(String caCheKey, Object obj, Integer time) {
		if (obj == null || ValidUtil.anyEmpty(caCheKey))
			return false;
		try {
			String cacheValue = "";
			if (obj instanceof String) {
				cacheValue = (String) obj;
			} else {
				cacheValue = JSON.toJSONString(obj);
			}
			client.delete(caCheKey);
			client.set(caCheKey, time, cacheValue);
			return true;
		} catch (Exception e) {
			logger.error("setToCache use time key:" + caCheKey + ",excetion:" + e.getMessage());
		}
		return false;
	}

	/**
	 * 保存对象缓存或者更新对象缓存
	 * 
	 * @param caCheKey
	 * @param obj
	 */
	public static boolean setObjectToCache(String caCheKey, Object obj) {
		return setObjectToCache(caCheKey, obj, CACHED_TIME_OUT);
	}

	/**
	 * 保存对象缓存或者更新对象缓存
	 * 
	 * @param caCheKey
	 * @param obj
	 * @param time
	 */
	public static boolean setObjectToCache(String caCheKey, Object obj,
			Integer time) {
		if (obj == null || ValidUtil.anyEmpty(caCheKey))
			return false;
		try {
			client.delete(caCheKey);
			client.set(caCheKey, time, obj);
			return true;
		} catch (Exception e) {
			logger.error("setObjectToCache use time key:" + caCheKey + ",excetion:" + e.getMessage());
		}
		return false;
	}

	/**
	 * 删除缓存
	 * 
	 * @param caCheKey
	 * @return
	 */
	public static OperationFuture<Boolean> deleteCache(String caCheKey) {
		return client.delete(caCheKey);
	}

	/**
	 * 判断缓存是否存在
	 * 
	 * @param cacheKey
	 * @return
	 */
	public static boolean isExist(String caCheKey) {
		try {
			return get(caCheKey) != null;
		} catch (Exception e) {
			logger.error("isExist key:" + caCheKey + ",excetion:" + e.getMessage());
		}
		return false;
	}

	/**
	 * 获取缓存
	 * 
	 * @param caCheKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getCache(String caCheKey) {
		try {
			T t = (T) get(caCheKey);
			if (t instanceof String) {
				return (T) t;
			}
			return t;
		} catch (Exception e) {
			logger.error("getcache key:" + caCheKey + ",excetion:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 根据缓存转成对应的对象
	 * 
	 * @param cacheKey
	 * @param c
	 * @return
	 */
	public static <T extends Object> T getCache(String caCheKey, Class<T> c) {
		if (ValidUtil.anyEmpty(caCheKey))
			return null;
		try {
			String cacheValue = get(caCheKey);
			if (ValidUtil.anyEmpty(cacheValue))
				return null;
			return JSON.parseObject(cacheValue, c);
		} catch (Exception e) {
			logger.error("getCache<T>  key:" + caCheKey + ",excetion:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 根据缓存转成对应的集合
	 * 
	 * @param cacheKey
	 * @param c
	 * @return
	 */
	public static <T> List<T> getCacheToList(String caCheKey, Class<T> c) {
		if (ValidUtil.anyEmpty(caCheKey))
			return null;
		try {
			String cacheValue = get(caCheKey);
			if (ValidUtil.anyEmpty(cacheValue))
				return null;
			return JSON.parseArray(cacheValue, c);
		} catch (Exception e) {
			logger.error("getCacheToList<T>  key:" + caCheKey + ",excetion:" + e.getMessage());
		}
		return null;
	}

}
