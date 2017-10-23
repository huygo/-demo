package cn.easier.brow.comm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @Title: ExcelUtil.java
 * @Package cn.zeale.utils
 * @author 邱发红
 * @date 2015年10月20日 下午2:49:07
 * @version V1.0
 * @Description: (Excel导入导出,支持Excel2007版及以上)
 * @ModifyPeople (修改人:)
 * @ModifyDate (修改时间:2014年9月5日 下午2:49:07)
 * @ModifyContent (修改内容:)
 */
@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
public class ExcelUtil {

	static Logger log = Logger.getLogger(ExcelUtil.class);
	private static XSSFWorkbook wb;
	private static XSSFSheet sheet;
	private static XSSFRow row;
	private static FileInputStream input;

	/**
	 * 
	 * @Description (导出数据到Excel)
	 * @param dataList  数据列表
	 * @param rowName   列名
	 * @param cellName  属性名
	 * @param sheetName  工作簿的名称
	 * @return XSSFWorkbook 返回类型
	 * @date 2015-10-13下午2:11:00
	 * @author qiufh
	 */
	public static XSSFWorkbook exportToExcel(List<?> dataList, String[] rowName, String[] cellName, String sheetName) {
		if (dataList == null || dataList.size() == 0) {
			log.info("导出excel失败，没有数据");
			return null;
		}
		log.info("开始创建excel对象！");
		// 创建Excel的工作书册 Workbook,对应到一个excel文档
		XSSFWorkbook workbook = new XSSFWorkbook();
		// workbook.setSheetName(0, codedFileName, (short) 1); //
		// 这里(short)1是解决中文乱码的关键；而第一个参数是工作表的索引号。

		// 创建字体
		Font font = workbook.createFont();
		// 创建格式
		CellStyle cellStyle = workbook.createCellStyle();
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(font);
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);

