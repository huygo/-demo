package cn.easier.brow.comm.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.easier.brow.web.bean.ParamValiInfo;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class ParamValiUtil {

	public static String EX_MSG = "ex_msg";
	public static String EX_CODE = "ex_code";
	private static JSONObject jsonFile = null;
	private static JSONObject valiJsonMap = null;
	private static JSONObject patternMap = null;
	private static Map<String, Pattern> patternCache = new HashMap<String, Pattern>();
	private static Map<String, ParamValiInfo> valiInfoCache = new HashMap<String, ParamValiInfo>();
	private static Logger logger = Logger.getLogger(CommonParams.LOG_SYS);

	private static void init() {
		if (null == jsonFile) {
			logger.info(" ParamValiUtil.init ................");
			parseParamValiFile("");
		}
	}

	public static Map valiReqBody(String proNo, JSONObject reqBody) {
		init();
		Map res = new HashMap();
		res.put(EX_MSG, "验证通过");
		JSONObject jsonStruc = valiJsonMap.getJSONObject("H_" + proNo);
		//参数验证
		boolean isvali = fetchReqBody(res, proNo, jsonStruc, reqBody);
		if (isvali) {
			res.put(EX_CODE, "0"); //参数验证成功
		} else {
			res.put(EX_CODE, "1"); //参数验证失败
		}

		return res;
	}

	private static void parseParamValiFile(String filePath) {
		String jsonStr = readStreamToString(filePath);
		jsonFile = JSONObject.parseObject(jsonStr);
		patternMap = jsonFile.getJSONObject("patterns");
		valiJsonMap = jsonFile.getJSONObject("valis");
		parsePattern();
		Set<Map.Entry<String, Object>> set = valiJsonMap.entrySet();
		for (Map.Entry<String, Object> valiEntry : set) {
			fetch((Map<String, Object>) valiEntry.getValue());
		}
	}

	private static boolean fetchReqBody(Map ex, String proNo, Map<String, Object> jsonStruc, Map<String, Object> reqBody) {
		Set<Map.Entry<String, Object>> valiSet = jsonStruc.entrySet();
		try {
			for (Map.Entry<String, Object> entry : valiSet) {
				String key = entry.getKey();
				Object valiObj = entry.getValue();
				Class valiObjClass = valiObj.getClass();
				boolean res = false;
				if (reqBody.containsKey(key)) {
					if (String.class.isAssignableFrom(valiObjClass)) {
						res = valiParamInfo(valiInfoCache.get(valiObj), String.valueOf(reqBody.get(key)));
						if (!res) {
							ex.put(EX_MSG, "参数验证失败：接口编号：" + proNo + "，参数名：" + key);
							return false;
						}
					} else if (Map.class.isAssignableFrom(valiObjClass)) {
						fetchReqBody(ex, proNo, (Map<String, Object>) valiObj, (Map<String, Object>) reqBody.get(key));
					} else if (List.class.isAssignableFrom(valiObjClass)) {
						List<Map<String, Object>> list = (List<Map<String, Object>>) valiObj;
						List<Map<String, Object>> dstList = (List<Map<String, Object>>) reqBody.get(key);
						for (Map<String, Object> dstMap : dstList) {
							fetchReqBody(ex, proNo, list.get(0), dstMap);
						}
					}
				} else {
					ex.put(EX_MSG, "参数验证失败：接口编号：" + proNo + "，参数名：" + key);
					return false;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	private static void fetch(Map<String, Object> valiJsonMap) {
		Set<Map.Entry<String, Object>> valiSet = valiJsonMap.entrySet();
		try {
			for (Map.Entry<String, Object> entry : valiSet) {
				String key = entry.getKey();
				Object valiObj = entry.getValue();

				Class valiObjClass = valiObj.getClass();
				if (String.class.isAssignableFrom(valiObjClass)) {
					parseParamValiInfo(valiObj.toString());
				} else if (Map.class.isAssignableFrom(valiObjClass)) {
					fetch((Map<String, Object>) valiObj);
				} else if (List.class.isAssignableFrom(valiObjClass)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) valiObj;
					fetch(list.get(0));
				}
			}
		} catch (Exception e) {

			logger.error(e.getMessage(), e);
		}
	}

	private static boolean valiParamInfo(ParamValiInfo vali, String paramStr) {
		boolean res = false;
		if ("null".equals(paramStr) && vali.isNotMust())
			return true;
		if (paramStr.length() >= vali.getstrMin() && paramStr.length() <= vali.getstrMax()) {
			res = vali.getPattern().matcher(paramStr).matches();
		}
		return res;
	}

	private static void parsePattern() {
		Set<Map.Entry<String, Object>> set = patternMap.entrySet();
		for (Map.Entry<String, Object> entry : set) {
			patternCache.put(entry.getKey(), Pattern.compile(entry.getValue().toString()));
		}
	}

	private static void parseParamValiInfo(String regKey) {
		ParamValiInfo valiInfo = valiInfoCache.get(regKey);
		if (valiInfo == null) {
			int p = regKey.indexOf("(");
			int p2 = regKey.indexOf("!");
			int min = 0;
			int max = Integer.MAX_VALUE;
			String ptnKey = regKey;
			boolean notMust = false;
			if (p > 0) {
				String lenParam = regKey.substring(p).replaceAll("[()]", "");
				String[] paramArr = lenParam.split(",");
				min = Integer.parseInt(paramArr[0]);
				max = Integer.parseInt(paramArr[1]);
				ptnKey = regKey.replaceFirst("\\(.+\\)", "");
			}
			if (p2 > 0) {
				notMust = true;
				ptnKey = regKey.replace("!", "");
			}
			ParamValiInfo valiInfoObj = new ParamValiInfo(ptnKey, patternCache.get(ptnKey), min, max);
			valiInfoObj.setNotMust(notMust);
			valiInfoCache.put(regKey, valiInfoObj);
		}
	}

	/**
	 * 读取配json置文件
	 * 
	 * @param filePath
	 * @return
	 */
	private static String readStreamToString(String filePath) {
		InputStream ins = null;
		String jsonStr = null;
		try {
			if (filePath == null || "".equals(filePath)) {
				logger.debug(Thread.currentThread().getContextClassLoader().getResource("vali_parm.js").getPath());
				ins = Thread.currentThread().getContextClassLoader().getResourceAsStream("vali_parm.js");
			} else {
				ins = new FileInputStream(filePath);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (ins != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int num = 0;
			try {
				while ((num = ins.read(buf)) > 0) {
					bos.write(buf, 0, num);
				}
				jsonStr = bos.toString("UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonStr;
	}

	/**
	 * 
	 * @Description (校验token合法性)
	 * @param req
	 * @param name  保存在cookie里面的key
	 * @return boolean
	 * @date 2016年2月15日下午4:50:35
	 * @author qiufh
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean checkToken(HttpServletRequest req, String name) throws UnsupportedEncodingException {
		boolean isToken = false;
		String token = Tokener.getValueFromCookieByName(req, name);
		boolean falg = Tokener.isToken(token);
		if (falg) {
			String acct = Tokener.getPhone(token);
			String sessAcct = (String) SessionUtil.getObjectAttribute(req, CommonParams.SESSION_OPENID);
			if (StringUtils.isNotEmpty(acct) && StringUtils.isNotEmpty(sessAcct)) {
				if (acct.equals(sessAcct)) {
					isToken = true;
				}
			}
		}
		//isToken = true;
		return isToken;
	}

}
