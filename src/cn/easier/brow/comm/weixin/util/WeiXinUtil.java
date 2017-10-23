package cn.easier.brow.comm.weixin.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.easier.brow.comm.util.CommonParams;
import cn.easier.brow.comm.util.EasierUtils;
import cn.easier.brow.comm.util.HttpUtils;
import cn.easier.brow.comm.util.PropUtil;
import cn.easier.brow.comm.util.StringUtil;
import cn.easier.brow.comm.weixin.dto.AccessToken;
import cn.easier.brow.comm.weixin.dto.JsSign;
import cn.easier.brow.comm.weixin.dto.JsTicket;
import cn.easier.brow.comm.weixin.dto.UserInfoJO;

/**
 * 
 * @Title WeiXinUtil
 * @Package cn.comm.weixin.util
 * @author qiufh
 * @date 2016年4月12日上午9:40:36
 * @version V1.0
 * @Description (微信工具类)
 */
@SuppressWarnings("unused")
public class WeiXinUtil {
	private static Logger log = Logger.getLogger(CommonParams.LOG_SYS);

	public static void sendMsgToUser(String msg, String access_token) {
		//发送消息给普通用户
		String send_msg_to_user_url = "https://api.weixin.qq.com/cgi-bin/message/send?access_token=" + access_token;
		//send_msg_to_user_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token;
		log.debug(send_msg_to_user_url);
		String response = HttpUtils.httpsRequest(send_msg_to_user_url, msg, "POST");
		log.debug(response);
	}

	/**
	 * 
	 * @Description (获取公众号带参二维码)
	 * @param access_token
	 * @param sceneStr   场景字符串
	 * @return
	 * @throws UnsupportedEncodingException String
	 * @date 2016年5月18日下午2:30:24
	 * @author qiufh
	 */
	public static String create(String access_token, String sceneStr) throws UnsupportedEncodingException {
		String send_msg_to_user_url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + access_token;
		String jsonparam = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": " + sceneStr
				+ "}}}";
		log.debug(send_msg_to_user_url);
		String response = HttpUtils.httpsRequest(send_msg_to_user_url, "POST", jsonparam);
		log.debug(response);
		JSONObject parseObject = JSONObject.parseObject(response);
		String ticket = (String) parseObject.get("ticket");
		String imageUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + URLEncoder.encode(ticket, "UTF-8");
		//String httpGet = HttpUtils.httpGet(url, null);
		log.debug("httpGet -- " + imageUrl);
		return imageUrl;
	}

	/**
	 * 
	 * @Description (获取微信用户信息)
	 * @param openid
	 * @param access_token
	 * @return
	 * @throws UnsupportedEncodingException JSONObject
	 * @date 2016年4月8日下午4:57:29
	 * @author qiufh
	 */
	public static UserInfoJO getWeiXinUserInfo(String openid, String access_token) throws UnsupportedEncodingException {
		String get_userinfo = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid
				+ "&lang=zh_CN";
		String userInfoJson = HttpUtils.getUrl(get_userinfo);
		userInfoJson = new String(userInfoJson.getBytes("ISO-8859-1"), "utf-8");
		log.info("用户信息userInfoJson:" + userInfoJson);
		UserInfoJO userInfoJO = (UserInfoJO) SFJsonUtils.getObject4JsonString(userInfoJson, UserInfoJO.class);
		String nickname = EmojiFilter.filterEmoji(userInfoJO.getNickname());
		userInfoJO.setNickname(nickname);
		return userInfoJO;
	}

	/**
	 * 获取凭证
	 */
	public static String getAccessToken(String appid) {
		String accessToken = null;
		try {
			TreeMap<String, Object> params = new TreeMap<String, Object>();
			params.put("appid", appid);
			String url = PropUtil.get("access.token.url");
			String resultStr = HttpUtils.httpPost(url, params);
			log.debug("获取accessToken返回值 --- " + resultStr);
			if (resultStr != null) {
				JSONObject resultObject = JSONObject.parseObject(resultStr);
				String code = resultObject.getString("code");
				if ("10000".equals(code)) {
					AccessToken ac = resultObject.getObject("accessToken", AccessToken.class);
					accessToken = ac.getAccess_token();
				} else {
					log.debug(appid + " -->> 获取accessToken失败 : " + resultObject.getString("msg"));
				}
			} else {
				log.debug(appid + " -->> 获取accessToken失败 ");
			}
		} catch (Exception e) {
			log.error("获取凭证失败", e);
		}
		return accessToken;
	}

