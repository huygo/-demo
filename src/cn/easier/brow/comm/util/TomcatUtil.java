package cn.easier.brow.comm.util;

import javax.servlet.http.HttpServletRequest;

public class TomcatUtil {
	/**
	 * Tomcat路径
	 * 
	 * @return
	 */
	public static String getTomcatPath(HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		path = path.substring(0, path.lastIndexOf("webapps") - 1) + "/";
		return path;
	}

}
