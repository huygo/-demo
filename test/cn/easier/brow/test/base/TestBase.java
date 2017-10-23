package cn.easier.brow.test.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.easier.brow.comm.util.CommonParams;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-core.xml")
public abstract class TestBase {
	protected final Logger log = Logger.getLogger(CommonParams.LOG_SYS);

	long start = 0;
	long end = 0;

	@Before
	public void init() {
		start = System.currentTimeMillis();
	}

	@After
	public void destory() {
		end = System.currentTimeMillis();
		log.debug("调用消耗时间（毫秒）：" + (end - start));
	}

	// 请求地址
	public static String SERVERURI = "http://localhost:8080/dlt/";

	public static final String AUTHORIZATION = "token_auth_str";

	// 有效期一年
	public static String TOKEN = "9e8967548e6d29a69e35f6a1d0dc972d5fbc259a25816f207fb32d5fd1c723ef69f007b9f80a99f4fb00396bff9532ffc0cf71964656800ce0630bf88a388655";

	/**
	 * POST
	 * 
	 * @param url
	 * @param body
	 */
	public String postLogin(String url, String body) {
		String ret = "";
		try {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			StringEntity strpar = new StringEntity(body, "utf-8");
			httpPost.setHeader("ACCEPT", "application/json");
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader(AUTHORIZATION, TOKEN);
			httpPost.setEntity(strpar);
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			if (entity != null) {

				InputStream is = entity.getContent();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				String str;
				StringBuffer sb = new StringBuffer();
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
				ret = sb.toString();

				is.close();
				isr.close();
				br.close();
			}

			httpPost.abort();

			httpclient.getConnectionManager().shutdown();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * POST
	 * 
	 * @param url
	 * @param body
	 */
	public void postMethod(String url, String body) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			System.out.println("请求URI： " + httpPost.getURI());
			System.out.println("请求体body： " + body);
			StringEntity strpar = new StringEntity(body, "UTF-8");
			httpPost.setHeader("ACCEPT", "application/json");
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader(AUTHORIZATION, TOKEN);
			httpPost.setEntity(strpar);
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			System.out.println(response.getStatusLine().toString());
			System.out.println("----------------------------------------");
			if (entity != null) {
				System.out.println("Response content length: " + entity.getContentLength());
				InputStream is = entity.getContent();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				String str;
				StringBuffer sb = new StringBuffer();
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
				System.out.println("请求返回值： " + sb.toString());
				is.close();
				isr.close();
				br.close();
			}
			System.out.println("----------------------------------------");
			httpPost.abort();
			httpclient.getConnectionManager().shutdown();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * DELETE
	 * 
	 * @param url
	 */
	public void deleteMethod(String url) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpDelete httpDelete = new HttpDelete(url);
			System.out.println("executing request " + httpDelete.getURI());
			httpDelete.setHeader("ACCEPT", "application/json");
			httpDelete.setHeader("Content-Type", "application/json;charset=UTF-8");
			httpDelete.setHeader(AUTHORIZATION, TOKEN);
			HttpResponse response = httpclient.execute(httpDelete);
			HttpEntity entity = response.getEntity();
			System.out.println(response.getStatusLine().toString());
			System.out.println("----------------------------------------");
			if (entity != null) {
				System.out.println("Response content length: " + entity.getContentLength());
				InputStream is = entity.getContent();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				String str;
				StringBuffer sb = new StringBuffer();
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
				System.out.println(sb.toString());
				is.close();
				isr.close();
				br.close();
			}
			System.out.println("----------------------------------------");
			httpDelete.abort();
			httpclient.getConnectionManager().shutdown();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * PUT
	 * 
	 * @param url
	 */
	public void putMethod(String url, String body) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPut httpPut = new HttpPut(url);
			System.out.println("executing request " + httpPut.getURI());
			StringEntity strpar = new StringEntity(body, "utf-8");
			httpPut.setEntity(strpar);
			httpPut.setHeader("ACCEPT", "application/json");
			httpPut.setHeader(AUTHORIZATION, TOKEN);
			httpPut.setHeader("Content-Type", "application/json;charset=UTF-8");
			HttpResponse response = httpclient.execute(httpPut);
			HttpEntity entity = response.getEntity();
			System.out.println(response.getStatusLine().toString());
			System.out.println("----------------------------------------");
			if (entity != null) {
				System.out.println("Response content length: " + entity.getContentLength());
				InputStream is = entity.getContent();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				String str;
				StringBuffer sb = new StringBuffer();
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
				System.out.println(sb.toString());
				is.close();
				isr.close();
				br.close();
			}
			System.out.println("----------------------------------------");
			httpPut.abort();
			httpclient.getConnectionManager().shutdown();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * GET
	 * 
	 * @param url
	 */
	public void getMethod(String url) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			System.out.println("executing request " + httpget.getURI());
			httpget.setHeader("ACCEPT", "application/json");
			httpget.setHeader(AUTHORIZATION, TOKEN);
			httpget.setHeader("Content-Type", "application/json;charset=UTF-8");
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine().toString().toString());
			if (entity != null) {
				System.out.println("Response content length: " + entity.getContentLength());
				InputStream is = entity.getContent();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				String str;
				StringBuffer sb = new StringBuffer();
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
				System.out.println(sb.toString());
				is.close();
				isr.close();
				br.close();
			}
			System.out.println("----------------------------------------");
			httpget.abort();
			httpclient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String httpRequest(String url, String content, String method) {
		HttpURLConnection connect = null;
		String response = null;
		try {
			URL u = new URL(url);
			System.out.println("executing request " + url);
			System.out.println(content);
			connect = (HttpURLConnection) u.openConnection();
			connect.setDoOutput(true);
			connect.setDoInput(true);
			connect.setRequestMethod(method);
			connect.setUseCaches(false);
			connect.setRequestProperty("accept-encoding", "gzip,deflate");
			connect.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			connect.setRequestProperty("ACCEPT", "application/json");
			connect.setRequestProperty(AUTHORIZATION, TOKEN);
			connect.connect();
			OutputStreamWriter wr = null;
			if (method != null && !method.equals("GET") && !method.equals("DELETE")) {
				wr = new OutputStreamWriter(connect.getOutputStream());
				wr.write(content);
				wr.flush();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream(), "UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			response = buffer.toString();
			System.out.println("----------------------------------------");
			System.out.println(connect.getResponseMessage() + "____________" + connect.getResponseCode());
			System.out.println(response);
			System.out.println("----------------------------------------");
			if (wr != null) {
				wr.close();
			}
			if (reader != null) {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connect != null) {
				connect.disconnect();
			}
		}
		return response;
	}

	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
}
