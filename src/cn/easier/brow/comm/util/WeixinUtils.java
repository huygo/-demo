package cn.easier.brow.comm.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import cn.easier.brow.comm.vo.Accesstoken;
import cn.easier.brow.comm.vo.JsAPITicket;
import cn.easier.brow.comm.vo.WebAccesstoken;
import cn.easier.brow.comm.vo.WechatUser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@SuppressWarnings({ "unused", "rawtypes" })
public final class WeixinUtils {

	private static final String ACCESSTOKENURL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String JSAPITICKETURL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	private static final String CODEURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
	private static final String CODE_BASEURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
	private static final String MENUURL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	private static final String WEBTOKENURL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	private static final String USERURL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	private static final String UNIONIDURL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	private static final String USERCUMULATEURL = "https://api.weixin.qq.com/datacube/getusercumulate?access_token=ACCESS_TOKEN";
	private static final String USERSUMMARYURL = "https://api.weixin.qq.com/datacube/getusersummary?access_token=ACCESS_TOKEN";
	private static final String WXPAYURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	private static final String WXREFUNDURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	private static Logger log = LogManager.getLogger(WeixinUtils.class);
	private static Accesstoken accesstoken = null;
	private static JsAPITicket jsAPITicket = null;

	private static final String _116114TokenUrl = "http://116114.easier.cn/wxic/weixinToken/getAccessToken";
	private static final String _116114JsTicketUrl = "http://116114.easier.cn/wxic/weixinToken/getJsTicket";
	@Resource(name = "configUtil")
	private static ConfigUtil configUtil;

	// 获取微信凭证
	/*
	 * public static Accesstoken getAccesstoken1() { if (accesstoken == null ||
	 * (!accesstoken.isValid())) {// access为空或者失效 // 替换URL的请求参数 String
	 * accessTokenUrl = ACCESSTOKENURL; accessTokenUrl =
	 * accessTokenUrl.replace("APPID", APPID); accessTokenUrl =
	 * accessTokenUrl.replace("APPSECRET", APPSECRET);
	 * log.info("请求的accessTokenUrl地址：" + accessTokenUrl); String result = null;
	 * try { result = HttpUtils.get(accessTokenUrl); } catch (Exception e) {
	 * e.printStackTrace(); log.error("获取Accesstoken请求出错!"); } if (result ==
	 * null) { return accesstoken; } log.info("getAccesstoken接口返回的数据为：" +
	 * result); JSONObject object = JSONObject.parseObject(result); if
	 * (StringUtils.isNotNull(object.getString("access_token"))) { accesstoken =
	 * new Accesstoken();
	 * accesstoken.setToken(object.get("access_token").toString());
	 * accesstoken.setValidTime(Long.parseLong(object
	 * .get("expires_in").toString()) * 1000); log.info("最新的Accesstoken为:" +
	 * accesstoken);
	 * 
	 * } } else { log.info("缓存中的Accesstoken为:" + accesstoken); } return
	 * accesstoken; }
	 * 
	 * // 获取jsapiticket public static JsAPITicket getJsAPITicket1() { if
	 * (jsAPITicket == null || (!jsAPITicket.isValid())) {// access为空或者失效
	 * 
	 * String jsAPITicketURL = JSAPITICKETURL; jsAPITicketURL =
	 * jsAPITicketURL.replace("ACCESS_TOKEN", getAccesstoken().getToken());
	 * log.info("请求的jsApiUrl地址：" + jsAPITicketURL); String result = null; try {
	 * result = HttpUtils.get(jsAPITicketURL); } catch (Exception e) {
	 * e.printStackTrace(); log.error("获取JsAPITicket请求出错!"); } if (result ==
	 * null) { return jsAPITicket; } log.info("getJsAPITicket接口返回的数据为：" +
	 * result); JSONObject object = JSONObject.parseObject(result); if
	 * (StringUtils.isNotNull(object.getString("ticket"))) { jsAPITicket = new
	 * JsAPITicket(); jsAPITicket.setTicket(object.get("ticket").toString());
	 * jsAPITicket.setValidTime(Long.parseLong(object
	 * .get("expires_in").toString()) * 1000); log.info("最新的JsAPITicket为:" +
	 * jsAPITicket);
	 * 
	 * } } else { log.info("缓存中的jsAPITicket为:" + jsAPITicket); } return
	 * jsAPITicket; }
	 */

