package cn.easier.brow.comm.util;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigUtil {

	private ConfigUtil() {
	}

	private Properties config;

	@Value("#{config}")
	public void setConfig(Properties config) {
		this.config = config;
	}

	public String get(String key) {
		return config.getProperty(key);
	}

}
