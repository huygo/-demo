package cn.easier.brow.comm.util;

import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

public class PhoneUtils {

	private static String PHONE_SEARCH_URL = "http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=PHONENUMBER";

	/**
	 * 正则表达式：验证手机号
	 */
	public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

	/**
	 * 校验手机号
	 * 
	 * @param mobile
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isMobile(String mobile) {
		return Pattern.matches(REGEX_MOBILE, mobile);
	}

	/**
	 * 检查是否合法的手机号码
	 * 
	 * isPhoneNumber boolean xuzhaojie -2016-10-20 下午5:07:05
	 */
	public static boolean isPhoneNumber(String phone) {
		if (phone == null || "".equals(phone))
			return false;
		return isMobile(phone);
	}

	private static PhoneInfo getPhoneInfo(String moblie) {
		String url = PHONE_SEARCH_URL.replace("PHONENUMBER", moblie);

//		String url = url.replace("PHONENUMBER", PHONE_SEARCH_URL);
		String data = HttpUtils.getUrl(url);
		data = data.substring(data.indexOf('{'));
		return JSONObject.parseObject(data, PhoneInfo.class);
	}

	/**
	 * 检查是否是联通号段号码
	 * 
	 */
	public static boolean isGDUnicomPhone(String phone) {
		PhoneInfo info = getPhoneInfo(phone);
		if ("广东联通".equals(info.getCarrier()))
			return true;
		if ("广东".equals(info.getProvince()) && "中国联通".equals(info.getCatName()))
			return true;
		return false;
	}
	/**
	 * 获取号码归属地
	 * 
	 */
	public static String getPhoneProvince(String phone) {
		PhoneInfo info = getPhoneInfo(phone);
		return info.getProvince();
	}

	public static String maskingPhone(String phone) {
		// 括号表示组，被替换的部分$n表示第n组的内容
		phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
		return phone;
	}
	
	public static void main(String[] args) {
		System.out.println(getPhoneProvince("13877710118"));
	}
}
