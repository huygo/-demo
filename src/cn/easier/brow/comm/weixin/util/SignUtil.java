package cn.easier.brow.comm.weixin.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.easier.brow.comm.util.CommonParams;

public class SignUtil {

	protected final static Logger log = LoggerFactory.getLogger(CommonParams.LOG_SYS);

	private static final String HEXDIGITS_MD5[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
			"f" };

	/**
	 * 获取随机字符串，生成签名
	 * 
	 * @return
	 */
	public static String getNonceStr() {
		Random random = new Random();
		return MD5Encode(String.valueOf(random.nextInt(10000)), "GBK");
	}

	/**
	 * 获取时间戳
	 * 
	 * @return
	 */
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return HEXDIGITS_MD5[d1] + HEXDIGITS_MD5[d2];
	}

	private static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	public static String getSha1(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes());
			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * @Description (构造签名)
	 * @param encode
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException String
	 * @date 2016年6月4日下午9:12:24
	 * @author qiufh
	 */
	public static String createSign(boolean encode, SortedMap<Object, Object> params) throws UnsupportedEncodingException {
		Set<Object> keysSet = params.keySet();
		Object[] keys = keysSet.toArray();
		Arrays.sort(keys);
		StringBuffer temp = new StringBuffer();
		boolean first = true;
		for (Object key : keys) {
			if (first) {
				first = false;
			} else {
				temp.append("&");
			}
			temp.append(key).append("=");
			Object value = params.get(key);
			String valueString = "";
			if (null != value) {
				valueString = value.toString();
			}
			if (encode) {
				temp.append(URLEncoder.encode(valueString, "UTF-8"));
			} else {
				temp.append(valueString);
			}
		}
		return temp.toString();
	}

	public static String clientSign(String paternerKey, SortedMap<Object, Object> params) throws UnsupportedEncodingException {
		String string1 = createSign(false, params); //得到第一个string字符串，然后拼接上商户的密钥
		String stringSignTemp = string1 + "&key=" + paternerKey;
		String signValue = MD5Util.MD5Encode(stringSignTemp, "").toUpperCase();
		return signValue;
	}

}
