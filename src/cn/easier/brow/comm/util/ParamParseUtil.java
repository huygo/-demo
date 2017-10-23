package cn.easier.brow.comm.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.alibaba.fastjson.JSONObject;

import cn.easier.brow.web.bean.RequestMsg;

public class ParamParseUtil {
	private static Logger logger = Logger.getLogger(CommonParams.LOG_SYS);

	public static RequestMsg parseReqMsgWithjson(JSONObject jsonObj) {
		JSONObject base = jsonObj.getJSONObject("base");
		RequestMsg reqMsg = new RequestMsg();
		reqMsg.setReqTime(base.getString("reqTime"));
		reqMsg.setProNo(base.getString("proNo"));
		reqMsg.setToken((base.getString(Tokener.AUTHORIZATION)));
		reqMsg.setReqBody(jsonObj.getJSONObject("body"));
		reqMsg.setReqBase(base);
		return reqMsg;
	}

	public static RequestMsg parseReqMsg(String reqMsgStr) {
		JSONObject jsonObj = JSONObject.parseObject(reqMsgStr);
		return parseReqMsgWithjson(jsonObj);
	}

	public static String getRequestMsgStr(HttpServletRequest request, String charset) {
		String jsonStr = "";
		// 含有图片多媒体类型
		String contentType = request.getHeader("Content-Type");
		contentType = contentType != null ? contentType.toLowerCase() : "";
		logger.info("contentType:" + contentType);

		if (contentType.indexOf(CommonParams.FORM_MULTI) >= 0) {
			MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
			MultipartHttpServletRequest multipart = resolver.resolveMultipart(request);

			Map<String, MultipartFile> fileMap = multipart.getFileMap();

			request.setAttribute(CommonParams.PARAM_FILES, fileMap);

			String formJson = multipart.getParameter(CommonParams.FORM_JSON);
			jsonStr = (formJson == null) ? "" : formJson;
		} else if (contentType.equals("") || contentType.indexOf(CommonParams.FORM_NORMAL) >= 0) {
			String formJson = request.getParameter(CommonParams.FORM_JSON);
			String htmlStr = request.getParameter(CommonParams.FORM_HTML_STR);

			try {
				if (formJson != null)
					formJson = URLDecoder.decode(formJson, CommonParams.REQ_BODY_CHARSET);
				if (htmlStr != null)
					htmlStr = URLDecoder.decode(htmlStr, CommonParams.REQ_BODY_CHARSET);
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage(), e);
			}
			request.setAttribute(CommonParams.PARAM_HTML_STR, htmlStr);
			jsonStr = (formJson == null || "".equals(formJson)) ? "" : formJson;

		} else {
			// 普通body报文传输
			InputStream ins = null;

			try {
				ins = request.getInputStream();
			} catch (IOException e1) {
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
					jsonStr = bos.toString(charset);
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
		}
		// 解析设置RequestMsg
		logger.info("request_json:" + jsonStr);
		return jsonStr;
	}

	public static JSONObject parseToJsonWithSessionCheck(String str, HttpServletRequest request) {
		/**
		SessionUser ssoInfo = (SessionUser) SessionUtil.getObjectAttribute(
		        request, CommonParams.SESSION_USER);
		        **/
		JSONObject json = JSONObject.parseObject(str);
		/**
		if(ssoInfo != null)
		{
		    json.getJSONObject("base").put("s_user_id", ssoInfo.getUserId());
		}
		 **/
		json.getJSONObject("base").put("reqTime", CommonUtil.getSysTimeWithMill());

		return json;
	}
}
