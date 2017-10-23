package cn.easier.brow.comm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * txt 读取写入创建
 * @Title TxtUtil
 * @Package cn.easier.pyd.comm.util
 * @author mengtx
 * @date 2017年4月28日上午10:47:44
 * @version V1.0
 * @Description (请用一句话描述)
 */

@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
public class TxtUtil {

	static Logger log = Logger.getLogger(ExcelUtil.class);
	private static XSSFWorkbook wb;
	private static XSSFSheet sheet;
	private static XSSFRow row;
	private static FileInputStream input;

	/**
	 * 将前台读取到的信息写入本地
	 * @Description (请用一句话描述)
	 * @throws Exception void
	 * @date 2017年4月28日上午10:56:00
	 * @author mengtx
	 */
	public boolean write(String phone) throws Exception {
		boolean flag = false;
		String temp = "";

		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			// 文件路径
			File file = new File(phone);
			if (!file.exists()) {
				file.createNewFile();
				file.mkdirs();
			}
			// 将文件读入输入流
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			StringBuffer buf = new StringBuffer();

			// 保存该文件原有的内容
			for (int j = 1; (temp = br.readLine()) != null; j++) {
				buf = buf.append(temp);
				// System.getProperty("line.separator")
				// 行与行之间的分隔符 相当于“\n”
				buf = buf.append(System.getProperty("line.separator"));
			}
			buf.append(phone);

			fos = new FileOutputStream(file);
			pw = new PrintWriter(fos);
			pw.write(buf.toString().toCharArray());
			pw.flush();
			return true;
		} catch (IOException e1) {
			throw e1;
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fos != null) {
				fos.close();
			}
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
	}

	public List<String> read(String path) throws Exception {
		File f = new File(path);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(f), "GBK");
		BufferedReader bf = new BufferedReader(reader);
		String str = null;
		List<String> list = new ArrayList<String>();
		while ((str = bf.readLine()) != null) {
			list.add(str);
		}
		if(bf != null){
			bf.close();
		}
		return list;
		
	}
	
	public static List<String> readUploadFile(InputStream fis) throws Exception {
		InputStreamReader reader = new InputStreamReader(fis,"GBK");
		BufferedReader bf = new BufferedReader(reader);
		String str = null;
		List<String> list = new ArrayList<String>();
		while ((str = bf.readLine()) != null) {
			list.add(str);
		}
		return list;
		
	}

	public static void writeLocal(List<Map<String, Object>> list, String realPath, String xlsName) throws Exception {
		log.info("将TXT保存到服务器");
		// 初始化变量
		FileOutputStream fileOut = null;
		String temp = "";
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			File targetFile = new File(realPath, xlsName);
			File file = new File(realPath);
			if (!file.exists()) {
				file.mkdir();
			}
			if (!targetFile.exists()) {
				targetFile.createNewFile();//创建文件
			}

			fis = new FileInputStream(targetFile);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			StringBuffer buf = new StringBuffer();
			for (int j = 1; (temp = br.readLine()) != null; j++) {
				buf = buf.append(temp);
				// System.getProperty("line.separator")
				// 行与行之间的分隔符 相当于“\n”
				buf = buf.append(System.getProperty("line.separator"));
			}
			for(int a = 0; a<list.size();a++){
				System.out.println("aaa");
			buf.append(list.get(a).toString());
			}
			fos = new FileOutputStream(targetFile);
			pw = new PrintWriter(fos);
			System.out.println(list.size()+"aaa");
			pw.write(buf.toString().toCharArray());
			pw.flush();

		} catch (Exception e) {
			// 抛出异常
			throw new RuntimeException("将Excel表保存到服务器失败,错误消息： " + e.getMessage(), e);
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fos != null) {
				fos.close();
			}
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (fis != null) {
				fis.close();
			}
		}

	}

	public static void downloadExcel(HttpServletResponse response, String realPath, String xlsName) {
		log.info("下载本地Excel到客户端");
		// 先建立一个文件读取流去读取这个临时excel文件
		FileInputStream fs = null;
		String excelName = null;
		OutputStream os = null;
		int len = 0;
		try {
			fs = new FileInputStream(realPath + xlsName);
			// 设置响应头和保存文件名
			excelName = URLEncoder.encode(xlsName, "UTF-8");
			//HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + excelName + "\"");
			response.setCharacterEncoding("utf-8");
			os = response.getOutputStream();
			// 写出流信息
			while ((len = fs.read()) != -1) {
				os.write(len);
			}
		} catch (Exception e) {
			// 抛出异常
			throw new RuntimeException("下载服务器TXT到客户端失败,错误消息： " + e.getMessage(), e);
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				os = null;
				e.printStackTrace();
			} finally {
				try {
					if (fs != null) {
						fs.close();
					}
				} catch (IOException e) {
					fs = null;
					e.printStackTrace();
				}
			}
			//删除源文件
			/*File file = new File(realPath + xlsName);
			if (file.isFile() & file.exists()) {
				file.delete();
			}*/
		}
	}

	public boolean newTxt(String realPath, String txtName) throws Exception {
		File targetFile = new File(realPath, txtName);
		File file = new File(realPath);
		if (!file.exists()) {
			file.mkdir();
		}
		if (!targetFile.exists()) {
			targetFile.createNewFile();//创建文件
		}
		return true;
	}

}