	public static Accesstoken getAccesstoken() {
		if (accesstoken == null || !accesstoken.isValid()) {
			TreeMap<String, Object> map = new TreeMap<String, Object>();
			map.put("appid", configUtil.get("appid"));
			String resulet = null;
			try {
				resulet = HttpUtils.httpPost(_116114TokenUrl, map);
			} catch (Exception e) {
				e.printStackTrace();
			}
			log.info(resulet);
			if (resulet == null) {
				log.error("请求Accesstoken出现错误！");
				return null;
			}
			if (resulet.contains("10000")) {
				JSONObject object = JSONObject.parseObject(resulet);
				JSONObject ob = object.getJSONObject("accessToken");
				accesstoken = new Accesstoken();
				accesstoken.setToken(ob.getString("access_token"));
				accesstoken.setStartTime(ob.getLongValue("beg_time"));
				accesstoken.setEndTime(ob.getLongValue("end_time"));
				log.info("token失效了,重新获取, accessToken = " + accesstoken);
			} else {
				log.info("获取116114Token错误");
			}
		} else {
			log.info("token有效，直接取缓存值, accessToken = " + accesstoken);
		}
		return accesstoken;
	}

	/**
	 * xuzhaojie 20160818
	 * 
	 * @return
	 */
	public static JsAPITicket getJsAPITicket() {
		if (jsAPITicket == null || !jsAPITicket.isValid()) {
			TreeMap<String, Object> map = new TreeMap<String, Object>();
			map.put("appid", configUtil.get("appid"));
			String resulet = null;
			try {
				resulet = HttpUtils.httpPost(_116114JsTicketUrl, map);
			} catch (Exception e) {
				e.printStackTrace();
			}
			log.info(resulet);
			if (resulet == null) {
				log.error("请求JsAPITicket出现错误！");
				return null;
			}
			if (resulet.contains("10000")) {
				JSONObject object = JSONObject.parseObject(resulet);
				JSONObject ob = object.getJSONObject("jsticket");
				jsAPITicket = new JsAPITicket();
				jsAPITicket.setTicket(ob.getString("ticket"));
				jsAPITicket.setStartTime(ob.getLongValue("beg_time"));
				jsAPITicket.setEndTime(ob.getLongValue("end_time"));
				log.info("jsticket失效了,重新获取");
			} else {
				log.info("获取116114jsticket错误");
			}
		} else {
			log.info("jsticket有效，直接取缓存值");
		}
		return jsAPITicket;
	}

	// 得到微信使用jssdk的标志
	public static String getJsSDKSign(String url) {
		if (StringUtil.isNotNull(url)) {
			log.info("getJsSDKSign传入的url参数为" + url);
			return getJsSign(url);
		} else {
			log.error("getJsSDKSign传入的url参数为" + url + ",没有传入正确参数");
			return null;
		}
	}

