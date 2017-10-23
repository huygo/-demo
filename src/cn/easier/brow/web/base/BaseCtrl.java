package cn.easier.brow.web.base;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;

import cn.easier.brow.comm.util.CommonParams;

@Controller("baseCtrl")
public class BaseCtrl {
	protected final Logger log = Logger.getLogger(CommonParams.LOG_SYS);

	public void writeJson(HttpServletResponse response, Object object) {
		try {
			String str = JSONObject.toJSONString(object);
			response.setContentType("text/json");
			response.setCharacterEncoding("utf-8");
			ServletOutputStream os = response.getOutputStream();
			IOUtils.write(str, os, "utf-8");
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final static String COKIE_NAME = "wov_admin_token";	
}
