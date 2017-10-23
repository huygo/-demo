package cn.easier.brow.test.util;

import javax.annotation.Resource;

import org.junit.Test;

import cn.easier.brow.comm.util.ConfigUtil;
import cn.easier.brow.test.base.TestBase;

/**
 * 测试类
 * 
 * @version 2015-7-23
 * @author json
 */
public class ConfigUtilTest extends TestBase {

	@Resource(name = "configUtil")
	private ConfigUtil configUtil;

	@Test
	public void testPor() {
		String jsticketurl = configUtil.get("jsticket.url");
		log.info("---->> " + jsticketurl);
	}

}