		// 创建Excel的工作sheet,对应到一个excel文档的tab
		XSSFSheet sheet = workbook.createSheet(sheetName);
		sheet.setDefaultColumnWidth(15);// 表格宽度
		// 从第0行开始创建
		XSSFRow row = sheet.createRow(0);
		for (int i = 0; i < rowName.length; i++) {
			XSSFCell cell = row.createCell(i);// 创建该行的单元格
			// cell.setCellType(XSSFCell.CELL_TYPE_STRING); //设置类型 为字符串
			// cell.setEncoding(); // 设置编码
			cell.setCellValue(rowName[i]);// 设置值
			cell.setCellStyle(cellStyle);// 应用格式
		}
		for (int i = 0; i < dataList.size(); i++) {
			if (dataList.get(i) != null && !"".equals(dataList.get(i))) {
				// 创建第i+1行
				XSSFRow rowi = sheet.createRow(i + 1);
				for (int j = 0; j < cellName.length; j++) {
					XSSFCell cell = rowi.createCell(j);// 创建该行的单元格
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					// cell.setEncoding(HSSFCell.); // 设置编码
					cell.setCellStyle(cellStyle);// 应用格式
					String str = null;
					try {
						str = getProperty((Map) dataList.get(i), cellName[j]);
						if (str == null) {
							str = "";
							//log.info("object="+object);
						}
					} catch (Exception e) {
						log.info("该对象中不存在对应属性!");
						throw new RuntimeException("该对象中不存在对应属性!", e);
					}
					cell.setCellValue(str);// 设置值
				}
			}
		}
		log.info("excel对象创建成功！");
		return workbook;
	}

	
	/**
	 * 
	 * @Description (得到某个对象的属性值)
	 * @param obj  对象
	 * @param fieldName  属性名
	 * @return Object 属性值
	 * @date 2015-10-13下午2:14:22
	 * @author qiufh
	 */
	private static String getProperty(Map map, String fieldName) {
		if(map.get(fieldName) == null || "".equals(map.get(fieldName))){
			return "";
		}		
		return map.get(fieldName).toString();
//		Class<? extends Object> ownerClass = obj.getClass();
//		fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
//		
//		Object property = null;
//		try {
//			property = ownerClass.getMethod("get" + fieldName).invoke(obj);
//		} catch (Exception e) {
//			log.info("反射获取字段值失败");
//			throw new RuntimeException("反射获取字段值失败", e);
//		}
//		return property;
	}

	// 读取Excel表格表头的内容
	/*public static List<Attribute> readExcelTitle(String excelPath, List<Attribute> attributeList) {
		List<Attribute> titleList = new ArrayList<Attribute>();
		try {
			log.info("开始导入表头信息");
			// excelPath,Excel 文件 的绝对路径
			input = new FileInputStream(new File(excelPath));
			wb = new XSSFWorkbook(input);
			sheet = wb.getSheetAt(0);
			// 得到标题的内容对象。
			row = sheet.getRow(0);
			// 得到标题总列数
			int colNum = row.getPhysicalNumberOfCells();
			for (int i = 0; i < colNum; i++) {
				String cellValue = getStringCellValue(row.getCell(i));
				for (Attribute attribute : attributeList) {
					if (attribute.getcName().equals(cellValue)) {
						titleList.add(attribute);
					}
				}
			}
			log.info("导入表头信息成功");
		} catch (FileNotFoundException e) {
			log.info("导入表头信息失败, 错误信息：" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return titleList;
	}*/

	/**
	 * 
	 * @Description (从Excel导出数据)
	 * @param excelPath  Excel文件路径
	 * @param excelTitle  Excel列表头
	 * @param clazzName  设定文件
	 * @return List<Object>  返回类型
	 * @date 2015-10-13下午2:18:11
	 * @author qiufh
	 */
	/*public static List<Object> readExcelContent(String excelPath, List<Attribute> excelTitle, String clazzName) {
		// 读取Excel数据内容
		List<Object> contentList = new ArrayList<Object>();
		// excel 内容
		try {
			log.info("开始导入Excel内容信息!");
			input = new FileInputStream(new File(excelPath));
			wb = new XSSFWorkbook(input);
			sheet = wb.getSheetAt(0);
			// 得到总行数
			int rowNum = sheet.getLastRowNum();
			// 得到标题的内容对象。
			XSSFRow rowTitle = sheet.getRow(0);
			// 得到每行的列数。
			int colNum = rowTitle.getPhysicalNumberOfCells();
			Object object = null;
			Class<? extends Object> ownerClass = null;
			Object newInstance = null;
	
			for (int r = 1; r <= rowNum; r++) {
				// 正文内容应该从第二行开始,第一行为表头的标题
				row = sheet.getRow(r);
				int c = 0;
				ownerClass = Class.forName(clazzName);
				newInstance = ownerClass.newInstance();
				//log.info("开始将Excel表格的第"+(r+1)+"行值封装成对象，并校验!");
				while (c < colNum) {// colNum
					String strValue = getStringCellValue(rowTitle.getCell(c));
					for (Attribute attribute : excelTitle) {
						if (attribute.getcName().equals(strValue)) {
							String str = getStringCellValue(row.getCell(c)).trim();
							object = setProperty(str, attribute, ownerClass, newInstance, r, c);//r表示行号，c表示第列号
						}
					}
					c++;
				}
				//log.info("成功将Excel表格的第"+(r+1)+"行值封装成对象，并校验!");
				contentList.add(object);
			}
			log.info("导入Excel内容成功");
		} catch (Exception e) {
			log.info("导入Excel内容失败, 错误信息：" + e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return contentList;
	}*/

	// 获取单元格数据内容为字符串类型的数据
	private static String getStringCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
		String strCell = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING: // 字符串格式
			strCell = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC: // 数字格式
			if (DateUtil.isCellDateFormatted(cell)) {// 日期格式
				strCell = DateUtils.dateToString(cell.getDateCellValue(), "yyyy-MM-dd HH:mm:ss");
			} else {// 数字格式
				double value = cell.getNumericCellValue();
				String valueStr = String.valueOf(value);
				String valueStrEnd = valueStr.substring(valueStr.lastIndexOf(".") + 1, valueStr.length());
				if (valueStrEnd.matches("^[0]*$")) {
					strCell = String.valueOf((int) value);
				} else {
					strCell = valueStr;
				}
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN: // BOOLEAN格式
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		return strCell;
	}

	// 将属性值设置到对象中，并校验。
	/*private static Object setProperty(String str, Attribute attribute, Class<? extends Object> ownerClazz, Object newInstance, int row, int column) {
		try {
			String eName = attribute.geteName();
			if (StringUtils.isNotBlank(eName)) {
				eName = eName.substring(0, 1).toUpperCase() + eName.substring(1, eName.length());
				if ("Integer".equals(attribute.getType())) {
					if (str.length() <= Integer.parseInt(attribute.getLength())) {
						if ("notnull".equals(attribute.getIsempty())) {
							if (StringUtils.isBlank(str)) {
								log.info("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为非空字段!必填!");
								throw new RuntimeException("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为非空字段!必填!");
							} else {
								try {
									ownerClazz.getMethod("set" + eName, Integer.class).invoke(newInstance, Integer.parseInt(str));
								} catch (NumberFormatException e) {
									log.info("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为整型类型，实际类型错误(" + str + ")");
									throw new RuntimeException("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为整型类型，实际类型错误(" + str + ")");
								}
							}
						} else {
							if (StringUtils.isBlank(str)) {
								// ownerClazz.getMethod("set" + eName,
								// Integer.class).invoke(null);
							} else {
								try {
									ownerClazz.getMethod("set" + eName, Integer.class).invoke(newInstance, Integer.parseInt(str));
								} catch (NumberFormatException e) {
									log.info("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为整型类型，实际类型错误(" + str + ")");
									throw new RuntimeException("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为整型类型，实际类型错误(" + str + ")");
								}
							}
						}
					} else {
						log.info("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 字段长度为" + attribute.getLength() + ",超出该长度!");
						throw new RuntimeException("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 字段长度为" + attribute.getLength() + ",超出该长度!");
					}
				} else if ("Date".equals(attribute.getType())) {
					if (str.length() <= Integer.parseInt(attribute.getLength())) {
						Date stringToDate = null;
						if ("notnull".equals(attribute.getIsempty())) {
							if (StringUtils.isBlank(str)) {
								log.info("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为非空字段!必填!");
								throw new RuntimeException("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为非空字段!必填!");
							} else {
								stringToDate = cn.zeale.coas.util.DateUtil.stringToDate("yyyy-MM-dd HH:mm:ss", str);
							}
						} else {
							if (StringUtils.isBlank(str)) {
	
							} else {
								stringToDate = cn.zeale.coas.util.DateUtil.stringToDate("yyyy-MM-dd HH:mm:ss", str);
							}
						}
						ownerClazz.getMethod("set" + eName, Date.class).invoke(newInstance, stringToDate);
					} else {
						log.info("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 字段长度为" + attribute.getLength() + ",超出该长度!");
						throw new RuntimeException("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 字段长度为" + attribute.getLength() + ",超出该长度!");
					}
				} else if ("String".equals(attribute.getType())) {
					if (str.length() <= Integer.parseInt(attribute.getLength())) {
						if ("notnull".equals(attribute.getIsempty())) {
							if (StringUtils.isBlank(str)) {
								log.info("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为非空字段!必填!");
								throw new RuntimeException("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为非空字段!必填!");
							} else {
								ownerClazz.getMethod("set" + eName, String.class).invoke(newInstance, str);
							}
						} else {
							if (StringUtils.isBlank(str)) {
								ownerClazz.getMethod("set" + eName, String.class).invoke(newInstance, "");
							} else {
								ownerClazz.getMethod("set" + eName, String.class).invoke(newInstance, str);
							}
						}
					} else {
						log.info("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 字段长度为" + attribute.getLength() + ",超出该长度!");
						throw new RuntimeException("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 字段长度为" + attribute.getLength() + ",超出该长度!");
					}
				} else if ("Double".equals(attribute.getType())) {
					if (str.length() <= Integer.parseInt(attribute.getLength())) {
						if ("notnull".equals(attribute.getIsempty())) {
							if (StringUtils.isBlank(str)) {
								log.info("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + "为非空字段!必填!");
								throw new RuntimeException("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为非空字段!必填!");
							} else {
								String floatEnd = str.substring(str.lastIndexOf('.'), str.length());
								int floatLen = floatEnd.length();
								if (floatLen <= Integer.parseInt(attribute.getFloating())) {
									ownerClazz.getMethod("set" + eName, Double.class).invoke(newInstance, Double.parseDouble(str));
								}
							}
						} else {
							if (StringUtils.isBlank(str)) {
								// ownerClazz.getMethod("set" + eName,
								// Double.class).invoke(null);
							} else {
								String floatEnd = str.substring(str.lastIndexOf('.'), str.length());
								int floatLen = floatEnd.length();
								if (floatLen <= Integer.parseInt(attribute.getFloating())) {
									ownerClazz.getMethod("set" + eName, Double.class).invoke(newInstance, Double.parseDouble(str));
								}
							}
						}
					} else {
						log.info("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 字段长度为" + attribute.getLength() + ",超出该长度!");
						throw new RuntimeException("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 字段长度为" + attribute.getLength() + ",超出该长度!");
					}
				} else if ("Float".equals(attribute.getType())) {
					if (str.length() <= Integer.parseInt(attribute.getLength())) {
						if ("notnull".equals(attribute.getIsempty())) {
							if (StringUtils.isBlank(str)) {
								log.info("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为非空字段!必填!");
								throw new RuntimeException("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为非空字段!必填!");
							} else {
								String floatEnd = str.substring(str.lastIndexOf('.'), str.length());
								int floatLen = floatEnd.length();
								if (floatLen <= Integer.parseInt(attribute.getFloating())) {
									ownerClazz.getMethod("set" + eName, Float.class).invoke(newInstance, Float.parseFloat(str));
								}
							}
						} else {
							if (StringUtils.isBlank(str)) {
								// ownerClazz.getMethod("set" + eName,
								// Float.class).invoke(null);
							} else {
								String floatEnd = str.substring(str.lastIndexOf('.'), str.length());
								int floatLen = floatEnd.length();
								if (floatLen <= Integer.parseInt(attribute.getFloating())) {
									ownerClazz.getMethod("set" + eName, Float.class).invoke(newInstance, Float.parseFloat(str));
								}
							}
						}
					} else {
						log.info("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 字段长度为" + attribute.getLength() + ",超出该长度!");
						throw new RuntimeException("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 字段长度为" + attribute.getLength() + ",超出该长度!");
					}
				} else if ("Character".equals(attribute.getType())) {
					if (str.length() <= Integer.parseInt(attribute.getLength())) {
						if ("notnull".equals(attribute.getIsempty())) {
							if (StringUtils.isBlank(str)) {
								log.info("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为非空字段!必填!");
								throw new RuntimeException("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 为非空字段!必填!");
							} else {
								ownerClazz.getMethod("set" + eName, Character.class).invoke(newInstance, str.charAt(0));
							}
						} else {
							if (StringUtils.isBlank(str)) {
								ownerClazz.getMethod("set" + eName, Character.class).invoke(' ');
							} else {
								ownerClazz.getMethod("set" + eName, Character.class).invoke(newInstance, str.charAt(0));
							}
						}
					} else {
						log.info("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 字段长度为" + attribute.getLength() + ",超出该长度!");
						throw new RuntimeException("第" + (row + 1) + "行第" + (column + 1) + "列," + attribute.geteName() + " 字段长度为" + attribute.getLength() + ",超出该长度!");
					}
				}
			}// ...
		} catch (ParseException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return newInstance;
	}*/

	/**
	 * 
	 * @Description (将Excel保存到本地磁盘)
	 * @param wb
	 * @param xlsName void
	 * @date 2015-10-19下午3:06:00
	 * @author qiufh
	 */
	public static void writeLocal(XSSFWorkbook wb, String realPath, String xlsName) {
		log.info("将Excel保存到服务器");
		// 初始化变量
		FileOutputStream fileOut = null;
		try {
			File targetFile = new File(realPath, xlsName);
			File file = new File(realPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			if (!targetFile.exists()) {
				targetFile.createNewFile();//创建文件
			}

			xlsName = URLDecoder.decode(xlsName, "UTF-8");
			fileOut = new FileOutputStream(realPath + xlsName);
		} catch (Exception e) {
			// 抛出异常
			throw new RuntimeException("将Excel表保存到服务器失败,错误消息： " + e.getMessage(), e);
		} finally {
			try {
				if (wb != null)
					wb.write(fileOut);
			} catch (IOException e) {
				wb = null;
			} finally {
				try {
					if (fileOut != null)
						fileOut.close();
				} catch (IOException e) {
					fileOut = null;
				}
			}
		}
	}

	/**
	 * 
	 * @Description (下载本地Excel到客户端)
	 * @param response
	 * @param realPath   保存路径
	 * @param xlsName   xls名称
	 *   void
	 * @date 2015-10-22上午10:14:46
	 * @author qiufh
	 */
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
			throw new RuntimeException("下载服务器Excel到客户端失败,错误消息： " + e.getMessage(), e);
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

	/**创建文件夹
	 * @param dir
	 */
	public static void makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}

	// 根据文件名读取excel文件
	public static List<Map<String, Object>> read(String fileName) {
		List<Map<String, Object>> dataset = null;
		// 检查文件名是否为空或者是否是Excel格式的文件
		if (fileName == null || !fileName.matches("^.+\\.(?i)((xls)|(xlsx)|(csv))$")) {
			return dataset;
		}
		boolean isExcel2003 = true;
		// 对文件的合法性进行验
		if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
			isExcel2003 = false;
		}
		// 检查文件是否存在
		File file = new File(fileName);
		if (file == null || !file.exists()) {
			return dataset;
		}
		try {
			// 读取excel
			dataset = read(new FileInputStream(file), isExcel2003);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dataset;
	}

	// 根据流读取Excel文件
	private static List<Map<String, Object>> read(InputStream inputStream, boolean isExcel2003) {
		List<Map<String, Object>> dataset = null;
		try {
			// 根据版本选择创建Workbook的方式
			Workbook wb = isExcel2003 ? new HSSFWorkbook(inputStream) : new XSSFWorkbook(inputStream);
			dataset = read(wb);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataset;
	}

	// 读取数据
	private static List<Map<String, Object>> read(Workbook wb) {
		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> dataLst = null;
		Map<String, Object> dataMap = null;
		int numberOfSheets = wb.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			//Sheet sheetAt = wb.getSheetAt(i);
			Sheet sheet = wb.getSheetAt(i);// 得到第一个shell
			String sheetName = sheet.getSheetName();
			Integer totalRows = sheet.getLastRowNum();//总行数,获取的是最后一行的编号（编号从0开始）。
			//int totalRow = sheet.getPhysicalNumberOfRows();//总行数,获取的是物理行数，也就是不包括那些空行（隔行）的情况。
			if (sheet.getPhysicalNumberOfRows() > 0 && sheet.getRow(0) != null) {
				List<?> readExcelTitles = readExcelTitle(wb, i);
				dataMap = new LinkedHashMap<String, Object>();
				dataLst = new ArrayList<Map<String, Object>>();
				int totalCells = sheet.getRow(0).getLastCellNum();//.getPhysicalNumberOfCells();//总列数
				// 循环Excel的行
				for (int r = 1; r <= totalRows; r++) {
					Row row = sheet.getRow(r);//拿到行
					if (row == null) {
						continue;
					}
					ArrayList<String> rowLst = new ArrayList<String>();
					Map<String, Object> linkedHasMap = new LinkedHashMap();
					// 循环Excel的列
					for (short c = 0; c < totalCells; c++) {
						Cell cell = row.getCell(c);
						String cellValue = "";
						if (cell == null) {
							rowLst.add(cellValue);
							continue;
						}
						cellValue = getStringCellValue(cell);
						//rowLst.add(cellValue);
						linkedHasMap.put((String) readExcelTitles.get(c), cellValue);
					}
					dataLst.add(linkedHasMap);
				}
				//dataset.add(dataLst);
				dataMap.put("sheetName", sheetName);
				dataMap.put("dataset", dataLst);
				dataset.add(dataMap);
			}
		}
		return dataset;
	}

	// 读取Excel表格表头的内容
	public static List<?> readExcelTitle(Workbook wb, int numberOfSheet) {
		List titleList = new ArrayList();
		log.info("开始导入表头信息");
		Sheet sheet = wb.getSheetAt(numberOfSheet);
		// 得到标题的内容对象。
		row = (XSSFRow) sheet.getRow(0);
		// 得到标题总列数
		int colNum = row.getLastCellNum();//getPhysicalNumberOfCells();
		for (int i = 0; i < colNum; i++) {
			String cellValue = getStringCellValue(row.getCell(i));
			titleList.add(cellValue);
		}
		log.info("导入表头信息结束");
		return titleList;
	}

	public static List<Map<String, Object>> readTxt(String filePath, List<String> titles) throws IOException {
		List<Map<String, Object>> txtList = new ArrayList();
		String tempStr = null;
		@SuppressWarnings("resource")
		BufferedReader readere = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8"));
		int i = 0;
		HashMap<String, Object> hashMap = null;
		while ((tempStr = readere.readLine()) != null) {
			String[] str = tempStr.split(",");
			hashMap = new HashMap<String, Object>();
			for (int j = 0; j < str.length; j++) {
				hashMap.put(titles.get(j), str[j]);
			}
			txtList.add(hashMap);
			i++;
		}
		return txtList;
	}
}
