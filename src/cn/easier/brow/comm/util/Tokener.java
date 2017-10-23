package cn.easier.brow.comm.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

/**
 * 对称加密 AES Coder<br/>
 * secret key length: 128bit, default: 128 bit<br/>
 * mode: ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/>
 * padding: Nopadding/PKCS5Padding/ISO10126Padding/
 * 
 * @author Aub
 * 
 */
@SuppressWarnings("unused")
public class Tokener {

	protected static final Log log = LogFactory.getLog(CommonParams.LOG_SYS);
	// 密钥算法
	private static final String KEY_ALGORITHM = "AES";

	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

	private static String KEY = "ad05c684945139ba1af9b814a8d70886";

	/**
	 * 客户标识  (UV)
	 */
	private static final String CUSTOMER_IDENTIFICATION = "wmpo.cus.id";

	/**
	 * 用户id
	 */
	public static final String USER_ID = "userID";
	/**
	 * token 生成当前时间戳
	 */
	public static final String TOKEN_TIME = "tokenTime";
	/**
	 *token 超时时间
	 */
	public static final String EXP_TIME = "expiretTime";
	/**
	 * token串关键字
	 */
	public static final String AUTHORIZATION = "authorization";
	/**
	 * 用户token保存用户手机号的键
	 */
	public static final String TOKEN_PHONE = "phone";
	/**
	 * 用户token保存的管理员（用户）账号
	 */
	public static final String TOKEN_ACCT = "acct";
	/**
	 * 令牌超时时间,单位：秒
	 */
	public final static int EXPIRETTIME = 60 * 30;

	public final static String RESOURCE_NOT_EXIST = "Access to the resource does not exist";

