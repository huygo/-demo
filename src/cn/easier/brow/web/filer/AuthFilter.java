package cn.easier.brow.web.filer;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.easier.brow.comm.util.CommonParams;
import cn.easier.brow.comm.util.IpUtil;
import cn.easier.brow.comm.util.SessionUtil;

public class AuthFilter extends OncePerRequestFilter {

	private static Logger logger = Logger.getLogger(CommonParams.LOG_SYS);

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// 需要统计的uri
		String notFilter = "-admin-sms-demo-upload-";
		// 请求的uri
		String requestURI = request.getServletPath();
		if (requestURI.split("/").length > 1) {
			String prefix = requestURI.split("/")[1];
			String ipadd = IpUtil.getIpAddr(request);
			if (notFilter.indexOf(prefix) != -1) {
				Integer usid = (Integer) SessionUtil.getObjectAttribute(request, CommonParams.SESSION_USER_ID);
				Integer aid = (Integer) SessionUtil.getObjectAttribute(request, CommonParams.SESSION_ACT_ID);
				if (usid != null && aid != null) {
					logger.info("|" + ipadd + "|" + aid + "|" + usid + "|" + requestURI);
				} else {
					logger.info("|" + ipadd + "|1|10|" + requestURI);
				}
			}
		}
		// 继续
		doDefaultNext(request, response, chain);

	}

	private void doDefaultNext(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		chain.doFilter(request, response);
	}

}
