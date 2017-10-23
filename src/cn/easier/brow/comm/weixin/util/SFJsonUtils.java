package cn.easier.brow.comm.weixin.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import cn.easier.brow.comm.util.DateUtils;

/**
 * JSON工具类
 * @version 2012-12-29
 * @author shenfei
 */
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class SFJsonUtils {

	/**
	 * JSON字符串转为指定JAVA对象
	 * 
	 * @param jsonString
	 * @param pojoCalss
	 * @return
	 */
	public static Object getObject4JsonString(String jsonString, Class pojoClass) {
		Object pojo = JSON.parseObject(jsonString, pojoClass);
		return pojo;
	}

	/**
	 * JSON对象转换为JSONObject
	 */
	public static JSONObject getJSONObject(Object obj) {
		JSONObject json = (JSONObject) JSON.toJSON(obj);
		return json;
	}

	/**
	 * JSON字符串转换成JSONObject
	 */
	public static JSONObject getJSONObject(String jsonString) {
		return JSON.parseObject(jsonString);
	}

	/**
	 * 从json HASH表达式中获取一个map，map支持嵌套功能
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Map getMap4Json(String jsonString) {
		Map valueMap = new HashMap();
		valueMap = (Map) JSON.parse(jsonString);
		return valueMap;
	}

	/**
	 * JSON数组字符串转换为JAVA数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Object[] getObjectArray4Json(String jsonString) {
		JSONArray jsonArray = JSON.parseArray(jsonString);
		return jsonArray.toArray();
	}

	/**
	 *JSON数组字符串转换为JAVA对象集合
	 * 
	 * @param jsonString
	 * @param pojoClass
	 * @return
	 */
	public static List getList4Json(String jsonString, Class pojoClass) {
		List<Object> list = JSON.parseArray(jsonString, pojoClass);
		return list;
	}

	/**
	 * JSON数组字符串转换为JAVA字符串数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static String[] getStringArray4Json(String jsonString) {
		// String[] stringArray = (String[])
		// JSON.parseArray(jsonString).toArray();
		Object[] objectArray = JSON.parseArray(jsonString).toArray();
		String str = "";
		String[] stringArray = new String[objectArray.length];
		int i = 0;
		for (Object c : objectArray) {
			str = (String) c;
			stringArray[i] = str;
			i++;
		}
		return stringArray;
	}

	/**
	 * 从json数组中解析出java Date 型对象数组，使用本方法必须保证
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Date[] getDateArray4Json(String jsonString, String dateFormat) {
		JSONArray jsonarray = JSON.parseArray(jsonString);
		Date[] dateArray = new Date[jsonarray.size()];
		String dateString;
		Date date;

		for (int i = 0; i < jsonarray.size(); i++) {
			dateString = jsonarray.getString(i);
			date = DateUtils.stringToDate(dateString, dateFormat);
			dateArray[i] = date;
		}
		return dateArray;
	}

	/**
	 * 将java对象转换成json字符串
	 * 
	 * @param javaObj
	 * @return
	 */
	public static String getJsonString4JavaPOJO(Object javaObj) {
		String jsonString = JSON.toJSONString(javaObj, configJson());
		return jsonString;
	}

	/**
	 * 将java对象转换成json字符串,并设定日期格式
	 * 
	 * @param javaObj
	 * @param dataFormat
	 * @return
	 */
	public static String getJsonString4JavaPOJO(Object javaObj, String dataFormat) {
		String jsonString = JSON.toJSONString(javaObj, configJson(dataFormat));
		return jsonString;
	}

	/**
	 * FASTJSON解析时间器具
	 * 
	 * @param datePattern
	 * @return
	 */
	public static SerializeConfig configJson(String datePattern) {
		SerializeConfig mapping = new SerializeConfig();
		mapping.put(Date.class, new SimpleDateFormatSerializer(datePattern));
		return mapping;
	}

	/**
	 * 判断JSON字符串是否为数组的
	 * 
	 * @param jsonString
	 * @return
	 */
	public static boolean isArrayJson(final String jsonString) {
		boolean result = false;

		if (jsonString != null && jsonString.trim().length() > 0) {

			int length = jsonString.length();
			String firstChar = jsonString.substring(0, 1); // 首字符
			String lastChar = jsonString.substring(length - 1, length); // 最后一个字符

			if (firstChar.equals("[") && lastChar.equals("]"))
				result = true;
		}

		return result;
	}

	/**
	 * 根据指定JSON字符串判断是否封装了多个对象
	 * 
	 * @param jsonString
	 * @return 返回1表示为对象数组，0表示为对象，-1表示非JSON字符串
	 */
	public static int existBeansFromJson(final String jsonString) {
		int result = -1;

		if (jsonString != null && jsonString.trim().length() > 0) {
			if (isArrayJson(jsonString))
				result = 1;
			else
				result = 0;
		}

		return result;
	}

	/**
	 * 判断JSON字符串是否为对象的
	 * 
	 * @param jsonString
	 * @return
	 */
	private static boolean isObjectJson(final String jsonString) {
		boolean result = false;

		if (jsonString != null && jsonString.trim().length() > 0) {

			int length = jsonString.length();
			String firstChar = jsonString.substring(0, 1); // 首字符
			String lastChar = jsonString.substring(length - 1, length); // 最后一个字符

			if (firstChar.equals("{") && lastChar.equals("}"))
				result = true;
		}

		return result;
	}

	/**
	 * List转换为JSON字符串
	 * 
	 * @param list
	 * @return
	 */
	public static String getJsonString(final List list) {
		return JSON.toJSONString(list, configJson());
	}

	public static String getJsonStringMap(final Map map) {
		return JSON.toJSONString(map, configJson());
	}

	/**
	 * @return JsonConfig 根据默认dataPattern生成的SerializeConfig
	 */
	public static SerializeConfig configJson() {
		SerializeConfig mapping = new SerializeConfig();
		mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
		return mapping;
	}

	// 返回格式
	public static String retrunDataFormate(String name, final List list) {
		StringBuffer buf = new StringBuffer();
		boolean flag = true;
		int i = 0;
		for (Object objList : list) {
			System.out.println("####转换后数据#####" + SFJsonUtils.getJsonString4JavaPOJO(objList));
			String string = "{" + name + ":" + SFJsonUtils.getJsonString4JavaPOJO(objList) + "}";
			System.out.println(i + "#############" + string);
			if (flag) {
				buf.append(string);
				flag = false;
			} else {
				buf.append("," + string);
			}
			i++;
		}

		return "[" + buf.toString() + "]";
	}

}
