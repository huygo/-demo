package cn.easier.brow.test.util;

import javax.annotation.Resource;

import org.junit.Test;

import cn.easier.brow.sys.redis.util.RedisUtil;
import cn.easier.brow.test.base.TestBase;

/**
 * 测试类
 * 
 * @version 2015-7-23
 * @author json
 */
public class RedisUtilTest extends TestBase {

	@Resource(name = "redisCache")
	private RedisUtil redisCache;

	@Test
	public void Test1() {
		String setString = redisCache.setString("k4", "v4");
		System.out.println(setString);
	}

}