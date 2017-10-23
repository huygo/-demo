package cn.easier.brow.test.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cn.easier.brow.comm.util.ExcelUtil;
import cn.easier.brow.test.base.TestBase;

/**
 * 测试类
 * 
 * @version 2015-7-23
 * @author json
 */
public class ExcelUtilTest extends TestBase {

	@Test
	public void Test1() throws IOException {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("test1");
		arrayList.add("test2");
		List<Map<String, Object>> list = ExcelUtil.readTxt("C:\\Users\\Administrator\\Desktop\\test.txt", arrayList);
		for (Map<String, Object> map : list) {
			log.info("test1 = " + map.get("test1"));
			log.info("test2 = " + map.get("test2"));
		}
	}

}