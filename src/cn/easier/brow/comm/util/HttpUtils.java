package cn.easier.brow.comm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpUtils {
	/**
	 * 
	 * @param source
	 * @param key
	 * @return
	 */
	public static String getkey(String source, String key) {
		try {
			int index = source.indexOf("\"" + key + "\":\"");
			if (index > 0) {
				source = source.substring(index + 9);
				source = source.substring(0, source.indexOf("\""));
				return source;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param params
	 * @return
	 */
	private static String getParamString(TreeMap<String, Object> params) {

		String paramStr = "";
		if (params != null && params.size() > 0) {
			Iterator<Map.Entry<String, Object>> iter = params.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				paramStr += paramStr = "&" + key + "=" + val;
			}
		}
		return paramStr;
	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String httpGet(String url, TreeMap<String, Object> params) {
		HttpClient client = new DefaultHttpClient();
		try {
			String paramStr = getParamString(params);
			if (!paramStr.equals("")) {
				paramStr = paramStr.replaceFirst("&", "?");
				url += paramStr;
			}
			HttpGet get = new HttpGet(url);
			System.out.println(url);
			HttpResponse response = client.execute(get);
			return IOUtils.toString(response.getEntity().getContent(), "utf-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String httpPost(String url, TreeMap<String, Object> params) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try {
			String paramStr = getParamString(params);
			if (!paramStr.equals("")) {
				paramStr = paramStr.replaceFirst("&", "");
			}
			post.setEntity(new StringEntity(paramStr, "application/x-www-form-urlencoded", "utf-8"));
			HttpResponse response = client.execute(post);
			return IOUtils.toString(response.getEntity().getContent(), "utf-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getUrl(String url) {
		String result = null;
		try {
			// 根据地址获取请求
			HttpGet request = new HttpGet(url);
			// 获取当前客户端对象
			HttpClient httpClient = new DefaultHttpClient();
			// 通过请求对象获取响应对象
			HttpResponse response = httpClient.execute(request);

			// 判断网络连接状态码是否正常(0--200都数正常)
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * 
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyTrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			return buffer.toString();
		} catch (ConnectException ce) {
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * httppost返回结果转json
	 * @param url
	 * @param params
	 * @return
	 */
	public static JSONObject httpPostParseJson(String url, TreeMap<String, Object> params) {
		return JSON.parseObject(httpPost(url, params));
	}

	public static void main(String[] args) {
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("key", "30278fa8b7da33ece7d3e8d7b1da2c38");
		map.put("info", "国庆节");
		map.put("userid", 1233);
		String ret = httpPost("http://www.tuling123.com/openapi/api", map);
		JSONObject obj = JSON.parseObject(ret);
		String code = obj.getString("code");

		System.out.println(code + "," + ret);
	}
}