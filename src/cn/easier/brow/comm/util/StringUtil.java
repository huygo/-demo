package cn.easier.brow.comm.util;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	/**
	 * 判断是否为非空 xuzhaojie -2016-9-13 上午9:56:52
	 */
	public static boolean isNotNull(Object... object) {
		return !isNull(object);
	}

	/**
	 * 过滤emoji表情 xuzhaojie -2016-9-13 上午9:57:16
	 */
	private static boolean isNotEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}
	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * 
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {
		if (source == null)
			return "";
		int len = source.length();
		StringBuilder buf = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (isNotEmojiCharacter(codePoint)) {
				buf.append(codePoint);
			} else {
				buf.append("*");
			}
		}
		return buf.toString();
	}
	/**
	 * 
	 * @Description ( 返回长度为【strLength】的随机数,strLength最小为4)
	 * @param strLength
	 * @return String
	 * @date 2016年6月4日下午7:54:02
	 * @author qiufh
	 */
	public static String getFixLenthString(int strLength) {
		// 十位随机数
		if (strLength < 4) {
			throw new RuntimeException("创建随机数失败：strLength < 4");
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strLength; i++) {
			sb.append((int) (10 * (Math.random())));
		}
		return sb.toString();
	}
	/**
	 * 
	 * @Description (获取指定长度唯一字符串：格式:prefix+yyyyMMdd+唯一数,例如：订单号)
	 * @param prefix
	 *            前缀
	 * @param length
	 *            随机数长度
	 * @return String 最终返回长度：prefix.length+8+length
	 * @date 2016年6月4日下午8:16:29
	 * @author qiufh
	 */
	public static String createOnlyNum(String prefix, int length) {
		StringBuffer only = new StringBuffer();
		String curDate = DateUtils.dateToString(
				DateUtils.getCurrentDate("yyyyMMdd"), "yyyyMMdd");
		String onlyRandom = StringUtil.getFixLenthString(length);
		if (StringUtils.isEmpty(prefix)) {

		} else
			only.append(prefix);
		only.append(curDate);
		if (StringUtils.isEmpty(onlyRandom)) {

		} else
			only.append(onlyRandom);
		return only.toString();
	}
	/**
	 * 获取随机的字符
	 * 
	 * @param num
	 * @return
	 */
	public static String getRandomString(int length) {
		StringBuffer buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++) {
			sb.append(buffer.charAt(r.nextInt(range)));
		}
		return sb.toString();
	}

	public static boolean isNull(Object... objs) {
		for (Object obj : objs) {
			if (obj == null || "".equals(obj))
				return true;
		}
		return false;
	}

	/**
	 * 获取UUID字符串,(已去掉中划线，长度32位)
	 * 
	 * @return
	 * @throws Exception
	 */
	public static final String getUUID() {
		String old_uuid = UUID.randomUUID().toString();
		final String new_uuid = old_uuid.replaceAll("-", ""); //old_uuid.substring(0, 8) + old_uuid.substring(9, 13) + old_uuid.substring(14, 18) + old_uuid.substring(19, 23) + old_uuid.substring(24);
		return new_uuid;
	}

	/**
	 * 
	 * @Description (根据指导字符串获取数据字符串)
	 * @param str
	 * @return String
	 * @date 2016年1月15日下午4:13:27
	 * @author qiufh
	 */
	public static String confuse(String str) {
		if (str == null || str.trim().length() == 0) {
			return str;
		}
		char[] chs = str.toCharArray();
		Random ran = new Random();
		for (int i = 0, k = chs.length * 2; i < k; i++) {
			int first = ran.nextInt(k) % chs.length;
			char t = chs[first];
			int second = ran.nextInt(k) % chs.length;
			chs[first] = chs[second];
			chs[second] = t;
		}
		return new String(chs);
	}

	/**
	 * 
	 * @Description (获取（1到 num-1）位的随机数字)
	 * @param num
	 * @return int
	 * @date 2016年1月15日下午4:15:30
	 * @author qiufh
	 */
	public static int random(int num) {
		return (int) (Math.random() * num);
	}

	/**
	 * 
	 * @Description (获取百分比)
	 * @param x		除数
	 * @param total	被除数
	 * @return String
	 * @date 2016年2月24日上午11:42:19
	 * @author qiufh
	 */
	public static String getPercent(double x, double total) {
		String result = "";//接受百分比的值  
		double tempresult = x / total;
		/*NumberFormat nf = NumberFormat.getPercentInstance(); //注释掉的也是一种方法  
		nf.setMinimumFractionDigits(2); //保留到小数点后几位  
		result = nf.format(tempresult);*/
		DecimalFormat df1 = new DecimalFormat("0.00%");
		result = df1.format(tempresult);
		return result;
	}

	public static String getExcept(double x, double total) {
		String result = "";//接受百分比的值  
		Number tempresult = x / total;
		DecimalFormat df1 = new DecimalFormat("0.00");
		result = df1.format(tempresult);
		return result;
	}
}
