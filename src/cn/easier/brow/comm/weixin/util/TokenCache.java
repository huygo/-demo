package cn.easier.brow.comm.weixin.util;

import java.util.HashMap;
import java.util.Map;

import cn.easier.brow.comm.weixin.dto.AccessToken;

public class TokenCache {
	public final static String ACCESS_TOKEN = "access_token";
	private static Map<String, AccessToken> map = new HashMap<String, AccessToken>();

	/**
	 * 保存accesstoken
	 * @param key
	 * @param accessToken
	 */
	public static void putAccessToken(AccessToken accessToken) {
		map.put(ACCESS_TOKEN, accessToken);
	}

	/**
	 * 获取accesstoken
	 * @param key
	 * @return
	 */
	public static AccessToken getAccessToken() {
		return map.get(ACCESS_TOKEN);
	}

	/**
	 * 判断accessToken是否失效
	 * @return
	 */
	public static boolean fail() {
		try {
			AccessToken token = getAccessToken();
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
