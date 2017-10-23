package cn.easier.brow.test.base;

import com.alibaba.fastjson.JSONObject;

public class Test extends TestBase {

	@org.junit.Test
	public void mebind() {
		
		JSONObject req = JSONObject.parseObject(
				"{base:{reqTime:'" + getStringDate() + "',proNo:'1002'},body:{openid:'2323'}}");
		postMethod(SERVERURI + "dlt/select", req.toJSONString());
	}
}
