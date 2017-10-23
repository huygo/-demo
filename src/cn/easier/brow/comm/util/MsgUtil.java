package cn.easier.brow.comm.util;

import java.util.HashMap;
import java.util.Properties;

public class MsgUtil {
	private static HashMap<String, Properties> map = new HashMap<String, Properties>();

	public static void add(String evn, Properties properties) {
		map.put(evn, properties);
	}

	public static String getMsg(String key, String evn) {
		evn = (evn == null || "".equals(evn)) ? "cn" : evn;
		return map.get(evn).getProperty(key);
	}

	public static String getMsg(String key) {
		String msg = getMsg(key, "cn");
		msg = "".equals(msg) || null == msg ? key : msg;
		return msg;
	}

}
