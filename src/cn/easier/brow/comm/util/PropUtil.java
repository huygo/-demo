package cn.easier.brow.comm.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropUtil {
	private static Logger log = Logger.getLogger(CommonParams.LOG_SYS);
	public static Properties propers = new Properties();
	private static PropUtil prop = new PropUtil();

	private PropUtil() {
	}

	static {
		initDbByInputStream(PropUtil.class.getClassLoader().getResourceAsStream("config.properties"));
	}

	public static PropUtil getInstance() {
		if (prop == null) {
			prop = new PropUtil();
		}
		return prop;
	}

	private static void initDbByInputStream(InputStream input) {
		try {
			load(input, propers);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * zhanghs77@gmail.com
	 * 
	 * @param filePath
	 *            配置文件路径
	 * @param prop
	 *            读取配置参数对象
	 * @throws Exception
	 *             抛出异常对象 此方法用于读取配置文件参数
	 */
	@SuppressWarnings("unused")
	private static void load(String filePath, Properties prop) throws Exception {
		InputStream is = null;
		FileInputStream fis = null;
		if (null == filePath || "".equals(filePath.trim())) {
			throw new Exception("配置文件路径为空");
		}
		if (prop == null) {
			log.error("配置文件properties为空");
			return;
		}
		log.debug("加载配置文件properties:" + filePath);

		try {
			fis = new FileInputStream(filePath);
			is = new BufferedInputStream(fis);
			prop.load(is);
		} catch (IOException e) {
			log.error("读取文件失败" + e);
		} finally {
			close(fis, is);
		}

		log.debug("加载配置文件properties:" + filePath + "成功");

	}

	/**
	 * zhanghs77@gmail.com
	 * 
	 * @param input
	 *           数据库配置文件流
	 * @param prop
	 *            读取配置参数对象
	 * @throws Exception
	 *             抛出异常对象 此方法用于读取配置文件参数
	 */
	private static void load(InputStream input, Properties prop) throws Exception {
		InputStream is = null;
		if (null == input) {
			throw new Exception("配置文件读取失败");
		}
		if (prop == null) {
			log.error("配置文件properties为空");
			return;
		}
		try {
			is = new BufferedInputStream(input);
			prop.load(is);
		} catch (IOException e) {
			log.error("读取文件失败" + e);
		} finally {
			close(input, is);
		}

		log.debug("加载数据库配置文件properties成功");

	}

	/**
	 * 关闭IO流
	 */
	private static void close(InputStream input, InputStream bis) {
		try {
			if (input != null) {
				input.close();
			}
			if (bis != null) {
				bis.close();
			}
		} catch (Exception e) {
			log.error("关闭文件流异常" + e);
		}
	}

	public static String get(String key) {
		return (String) PropUtil.propers.getProperty(key);
	}

	public static void main(String[] args) {
		System.out.println("" + PropUtil.get("stat.pvuv.log.file.path"));
	}

}