	/**
	 * 
	 * @Description (获取微信access_token和openid)
	 * @param code
	 * @param appid
	 * @param appsecret
	 * @return JSONObject
	 * @date 2016年4月8日下午4:34:13
	 * @author qiufh
	 */
	/*public static JSONObject getWeiXinConfig(String code, String appid, String appsecret) {
		JSONObject jsonObject = null;
		try {
			log.info("获取授权code=" + code);
			// String code=getParameter("code");
			System.out.println("获取code=" + code);
			if (StringUtils.isNotEmpty(code)) {
				String get_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret="
						+ appsecret + "&code=" + code + "&grant_type=authorization_code";
				String json = HttpUtils.getUrl(get_access_token_url);
				// log.info(json);
				json = new String(json.getBytes("ISO-8859-1"), "utf-8");
				jsonObject = JSONObject.parseObject(json);
				log.info("json:" + json + ",jsonObject:" + jsonObject);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("", e);
		}
		return jsonObject;
	}*/

	/**
	 * 获取js票据
	 */
	public static String getJSTicket(String appid) {
		String jsticket = null;
		try {
			TreeMap<String, Object> params = new TreeMap<String, Object>();
			params.put("appid", appid);
			String url = PropUtil.get("jsticket.url");
			String resultStr = HttpUtils.httpPost(url, params);
			log.debug("获取jsticket返回值 --- " + resultStr);
			if (resultStr != null) {
				JSONObject resultObject = JSONObject.parseObject(resultStr);
				String code = resultObject.getString("code");
				if ("10000".equals(code)) {
					JsTicket ticket = resultObject.getObject("jsticket", JsTicket.class);
					jsticket = ticket.getTicket();
				} else {
					log.debug(appid + " -->> 获取jsticket失败 : " + resultObject.getString("msg"));
				}
			} else {
				log.debug(appid + " -->> 获取jsticket失败 ");
			}
		} catch (Exception e) {
			log.error("获取凭证失败", e);
		}
		return jsticket;
	}

	//	/**
	//	 * 获取缓存中的jsticket
	//	 * @return
	//	 */
	//	public static String getJsTicket2(String appid, String appsecret) {
	//		if (!JsTicketCache.fail()) {
	//			log.debug("jsticket无效,重新获取值");
	//			return getJSTicket(appid, appsecret);
	//		} else {
	//			log.debug("jsticket已存在,直接取缓存值");
	//			return JsTicketCache.getJsTicket().getTicket();
	//		}
	//	}

	/**tupf
	 * 获取code
	 * @return
	 */
	public static String getCode(String appid, String redirectUri) {
		String response = null;
		try {
			String oauth_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri="
					+ redirectUri + "?type=0&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
			response = HttpUtils.getUrl(oauth_url);
			log.debug(response);
			if (response != null && !"".equals(response)) {
				if (response != null) {
					if (response.equals("0")) {
						return response;
					} else {
						log.error("获取code失败,=" + response);
					}
				} else {
					log.error("获取code失败,response=null");
				}
			} else {
				log.error("获取code失败,response=" + response);
			}
		} catch (Exception e) {
			log.error("获取code失败", e);
		}
		return null;
	}

	/**tupf
	 * 根据code获取token
	 * @return
	 */
	public static AccessToken getTokenByOauth(String code, String appid, String appsecret) {
		String response = null;
		String get_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + appsecret
				+ "&code=" + code + "&grant_type=authorization_code";
		try {
			log.debug("url = " + get_access_token_url);
			response = HttpUtils.getUrl(get_access_token_url);
			log.debug(response);
			if (response != null && !"".equals(response)) {
				AccessToken accessToken = (AccessToken) SFJsonUtils.getObject4JsonString(response, AccessToken.class);
				if (accessToken != null) {
					if (accessToken.getErrcode() == 0) {
						log.debug("获取凭证成功,errcode=" + accessToken.getErrcode() + ",token=" + accessToken.getAccess_token());
						TokenCache.putAccessToken(accessToken);
						return accessToken;
					} else {
						log.error("获取凭证失败,errcode=" + accessToken.getErrcode() + ",errmsg=" + accessToken.getErrmsg());
					}
				} else {
					log.error("获取凭证失败,AccessToken=null");
				}
			} else {
				log.error("获取凭证失败,response=" + response);
			}
		} catch (Exception e) {
			log.error("获取凭证失败", e);
		}
		return null;
	}

