package cn.easier.brow.comm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class FileUtils {
	private static Logger log = LogManager.getLogger(FileUtils.class);

	private static int i = 999;

	public final static String DATA_FORMAT = "yyyyMMddHHmmssSSS";
	private static SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT);

	public synchronized static String createID(String prefix) {
		i++;
		if (i > 9999) {
			i = 1000;
		}
		if (prefix != null)
			return prefix + sdf.format(new Date()) + i;
		return "ID" + sdf.format(new Date()) + i;
	}

	public static String createID() {
		return createID("ID");
	}

	public static boolean createDirIfNotFound(String dirpath) {
		File file = new File(dirpath);
		if (!file.exists()) {
			log.info("文件路径不存在!创建文件路径:" + dirpath);
			if (file.mkdirs()) {
				log.info("文件路径创建成功!");

			} else {
				log.error("文件路径创建失败!");
				return false;
			}
		} else {
			log.info("文件夹存在!绝对路径为:" + dirpath);
		}
		return true;
	}

	public static boolean createFileIfNotFound(String filepath) {
		File file = new File(filepath);
		if (!file.exists()) {
			String dirpath = filepath.substring(0,
					filepath.lastIndexOf(File.separator));
			if (createDirIfNotFound(dirpath)) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					log.error("文件创建失败！" + filepath);
					e.printStackTrace();
					return false;
				}
			} else {
				log.error("创建路径失败!" + dirpath);
				return false;
			}
		} else {
			log.info("文件存在!直接修改文件,绝对路径为:" + filepath);
		}
		return true;
	}

	/**
	 * 判断是否符合文件后缀 xuzhaojie -2016-9-14 上午11:54:27
	 */
	public static boolean isFitFormat(String filename, String... fotmat) {
		// 如果文件名为空或者格式，则直接错误
		if ((filename == null && "".equals(filename)) || fotmat == null)
			return false;
		String name = filename.substring(filename.lastIndexOf(".") + 1,
				filename.length());
		for (String str : fotmat) {
			if (name.equalsIgnoreCase(str)) {
				return true;
			}
		}
		return false;
	}

	public static List<File> list(String path, String... fotmat) {
		List<File> list = null;
		if (path == null || "".equals(path)) {
			log.error("路径不能为空");
			return list;
		}
		File file = new File(path);
		if (!file.exists()) {
			log.error("路径不存在");
			return list;
		}
		if (!file.isDirectory()) {
			log.error("传入的路径要文件夹");
			return list;
		}
		Stack<File> stack = new Stack<File>();
		list = new LinkedList<File>();
		stack.push(file);
		while (stack.peek() != null) {
			File curFile = stack.peek();
			for (File f : curFile.listFiles()) {
				if (f.isFile()) {
					String name = f.getName().substring(
							f.getName().lastIndexOf(".") + 1,
							f.getName().length());
					for (String s : fotmat) {
						if (name.equalsIgnoreCase(s)) {
							list.add(f);
							break;
						}
					}
				} else {
					// stack.
				}
			}
		}
		return list;
	}

	public static String readTxtFile(String filePath) {
		StringBuffer sb = new StringBuffer();
		try {
			String encoding = "UTF-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sb.append(lineTxt);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 更改文件名
	 * 
	 * renameFileName String xuzhaojie -2016-11-24 上午9:30:14
	 */
	public static String renameFileName(String filename, String newName) {
		String suffix = filename.substring(filename.lastIndexOf("."));
		return newName + suffix;
	}

	public static void main(String[] args) {
		String filename = "E:" + File.separator + "mykiddie" + File.separator
				+ "dd.xls";
		System.out.println(renameFileName(filename, "newName"));
		File file = new File(filename);
		System.out.println(file.getAbsolutePath());
	}
}
