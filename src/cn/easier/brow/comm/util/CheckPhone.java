package cn.easier.brow.comm.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.NodeList;

/** 
 * 手机号归属地查询工具类 
 * @author zhaozuofa 
 * 2015-11-3 下午7:07:25 
 * 
 */
public class CheckPhone {

	/** 
	 * 中国电信号码格式验证 手机段： 133,153,180,181,189,177,1700 
	 * **/
	private static final String CHINA_TELECOM_PATTERN = "(^1(33|53|77|8[019])\\d{8}$)|(^1700\\d{7}$)";

	/** 
	 * 中国联通号码格式验证 手机段：130,131,132,155,156,185,186,145,176,1709 
	 * **/
	private static final String CHINA_UNICOM_PATTERN = "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)";

	/** 
	 * 中国移动号码格式验证 
	 * 手机段：134,135,136,137,138,139,150,151,152,157,158,159,182,183,184 
	 * ,187,188,147,178,1705 
	 * **/
	private static final String CHINA_MOBILE_PATTERN = "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";

	private static String getSoapRequest(String mobileCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + " "
				+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" + " "
				+ "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "\n" + "<soap:Body>" + "\n" + "<getMobileCodeInfo"
				+ " " + "xmlns=\"http://WebXml.com.cn/\">" + "\n" + "<mobileCode>" + mobileCode + "</mobileCode>" + "\n"
				+ "<userID></userID>" + "\n" + "</getMobileCodeInfo>" + "\n" + "</soap:Body>" + "\n" + "</soap:Envelope>");
		return sb.toString();

	}

	/**
	 * 免费用户24小时内不超过100次
	 * @Description (请用一句话描述)
	 * @param mobileCode
	 * @return InputStream
	 * @date 2017年4月22日下午11:58:24
	 * @author qiufh
	 */
	private static InputStream getSoapInputStream(String mobileCode) {
		try {
			String soap = getSoapRequest(mobileCode);
			if (soap == null)
				return null;
			URL url = new URL("http://www.webxml.com.cn/WebServices/MobileCodeWS.asmx");
			URLConnection conn = url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(soap.length()));
			conn.setRequestProperty("SOAPAction", "http://WebXml.com.cn/getMobileCodeInfo");
			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
			osw.write(soap);
			osw.flush();
			osw.close();
			osw.close();
			InputStream is = conn.getInputStream();
			return is;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 
	 * @Description (获取号码信息)
	 * @param mobileCode
	 * @return String
	 * @date 2016年11月3日下午2:59:20
	 * @author qiufh
	 */
	public static String getMobileNoTrack(String mobileCode) {
		try {
			org.w3c.dom.Document document = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			InputStream is = getSoapInputStream(mobileCode);
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(is);
			NodeList nl = document.getElementsByTagName("getMobileCodeInfoResult");
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < nl.getLength(); i++) {
				org.w3c.dom.Node n = nl.item(i);
				if (n.getFirstChild().getNodeValue().equals("手机号码错误")) {
					sb = new StringBuffer("#");
					System.out.println("手机号码输入有误");
					break;
				}
				sb.append(n.getFirstChild().getNodeValue() + "\n");
			}
			is.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @Description (获取号码归属地)
	 * @param mobile
	 * @return String
	 * @date 2016年11月3日下午2:58:32
	 * @author qiufh
	 */
	public static String getMobileAttribution(String mobile) {
		String str = "";
		str = CheckPhone.getMobileNoTrack(mobile);
		if (str != null && !"".equals(str)) {
			str = str.substring(str.indexOf("：") + 1);
			String strArry[] = new String[] {};
			strArry = str.split(" ");
			str = strArry[0];
		}
		return str;
	}
	/**
	 * 
	 * @Description (获取号码归所在城市)
	 * @param mobile
	 * @return String
	 * @date 2016年11月3日下午2:58:32
	 * @author qiufh
	 */
	public static String getMobileCity(String mobile) {
		String str = "";
		str = CheckPhone.getMobileNoTrack(mobile);
		if (str != null && !"".equals(str)) {
			str = str.substring(str.indexOf("：") + 1);
			String strArry[] = new String[] {};
			strArry = str.split(" ");
			str = strArry[1];
		}
		return str;
	}

	/** 
	 * 验证【电信】手机号码的格式 
	 *  
	 * @author LinBilin 
	 * @param str 
	 *            校验手机字符串 
	 * @return 返回true,否则为false 
	 */
	public static boolean isChinaTelecomPhoneNum(String mobile) {
		return mobile == null || mobile.trim().equals("") ? false : match(CHINA_TELECOM_PATTERN, mobile);
	}

	/** 
	 * 验证【联通】手机号码的格式 
	 *  
	 * @author LinBilin 
	 * @param str 
	 *            校验手机字符串 
	 * @return 返回true,否则为false 
	 */
	public static boolean isChinaUnicomPhoneNum(String mobile) {
		return mobile == null || mobile.trim().equals("") ? false : match(CHINA_UNICOM_PATTERN, mobile);
	}

	/** 
	 * 验证【移动】手机号码的格式 
	 *  
	 * @author LinBilin 
	 * @param str 
	 *            校验手机字符串 
	 * @return 返回true,否则为false 
	 */
	public static boolean isChinaMobilePhoneNum(String mobile) {
		return mobile == null || mobile.trim().equals("") ? false : match(CHINA_MOBILE_PATTERN, mobile);
	}

	/** 
	 * 执行正则表达式 
	 *  
	 * @param pat 
	 *            表达式 
	 * @param str 
	 *            待验证字符串 
	 * @return 返回true,否则为false 
	 */
	private static boolean match(String pat, String str) {
		Pattern pattern = Pattern.compile(pat);
		Matcher match = pattern.matcher(str);
		return match.find();
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10000; i++) {
			
			//System.out.println("号码详细信息：	" + CheckPhone.getMobileNoTrack("18679035905"));
			//System.out.println(i+"===>号码归属地：	" + CheckPhone.getMobileAttribution("18679035905"));
			System.out.println(i+"号码所在城市：	" + CheckPhone.getMobileCity("18679035905"));
			/*System.out.println("校验移动号码：	" + CheckPhone.isChinaMobilePhoneNum("13580559682"));
		System.out.println("校验电信号码：	" + CheckPhone.isChinaTelecomPhoneNum("13580559682"));
			 */
			//System.out.println("校验联通号码：	" + CheckPhone.isChinaUnicomPhoneNum("18565219853"));
		}
	}

}