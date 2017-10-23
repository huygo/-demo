package cn.easier.brow.comm.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class JsonUtils {
	private static Logger logger = Logger.getLogger(CommonParams.LOG_SYS);

	/**
	 * 
	 * @Description (输出json字符串)
	 * @param response
	 * @param json void
	 * @date 2017年1月17日上午11:15:24
	 * @author qiufh
	 */
	public static void outJsonAndClose(HttpServletResponse response, String json) {
		try {
			logger.info("response_json:" + json);
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			ServletOutputStream os = response.getOutputStream();
			IOUtils.write(json, os, "utf-8");
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description (json字符串转Map)
	 * @param jsonStr
	 * @return Map<String,Serializable>
	 * @date 2017年1月17日上午11:15:41
	 * @author qiufh
	 */
	public static Map<String, Object> jsonStringToMap(String jsonStr) {
		Map<String, Object> hashMap = JSONObject.parseObject(jsonStr, new TypeReference<HashMap<String, Object>>() {
		});
		return hashMap;
	}

}