	//
	/**
	 * 获取网页token的凭证
	 * 
	 * @param code
	 * @return
	 */
	public static WebAccesstoken getWebAccesstoken(String code) {
		WebAccesstoken webAccesstoken = null;
		if (code == null)
			return webAccesstoken;
		// 替换URL的请求参数
		String webaccesstokenUrl = WEBTOKENURL;
		webaccesstokenUrl = webaccesstokenUrl.replace("APPID", configUtil.get("appid"));
		webaccesstokenUrl = webaccesstokenUrl.replace("SECRET", configUtil.get("appsecret"));
		webaccesstokenUrl = webaccesstokenUrl.replace("CODE", code);
		String result = null;
		try {
			result = HttpUtils.httpPost(webaccesstokenUrl, new TreeMap<String, Object>());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取webaccesstoken请求出错!");
		}
		if (result == null) {
			// 获取网页token出现问题返回空
			return webAccesstoken;
		}
		log.info("getWebAccesstoken接口返回的数据为：" + result);
		JSONObject object = JSONObject.parseObject(result);
		if (object.get("access_token") != null) {
			webAccesstoken = new WebAccesstoken();
			webAccesstoken.setToken(object.get("access_token").toString());
			webAccesstoken.setValidTime(Long.parseLong(object.get("expires_in").toString()) * 1000);
			webAccesstoken.setRefreshToken(object.getString("refresh_token"));
			webAccesstoken.setOpenid(object.getString("openid"));
			webAccesstoken.setScope(object.getString("scope"));
			log.info("getWebAccesstoken得到的webAccesstoken数据为:" + webAccesstoken.getToken());
		}
		return webAccesstoken;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param webAccesstoken
	 * @return
	 */
	public static WechatUser getUserInf(WebAccesstoken webAccesstoken) {
		WechatUser wechatUser = null;
		if (webAccesstoken == null) {
			log.error("webAccesstoken参数不可以为空!");
			return wechatUser;
		}
		String userUrl = USERURL;
		userUrl = userUrl.replace("ACCESS_TOKEN", webAccesstoken.getToken());
		userUrl = userUrl.replace("OPENID", webAccesstoken.getOpenid());
		log.info("getUserInf请求的地址为:" + userUrl);
		String result = null;
		try {
			result = HttpUtils.httpGet(userUrl, null);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取微信userinf请求出错!");
		}
		log.info("getUserInf接口返回的数据为：" + result);
		JSONObject object = JSONObject.parseObject(result);
		if (StringUtil.isNotNull(object.getString("nickname"))) {
			wechatUser = JSONObject.parseObject(result, WechatUser.class);
			log.info("getUserInf得到的用户信息为:" + wechatUser);
			return wechatUser;
		}
		return wechatUser;
	}

	/**
	 * 获取用户基本信息（包括UnionID机制）
	 * 
	 * @param webAccesstoken
	 * @return
	 */
	public static WechatUser getUserInfUnionID(String openid) {
		WechatUser wechatUser = null;
		if (StringUtil.isNull(openid)) {
			log.error("openid不可以为空!");
			return wechatUser;
		}
		String unionIDUrl = UNIONIDURL;
		unionIDUrl = unionIDUrl.replace("ACCESS_TOKEN", getAccesstoken().getToken());
		unionIDUrl = unionIDUrl.replace("OPENID", openid);
		log.info("getUserInfUnionID请求的地址为:" + unionIDUrl);
		String result = null;
		try {
			result = HttpUtils.httpGet(unionIDUrl, null);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取微信getUserInfUnionID请求出错!");
		}
		if (result == null)// result为空直接返回空值
			return wechatUser;
		try {
			wechatUser = JSONObject.parseObject(result, WechatUser.class);
		} catch (Exception e) {
			log.error("Json解释出错!");
		}
		log.info("getUserInfUnionID得到的用户信息为:" + wechatUser);
		return wechatUser;
	}

	/**
	 * 获取用户增减数据(时间最大跨度7天) post的数据格式为： { "begin_date": "2014-12-02", "end_date":
	 * "2014-12-07" }
	 * 
	 * @param beginDate
	 *            日期开始时间
	 * @param endDate
	 *            日期结束时间
	 * @return
	 */
	/*public static String getUserSummary(String beginDate, String endDate) {
		String result = null;
		String data = "{\"begin_date\":\"beginDate\",\"end_date\":\"endDate\"}";
		log.info("getUserSummary查询开始的日期为：" + beginDate + "\t结束的日期为：" + endDate);
		data = data.replace("beginDate", beginDate);
		data = data.replace("endDate", endDate);
		String url = USERSUMMARYURL;
		url = url.replace("ACCESS_TOKEN", getAccesstoken().getToken());
		log.info("getUserSummary查询拼凑的url为" + url);
		try {
			result = HttpUtils.httpPost(url, data);
			log.info("getUserSummary返回的数据为：" + result);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("getUserSummary查询错误！");
		}
		return result;
	}*/

	/**
	 * 获取用户增减数据(时间最大跨度7天) post的数据格式为： { "begin_date": "2014-12-02", "end_date":
	 * "2014-12-07" }
	 * 
	 * @param beginDate
	 *            日期开始时间
	 * @param endDate
	 *            日期结束时间
	 * @return
	 */
	/*	public static String getUserCumulate(String beginDate, String endDate) {
			String result = null;
			log.info("getUserCumulate查询开始的日期为：" + beginDate + "\t结束的日期为：" + endDate);
			String data = "{\"begin_date\":\"beginDate\",\"end_date\":\"endDate\"}";
			data = data.replace("beginDate", beginDate);
			data = data.replace("endDate", endDate);
			String url = USERCUMULATEURL;
			url = url.replace("ACCESS_TOKEN", getAccesstoken().getToken());
			log.info("getUserCumulate查询拼凑的url为" + url);
			try {
				result = HttpUtils.httpPost(url, data);
				log.info("getUserCumulate返回的数据为：" + result);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("getUserCumulate查询错误！");
			}
			return result;
		}*/

	/**
	 * 获取用户需要授权跳转的url地址
	 * 
	 * @param url
	 * @return
	 */
	public static String getOAuth2Url(String url) {
		String oAuth2Url = CODEURL;
		oAuth2Url = oAuth2Url.replace("APPID", configUtil.get("appid"));
		oAuth2Url = oAuth2Url.replace("REDIRECT_URI", url);
		return oAuth2Url;
	}

	public static String getBase2Url(String url) {
		String oAuth2Url = CODE_BASEURL;
		oAuth2Url = oAuth2Url.replace("APPID", configUtil.get("appid"));
		oAuth2Url = oAuth2Url.replace("REDIRECT_URI", url);
		return oAuth2Url;
	}

	/**
	 * 生成sign用于请求微信
	 * 
	 * @param key
	 *            秘钥
	 * @param parameters
	 *            参数map
	 * @return
	 */
	public static String createSign(String key, Map<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		if (!(parameters instanceof SortedMap<?, ?>)) {
			parameters = new TreeMap<Object, Object>(parameters);
		}
		Set<?> es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator<?> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		String sign = Md5Util.toMD5(sb.toString()).toUpperCase();
		return sign;
	}

	/**
	 * 验证签名
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		String[] arr = new String[] { "", timestamp, nonce };
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;

		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		content = null;
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}

	// 将字节转换为十六进制字符串
	private static String byteToHexStr(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];

		String s = new String(ob);
		return s;
	}

	// 将字节数组转换为十六进制字符串
	private static String byteToStr(byte[] bytearray) {
		String strDigest = "";
		for (int i = 0; i < bytearray.length; i++) {
			strDigest += byteToHexStr(bytearray[i]);
		}
		return strDigest;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/**
	 * 生成随机字符串
	 * 
	 * @return
	 */
	public static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 生成时间戳
	 * 
	 * @return
	 */
	public static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	public static Map<String, String> sign(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String signature = "";

		// 注意这里参数名必须全部小写，且必须有序
		String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(str.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);

		return ret;
	}

	private static String getJsSign(String url) {
		Map<String, String> params = sign(getJsAPITicket().getTicket(), url);
		params.put("appid", configUtil.get("appid"));
		return JSON.toJSONString(params, false);
	}

	public static void main(String[] args) {
		System.out.println();
	}
}
