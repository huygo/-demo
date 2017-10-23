package cn.easier.brow.comm.util;

import java.io.File;
import java.net.InetAddress;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

public class EasierUtils {
	/**
	 * Tomcat路径
	 * @return
	 */
	public static String getTomcatPath() {

		String path = System.getProperty("user.dir");
		path = path.replace("%20", " ");
		File f = new File(path);
		path = f.getParentFile().getPath();
		return path;
	}

	/**
	 * Tomcat路径
	 * @return
	 */
	public static String getTomcatPath(HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		path = path.substring(0, path.lastIndexOf("webapps") - 1);
		return path;
	}

	/**
	 * 项目Class路径
	 * @return
	 */
	public static String getClassPath() {
		try {
			return EasierUtils.class.getClassLoader().getResource("").toURI().getPath();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取项目网络路径(不包括项目名称如：http://192.168.0.165:8080/)
	 * @return
	 */
	public static String getNotworkPath(HttpServletRequest request) {
		try {
			//获取本地ip
			InetAddress localHost = InetAddress.getLocalHost();
			String localIp = localHost.getHostAddress();
			//拼接ip+端口(http://192.168.0.165:8080/)
			return request.getScheme() + "://" + localIp + ":" + request.getServerPort() + "/";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取项目地址
	 * 
	 * @return
	 */
	public static String getProjectPath(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append(request.getScheme() + "://"); // 请求协议 http 或 https
		sb.append(request.getHeader("host")); // 请求服务器
		sb.append(request.getRequestURI()); // 工程名
		return sb.toString();
	}

	/**
	 * 
	 * @Description (获取前端提交方式)
	 * @return String
	 * @date 2015-9-21上午10:45:25
	 * @author qiufahong
	 */
	public static String gainMethod(HttpServletRequest requset) {
		String method = requset.getMethod();
		boolean isPostMethod = "POST".equalsIgnoreCase(method);
		if (!isPostMethod) {
			return "GET";
		} else {
			return "POST";
		}
	}
}