	/**
	 * 获取用户id判断是否有效
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean isWorkOpenId(String openid, String appid, String appsecret) throws UnsupportedEncodingException {
		boolean b = false;

		String accessToken = getAccessToken(appid);
		String json = HttpUtils.getUrl(
				"https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN");
		log.debug(json);
		if (!json.contains("\"errcode\":40003")) {
			b = true;
		}
		return b;
	}

	/**
	 * 判断用户是否关注改公众号
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean isFollow(String openid, String appid, String appsecret) throws UnsupportedEncodingException {
		boolean b = false;

		String accessToken = getAccessToken(appid);
		String json = HttpUtils.getUrl(
				"https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN");
		log.debug("jsonString --- " + json);
		JSONObject parseObject = JSONObject.parseObject(json);
		log.debug("jsonObject --- " + parseObject);
		Integer subscribe = (Integer) parseObject.get("subscribe");
		if (1 == subscribe) {
			b = true;
		}
		return b;
	}

	/***
	 * 
	 * @Description (获取用于分享的数字签名)
	 * @param appid   微信appid
	 * @param appsecret   微信appsecret
	 * @return JsSign
	 * @date 2016年4月12日上午10:22:18
	 * @author qiufh
	 */
	public static JsSign getJsSign(String appid, String jsurl) {
		JsSign jsSign = null;
		try {
			String jsticket = getJSTicket(appid);
			String noncestr = SignUtil.getNonceStr(); // 随机字符串
			String timestamp = SignUtil.getTimeStamp();// 时间戳
			String beginSign = "jsapi_ticket=" + jsticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + jsurl;
			log.debug("预备签名字符串=" + beginSign);
			String signature = SignUtil.getSha1(beginSign);
			log.debug("获取签名值=" + signature);
			jsSign = new JsSign(jsticket, noncestr, timestamp, signature);
		} catch (Exception e) {
			log.debug("获取jssdk签名错误", e);
		}
		return jsSign;
	}

	/**
	* @Description (微信红包)
	* @param mch_id  商户号
	* @param paternerKey   商户秘钥
	* @param send_name  红包发送者名称
	* @param wxappid   APPID
	* @param re_openid  接受红包的用户id
	* @param total_amount  付款金额，单位分
	* @param total_num   红包发放总人数 
	* @param wishing     红包祝福语
	* @param client_ip   调用接口的机器Ip地址
	* @param act_name    活动名称备注信息
	* @param remark void  备注信息
	* @date 2016年6月4日下午9:28:44
	* @author qiufh
	 * @throws Exception 
	*//*
		public static Map<String, Object> sendRedPack(String mch_id, String paternerKey, String send_name, String wxappid,
			String re_openid, String total_amount, String total_num, String wishing, String client_ip, String act_name,
			String remark, HttpServletRequest request) throws Exception {
		String response = null;
		TreeMap<Object, Object> params = new TreeMap<Object, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		String nonce_str = StringUtil.getUUID().toUpperCase();
		String mch_billno = StringUtil.createOnlyNum(mch_id, 10);
		params.put("nonce_str", nonce_str);
		params.put("mch_billno", mch_billno);
		params.put("mch_id", mch_id);
		params.put("send_name", send_name);
		params.put("wxappid", wxappid);
		params.put("re_openid", re_openid);
		params.put("total_amount", total_amount);
		params.put("total_num", total_num);
		params.put("wishing", wishing);
		params.put("client_ip", client_ip);
		params.put("act_name", act_name);
		params.put("remark", remark);
		String sign = SignUtil.clientSign(paternerKey, params);
		params.put("sign", sign);
		//参数转换成xml
		String xml = getRequestXml(params);
		log.info("现金发红包xml数据:" + xml);
		//获取证书
		Long startTime = System.currentTimeMillis();
		InputStream instream = EasierUtils.getCertPath(request);
		response = HttpUtils.sendRedPack(xml, instream, mch_id);
		log.info("请求发送红包返回数据:" + response);
		Long endTime = System.currentTimeMillis();
		log.info("现金发红包接口总耗时:" + ((endTime - startTime)) / 1000 + "秒");
		map = XMLUtil.xmlToMap(response);
		return map;
		}*/

	/**
	 * @Description：将请求参数转换为xml格式的string
	 * @param parameters 请求参数
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getRequestXml(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}
}
