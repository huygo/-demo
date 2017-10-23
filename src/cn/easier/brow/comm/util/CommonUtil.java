package cn.easier.brow.comm.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CommonUtil {
	private static Logger logger = Logger.getLogger(CommonParams.LOG_SYS);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private static SimpleDateFormat sdfs = new SimpleDateFormat("yyyyMMddHHmmssS");

	private static SimpleDateFormat cnOrderSdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	private static Md5Util md5 = new Md5Util();

	/**
	 * 
	 * @param dd
	 * @return
	 */
	public static String formatDate(Date dd) {
		return sdf.format(dd);
	}

	/*
	 * 获取UUID
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString().replaceAll("-", "");
		return str.toUpperCase();
	}

	public static String getSysTime() {
		return sdf.format(new Date());
	}

	public static String getSysTimeWithMill() {
		return sdfs.format(new Date());
	}

	/**
	 * 
	 * @param is
	 * @param mntPath
	 * @return boolean 保存文件到挂在目录
	 */
	public static void savetoMntFileServer(InputStream is, String endFix) {
		String mntPath = "";//ConfigUtil.get("file.local.path") + endFix;
		BufferedOutputStream bos = null;
		try {
			File file = new File(mntPath);
			File dir = file.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			byte[] buff = new byte[2048];
			int len = 0;

			bos = new BufferedOutputStream(new FileOutputStream(file), 2048);
			while ((len = is.read(buff)) > 0) {
				bos.write(buff, 0, len);
			}
			bos.flush();
		} catch (Exception e) {
			logger.error("Save file Exception", e);
		} finally {
			if (null != bos) {
				try {
					bos.close();
				} catch (Exception ex) {
					logger.error("Stream close is exception", ex);
				}
			}
		}
	}

	public static String getCnOrderDate(String strDate) {
		try {
			return cnOrderSdf.format(sdf.parse(strDate));
		} catch (ParseException e) {
			logger.error("CommonUtil.getCnOrderDate exception");
			return "";
		}
	}

	public static String toMD5(String source) {
		return md5.getMD5ofStr(source);
	}

	public static Map[] toMapArray(JSONArray array) {
		JSONObject[] paramMap = new JSONObject[array.size()];
		array.toArray(paramMap);
		return paramMap;
	}

	public static String getToken() {
		return toMD5(getUUID());
	}

	public static String getRandString(int length) {
		String charList = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		String rev = "";
		java.util.Random f = new Random();
		for (int i = 0; i < length; i++) {
			rev += charList.charAt(Math.abs(f.nextInt()) % charList.length());
		}
		return rev;
	}

	public static void ajustObjectId(Map map) {
		Object o = map.get("_id");
		if (o != null)
			map.put("_id", o.toString());
	}

	public static String trimHTML(String html) {
		Document doc = Jsoup.parse(html);
		return doc.text();
	}
}
