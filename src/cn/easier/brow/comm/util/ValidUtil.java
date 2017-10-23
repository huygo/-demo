package cn.easier.brow.comm.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 校验相关工具类
 * @author lizh
 *
 */
public class ValidUtil {
	/**
	 * 检查集合是否为空(集合为空 或集合的长度为空)
	 * @param c
	 * @return 为空=true, 不为空=false
	 */
	public static boolean isEmpty(Collection<?> c) {
		if (null == c || c.isEmpty())
			return true;
		return false;
	}

	/**
	 * 检查Map是否为空(Map为空 或Map的长度为空)
	 * @param map
	 * @return 为空=true, 不为空=false
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		if (null == map || map.isEmpty())
			return true;
		return false;
	}

	/**
	 * 检查所有对象中是否为空(其中任意一个为空 返回true)
	 * @param args 需要校验的参数
	 * @return 为空=true 不为空=false
	 */
	public static boolean anyEmpty(Object... args) {
		for (Object o : args) {
			if (o == null)
				return true;
			else if (o instanceof String && StringUtils.isEmpty((String) o))
				return true;
			else if (o instanceof Integer && ((Integer) o) <= 0)
				return true;
			else if (o instanceof Long && ((Long) o) <= 0)
				return true;
			else if (o instanceof List<?> && ((List<?>) o).size() <= 0)
				return true;
			else if (o instanceof Map<?, ?> && ((Map<?, ?>) o).size() <= 0)
				return true;
		}
		return false;
	}

	/**
	 * 判断是否手机号码
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

}
