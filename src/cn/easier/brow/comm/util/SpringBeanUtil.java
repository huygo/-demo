package cn.easier.brow.comm.util;

import org.springframework.context.ApplicationContext;

public class SpringBeanUtil {
	public static ApplicationContext springContext;

	public static Object getBean(String beanName) {
		return springContext.getBean(beanName);
	}
}