	/**
	 * 组装token
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static String createTokenStr(Map<String, String> map) throws Exception {
		return encodeStr((new Gson()).toJson(map));

	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getMapFromTokenStr(String mapStr) {
		String data = decodeStr(mapStr);
		if (data != null) {
			return (new Gson()).fromJson(data, Map.class);
		}
		return null;

	}

	/**
	 * 初始化密钥
	 * 
	 * @return byte[] 密钥
	 * @throws Exception
	 */
	private static byte[] initSecretKey() {
		// 返回生成指定算法的秘密密钥的 KeyGenerator 对象
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new byte[0];
		}
		// 初始化此密钥生成器，使其具有确定的密钥大小
		// AES 要求密钥长度为 128
		kg.init(128);
		// 生成一个密钥
		SecretKey secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}

	/**
	 * 转换密钥
	 * 
	 * @param key
	 *            二进制密钥
	 * @return 密钥
	 */
	private static Key toKey(byte[] key) {
		// 生成密钥
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}

	/**
	 * 加密
	 * 
	 * @param data
	 *            待加密数据
	 * @param key
	 *            密钥
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, Key key) throws Exception {
		return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}

	/**
	 * 加密
	 * 
	 * @param data
	 *            待加密数据
	 * @param key
	 *            二进制密钥
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}

	/**
	 * 加密
	 * 
	 * @param data
	 *            待加密数据
	 * @param key
	 *            二进制密钥
	 * @param cipherAlgorithm
	 *            加密算法/工作模式/填充方式
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
		// 还原密钥
		Key k = toKey(key);
		return encrypt(data, k, cipherAlgorithm);
	}

	/**
	 * 加密
	 * 
	 * @param data
	 *            待加密数据
	 * @param key
	 *            密钥
	 * @param cipherAlgorithm
	 *            加密算法/工作模式/填充方式
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
		// 实例化
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		// 使用密钥初始化，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, key);
		// 执行操作
		return cipher.doFinal(data);
	}

	/**
	 * 解密
	 * 
	 * @param data
	 *            待解密数据
	 * @param key
	 *            二进制密钥
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}

	/**
	 * 解密
	 * 
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, Key key) {
		try {
			return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
		} catch (Exception e) {
			//
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param data
	 *            待解密数据
	 * @param key
	 *            二进制密钥
	 * @param cipherAlgorithm
	 *            加密算法/工作模式/填充方式
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
		// 还原密钥
		Key k = toKey(key);
		return decrypt(data, k, cipherAlgorithm);
	}

	/**
	 * 解密
	 * 
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @param cipherAlgorithm
	 *            加密算法/工作模式/填充方式
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
		// 实例化
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		// 使用密钥初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, key);
		// 执行操作
		return cipher.doFinal(data);
	}

	private static String showByteArray(byte[] data) {
		if (null == data) {
			return null;
		}
		StringBuilder sb = new StringBuilder("{");
		for (byte b : data) {
			sb.append(b).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * 返回加密后字符串
	 * 
	 * @param data
	 *            加密字符串
	 * @param k
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	public static String encodeStr(String data) {
		String response = "";
		try {
			// String key = WebApplictionContext.ieks.getKey(
			// EmailKeyType.EMAILTYPE);
			byte[] bytekey = Hex.decodeHex(KEY.toCharArray());
			Key k = toKey(bytekey);
			byte[] b = encrypt(data.getBytes(), k);
			response = Hex.encodeHexString(b);
		} catch (DecoderException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 指定KEY加密
	 * 
	 * @param data
	 * @param AESKEY
	 * @return
	 */
	private static String encodeStr(String data, String AESkey) {
		String response = "";
		try {
			byte[] bytekey = Hex.decodeHex(AESkey.toCharArray());
			Key k = toKey(bytekey);
			byte[] b = encrypt(data.getBytes(), k);
			response = Hex.encodeHexString(b);
		} catch (DecoderException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 返回解密后字符串
	 * 
	 * @param data
	 *            揭秘数据
	 * @param k
	 *            解密密钥
	 * @return
	 * @throws Exception
	 */
	public static String decodeStr(String data) {
		String response = "";
		try {

			byte[] bytekey = Hex.decodeHex(KEY.toCharArray());
			Key k = toKey(bytekey);
			byte[] b = Hex.decodeHex(data.toCharArray());
			byte[] decryptData = decrypt(b, k);
			response = new String(decryptData);
		} catch (DecoderException e) {
			//
		} catch (Exception e) {
			//
		}
		return response;
	}

	/**
	 * 指定key解密
	 * 
	 * @param data
	 * @param AESkey
	 * @return
	 */
	private static String decodeStr(String data, String AESkey) {
		String response = "";
		try {
			byte[] bytekey = Hex.decodeHex(AESkey.toCharArray());
			Key k = toKey(bytekey);
			byte[] b = Hex.decodeHex(data.toCharArray());
			byte[] decryptData = decrypt(b, k);
			response = new String(decryptData);
		} catch (DecoderException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 设置cookie
	 * 
	 * @param response
	 * @param name
	 *            cookie名字
	 * @param value
	 *            cookie值
	 * @param maxAge
	 *            cookie生命周期 以秒为单位
	 */
	public static void addCookie(HttpServletRequest req, HttpServletResponse response, String name, String value, int maxAge) {
		try {
			value = URLEncoder.encode(value, "UTF-8");
			Cookie cookie = new Cookie(name, value);
			cookie.setPath(req.getContextPath());
			cookie.setMaxAge(maxAge);
			log.debug("成功设置Cookie：" + name + "_" + value + "_" + maxAge);
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description (删除coolie,立即刪除)
	 * @param req
	 * @param response
	 * @param name void
	 * @date 2016年8月3日下午9:49:16
	 * @author qiufh
	 */
	public static void delCookie(HttpServletRequest req, HttpServletResponse response, String name) {
		try {
			Cookie cookie = new Cookie(name, null);
			cookie.setMaxAge(0);//0立即刪除，负数则退出浏览器自动删除。
			cookie.setPath(req.getContextPath());
			response.addCookie(cookie);
			log.debug("删除Cookie成功：" + name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**  
	 * cookie中获取用户唯一标识 采用uuid记录 存在获取不存在重新设置  
	 * @throws ParseException 
	 */
	public static String autoSetCookie(HttpServletRequest req, HttpServletResponse response, String cookieName, int expire) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (StringUtils.equals(cookie.getName(), cookieName)) {
					return cookie.getValue();
				}
			}
		}
		String uuid = StringUtil.getUUID();
		autoSetCid(req, response, cookieName, uuid, expire);
		return uuid;
	}

	/**  
	 * 自动设置客户端唯一标识  
	 * @throws ParseException 
	 */
	private static void autoSetCid(HttpServletRequest req, HttpServletResponse response, String cookieName, String uvFlag,
			int expire) {
		Cookie cidCookie = new Cookie(cookieName, uvFlag);
		//写入cookie,过期到今天的23:59:59点
		//long curtime = DateUtils.toLong(DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		//int intExpire = (int) (DateUtils.getEndTime() / 1000);
		//int intExpire = 10 * 365 * 24 * 60 * 60;//10年
		cidCookie.setMaxAge(expire);
		cidCookie.setPath(req.getContextPath());
		response.addCookie(cidCookie);
	}

	/**
	 * Base64混编码
	 * 
	 * @param base64Str
	 * @return
	 */
	public static String mixedBase64Encode(String base64Str) {
		StringBuffer bsNewStr = new StringBuffer();
		for (int i = 0; i < base64Str.length() - 2; i++) {
			bsNewStr.append(base64Str.charAt(i)).append(StringUtil.getRandomString(1));
		}
		bsNewStr.append(base64Str.charAt(base64Str.length() - 2)).append(base64Str.charAt(base64Str.length() - 1));
		return bsNewStr.toString();
	}

	/**
	 * Base64混编码解码
	 * 
	 * @param base64Str
	 * @return
	 */
	public static String mixedBase64Decode(String base64Str) {
		StringBuffer bsNewStr = new StringBuffer();
		for (int i = 0; i < base64Str.length() - 2; i++) {
			if (i % 2 == 0) {
				bsNewStr.append(base64Str.charAt(i));
			}
		}
		bsNewStr.append(base64Str.charAt(base64Str.length() - 2)).append(base64Str.charAt(base64Str.length() - 1));
		return bsNewStr.toString();
	}

	/**
	 * 判断对象数组是否存在Null的对象。
	 * 
	 * @param objs
	 * @return
	 */
	public static boolean isNull(Object... objs) {
		for (Object obj : objs) {
			if (obj == null || "".equals(obj))
				return true;
		}
		return false;
	}

	/**
	 * 获取token
	 */
	public static String getToken(HttpServletRequest requset) {
		String authorization = null;
		try {
			authorization = requset.getHeader(AUTHORIZATION);
			if (isNull(authorization)) {
				authorization = requset.getParameter(AUTHORIZATION);
			}
			String expiretTime = (String) (getMapFromTokenStr(authorization).get(EXP_TIME));
			boolean isToken = isToken(authorization);
			if (isToken) {
				return authorization;
			} else {
				return null;
			}
		} catch (Exception e) {
			log.debug("获取token失败");
		}
		return null;
	}

	/**
	 * 获取token
	 */
	public static String getToken(JSONObject obj) {
		String authorization = null;
		try {
			JSONObject basejson = (JSONObject) obj.get("base");
			authorization = basejson.getString(AUTHORIZATION);
			//authorization = obj.getString(AUTHORIZATION);
			boolean isToken = isToken(authorization);
			if (isToken) {
				return authorization;
			} else {
				return null;
			}
		} catch (Exception e) {
			log.debug("获取token失败");
		}
		return null;
	}

	/**
	 * 
	 * @Description (检查token是否过期)
	 * @param token
	 * @return boolean
	 * @date 2016年2月15日下午3:56:45
	 * @author qiufh
	 */
	public static boolean isToken(String token) {
		boolean isToken = false;
		if (StringUtils.isNotEmpty(token)) {
			String expiretTime = (String) (getMapFromTokenStr(token).get(EXP_TIME));
			if (Long.valueOf(expiretTime) > System.currentTimeMillis()) {
				isToken = true;
			} else {
				log.debug("token失效");
			}
		} else {
			log.debug("token is null");
		}
		return isToken;
	}

	/**
	 * 获取用户ID
	 * 
	 * @return
	 */
	public static String getUseid(HttpServletRequest requset) {
		String userid = null;
		try {
			String authorization = getToken(requset);
			if (!isNull(authorization)) {
				return userid = (String) (getMapFromTokenStr(authorization).get(USER_ID));
			}
		} catch (Exception e) {
			log.debug("从token获取userid失败");
		}
		return userid;
	}

	public static String getUseid(String token) {
		String userid = null;
		try {
			if (!isNull(token)) {
				return userid = (String) (getMapFromTokenStr(token).get(USER_ID));
			}
		} catch (Exception e) {
			log.debug("从token获取userid失败");
		}
		return userid;
	}

	/**
	 * 获取用户ID
	 * 
	 * @return
	 */
	public static String getUseid(JSONObject obj) {
		String userid = null;
		try {
			String authorization = getToken(obj);
			if (!isNull(authorization)) {
				return userid = (String) (getMapFromTokenStr(authorization).get(USER_ID));
			}
		} catch (Exception e) {
			log.debug("获取用户ID失败");
		}
		return userid;
	}

	/**
	 * 获取用户phone
	 * 
	 * @return
	 */
	public static String getPhone(HttpServletRequest requset) {
		String phone = null;
		try {
			String authorization = requset.getHeader(AUTHORIZATION);
			if (isNull(authorization)) {
				authorization = getToken(requset);// requset.getParameter(AUTHORIZATION);
			}
			if (!isNull(authorization)) {
				return phone = (String) (getMapFromTokenStr(authorization).get(TOKEN_PHONE));
			}
		} catch (Exception e) {
			log.debug("从token获取手机号码失败");
		}
		return phone;
	}

	/**
	 * 获取用户phone
	 * 
	 * @return
	 */
	public static String getPhone(String token) {
		String phone = null;
		try {
			if (!isNull(token)) {
				return phone = (String) (getMapFromTokenStr(token).get(TOKEN_PHONE));
			}
		} catch (Exception e) {
			log.debug("从token获取手机号码失败");
		}
		return phone;
	}

	/**
	 * 获取账号
	 * 
	 * @return
	 */
	public static String getAcct(HttpServletRequest requset) {
		String acct = null;
		try {
			String authorization = requset.getHeader(AUTHORIZATION);
			if (isNull(authorization)) {
				authorization = getToken(requset);// requset.getParameter(AUTHORIZATION);
			}
			if (!isNull(authorization)) {
				return acct = (String) (getMapFromTokenStr(authorization).get(TOKEN_ACCT));
			}
		} catch (Exception e) {
			log.debug("从token获取账号失败");
		}
		return acct;
	}

	/**
	 * 获取账号
	 * 
	 * @return
	 */
	public static String getAcct(String token) {
		String acct = null;
		try {
			if (!isNull(token)) {
				return acct = (String) (getMapFromTokenStr(token).get(TOKEN_ACCT));
			}
		} catch (Exception e) {
			log.debug("从token获取账号失败");
		}
		return acct;
	}

	/**
	 * 
	 * @Description (创建token)
	 * @param userid  用户id
	 * @param acct    账号
	 * @param phone   手机号码
	 * @return String
	 * @date 2016年2月4日下午10:16:41
	 * @author qiufh
	 */
	public static String createToken(String userid, String acct, String phone) {
		try {
			// 当前时间截
			Long time = System.currentTimeMillis();
			Map<String, String> map = new HashMap<String, String>();
			// 组装
			if (userid != null) {
				map.put(USER_ID, String.valueOf(userid));// ID标识
			}
			if (StringUtils.isNotEmpty(acct)) {
				map.put(TOKEN_ACCT, String.valueOf(acct));// 账号标识
			}
			if (StringUtils.isNotEmpty(phone)) {
				map.put(TOKEN_PHONE, phone);// 手机号
			}
			map.put(EXP_TIME, time + EXPIRETTIME * 1000 + "");// 设置过期时间
			map.put(TOKEN_TIME, time + "");// 创建时间
			// 生成
			String token = createTokenStr(map);
			return token;
		} catch (Exception e) {
			log.debug("创建token失败");
		}
		return null;
	}

	/**
	 * 
	 * @Description (创建token)
	 * @param userid 用户id
	 * @return String
	 * @date 2016年2月4日下午10:16:00
	 * @author qiufh
	 */
	public static String createToken(Long userid) {
		try {
			// 当前时间截
			Long time = System.currentTimeMillis();

			Map<String, String> map = new HashMap<String, String>();
			// 组装
			map.put(USER_ID, String.valueOf(userid));// 会员ID标识
			map.put(EXP_TIME, time + EXPIRETTIME * 1000 + "");// 设置过期时间
			map.put(TOKEN_TIME, time + "");// 创建时间
			// 生成
			String token = createTokenStr(map);
			return token;
		} catch (Exception e) {
			log.debug("创建token失败");
		}
		return null;
	}

	/**
	 * 
	 * @Description (根据name获取cookie值)
	 * @param req
	 * @param name  保存在cookie里面的key
	 * @return String
	 * @date 2016年2月15日下午3:17:25
	 * @author qiufh
	 * @throws UnsupportedEncodingException 
	 */
	public static String getValueFromCookieByName(HttpServletRequest req, String name) throws UnsupportedEncodingException {
		// 获取request里面的cookie cookie里面存值方式也是 键值对的方式 
		String value = null;
		Cookie[] cookies = req.getCookies();
		boolean isToken = false;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cook = cookies[i];
				if (cook.getName().equalsIgnoreCase(name)) { //获取键 
					value = URLDecoder.decode(cook.getValue().toString(), "UTF-8");
					break;
				}
			}
		}
		return value;
	}

	/**
	 * 
	 * @Description (创建短信验证码token)
	 * @param str mobile+code
	 * @return String
	 * @date 2016年4月4日下午10:16:00
	 * @author jsan
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String createSmsToken(String str) {
		try {
			// 当前时间截
			Long time = System.currentTimeMillis() + 600 * 1000;
			Map map = new HashMap();
			// 组装
			map.put(AUTHORIZATION, str);// 验证串
			map.put(EXP_TIME, time);// 创建时间
			// 生成
			String token = createTokenStr(map);
			return token;
		} catch (Exception e) {
			log.debug("创建token失败");
		}
		return null;
	}

	/**
	 * 
	 * @Description (检查短信token是否过期)
	 * @param token
	 * @return boolean
	 * @date 2016年2月15日下午3:56:45
	 * @author qiufh
	 */
	public static boolean isSmsTokenExpired(String token) {
		boolean isToken = false;
		if (StringUtils.isNotEmpty(token)) {
			String expiretTime = (String) (getMapFromTokenStr(token).get(EXP_TIME));
			if (Long.valueOf(expiretTime) > System.currentTimeMillis()) {
				isToken = true;
			} else {
				log.debug("token失效");
			}
		} else {
			log.debug("token is null");
		}
		return isToken;
	}

	/**
	 * 
	 * @Description (检查短信token)
	 * @param token
	 * @param str = mobile+code
	 * @return boolean
	 * @date 2016年2月15日下午3:56:45
	 * @author qiufh
	 */
	public static boolean isSmsToken(String token, String str) {
		boolean isToken = false;
		if (StringUtils.isNotEmpty(token)) {
			log.info("tokenstr:" + getMapFromTokenStr(token));
			if (getMapFromTokenStr(token).get(AUTHORIZATION).equals(str)) {
				isToken = true;
			} else {
				log.debug("token检验失败");
			}
		} else {
			log.debug("token is null");
		}
		return isToken;
	}

	/**
	 * 获取用户手机
	 * @param req
	 * @return
	 */
	public static String getPhoneFromToken(HttpServletRequest req) {
		String token = null;
		String phone = null;
		try {
			token = getValueFromCookieByName(req, CommonParams.COKIE_NAME);
			phone = getPhone(token);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return phone;
	}

	public static void main(String[] args) {
		String a = getPhone("619f8c72979e4a0ebba1ae70c88326eb");

	}

}