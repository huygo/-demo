package cn.easier.brow.comm.weixin.util;

import java.util.HashMap;
import java.util.Map;

import cn.easier.brow.comm.weixin.dto.JsTicket;

public class JsTicketCache {
	public final static String JsTicket_KEY = "js_ticket";
	private static Map<String, JsTicket> map = new HashMap<String, JsTicket>();

	/**
	 * 保存accesstoken
	 * @param key
	 * @param accessToken
	 */
	public static void putJsTicket(JsTicket jsTicket) {
		map.put(JsTicket_KEY, jsTicket);
	}

	/**
	 * 获取accesstoken
	 * @param key
	 * @return
	 */
	public static JsTicket getJsTicket() {
		return map.get(JsTicket_KEY);
	}

	/**
	 * 判断accessToken是否失效
	 * @return
	 */
	public static boolean fail() {
		try {
			JsTicket token = getJsTicket();
			if (token != null) {
				long endTime = token.getEnd_time();
				long newTime = System.currentTimeMillis();
				if (endTime > newTime) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}
