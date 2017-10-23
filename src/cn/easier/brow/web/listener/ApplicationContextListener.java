package cn.easier.brow.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.easier.brow.comm.util.AliOcsMemcachedUtil;
import cn.easier.brow.comm.util.SpringBeanUtil;

public class ApplicationContextListener implements ServletContextListener {
	//private static Logger logger = Logger.getLogger(CommonParams.LOG_SYS);

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void contextInitialized(ServletContextEvent context) {
		initSpring(context);
		//initProp(context); // 必须预先加载配置
		//(new TaskThread()).start();
		initMemcached();
	}

	private void initSpring(ServletContextEvent context) {
		// spring 工厂类
		SpringBeanUtil.springContext = WebApplicationContextUtils.getWebApplicationContext(context.getServletContext());
	}

	/*private void initProp(ServletContextEvent context) {
		// 初始化prop配置文件
		Properties properties = new Properties();
		try {
			InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/config.properties"), "UTF-8");
			properties.load(reader);
			//ConfigUtil.setConfig(properties);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}*/

	private void initMemcached() {
		AliOcsMemcachedUtil.initMemcached();
	}
	
}
