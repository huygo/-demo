package cn.easier.brow.comm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {
	/**
	 * 日期格式包含年、月、日、小时、分钟、秒,如：2003-10-01 10:20:15
	 */
	public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	protected final static String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss:SSS";

	/** yyyy-MM-dd HH:mm:ss */
	public final static String DATA_FORMAT1 = "yyyy-MM-dd HH:mm:ss";

	/** MMDDHHMMSS */
	public final static String DATA_FORMAT5 = "MMddHHmmss";

	public final static String DATA_FORMAT4 = "HH:mm:ss";

	/** yyyy-MM-dd */
	public final static String DATA_FORMAT2 = "yyyy-MM-dd";
	/** yyyy-MM */
	public final static String DATA_FORMAT6 = "yyyy-MM";
	/** yyyy-MM-dd HH:mm */
	public final static String DATA_FORMAT3 = "yyyy-MM-dd HH:mm";
	/** yyyy-MM-dd HH:mm:ss:sss */
	public final static String DATA_FORMATSS = "yyyy-MM-dd HH:mm:ss:sss";

	/**
	 * 将字符型数字转为Date类型
	 * 
	 * @param value
	 *            要转换的字符
	 * @return Date类型的日期
	 * @throws ParseException
	 */
	public static java.util.Date stringToDate(String dateString) throws ParseException {
		return stringToDate(dateString, YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * 将字符型数字转为Date类型
	 * 
	 * @param value
	 *            要转换的字符
	 * @return Date类型的日期
	 * @throws ParseException
	 */
	public static java.util.Date stringToDate(String dateString, String dateFormat) {
		if (dateString != null && dateString.trim().length() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
			try {
				Date date = sdf.parse(dateString);
				return date;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 把包含日期值转换为字符串
	 * 
	 * @param date
	 *            日期（日期+时间）
	 * @param pattern
	 *            输出类型
	 * @return 字符串
	 */
	public static String DateTimeToString(java.util.Date date, String pattern) {
		String DateString = "";
		if (date == null) {
			DateString = "";
		} else {
			SimpleDateFormat formatDate = new SimpleDateFormat(pattern, Locale.getDefault());
			DateString = formatDate.format(date);
		}
		return DateString;
	}

	/***************************************************************************
	 * The number of milliseconds in a minute.
	 */
	private final static long MS_IN_MINUTE = 60 * 1000;

	/***************************************************************************
	 * The number of milliseconds in an hour.
	 */
	private final static long MS_IN_HOUR = 60 * 60 * 1000;

	// 将时间字符串转换成ISO 8601 格式
	public static String isoFormat(long inputDateLong) {
		String dateString = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date inputDate = sdf.parse(sdf.format(new Date(inputDateLong)));
			SimpleDateFormat isoFormat_ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			dateString = isoFormat_.format(inputDate);
			TimeZone tz = isoFormat_.getTimeZone();
			String tzName = tz.getDisplayName();
			if (tzName.equals("Greenwich Mean Time")) {
				dateString = dateString.concat("Z");
			} else {
				long tzOffsetMS = tz.getRawOffset();
				long tzOffsetHH = tzOffsetMS / MS_IN_HOUR;
				if (tz.inDaylightTime(inputDate)) {
					tzOffsetHH = tzOffsetHH + 1;
				}
				String hourString = String.valueOf(Math.abs(tzOffsetHH));
				if (hourString.length() == 1) {
					hourString = "0" + hourString;
				}
				long tzOffsetMMMS = tzOffsetMS % MS_IN_HOUR;
				long tzOffsetMM = 0;
				if (tzOffsetMMMS != 0) {
					tzOffsetMM = tzOffsetMMMS / MS_IN_MINUTE;
				}
				String minuteString = String.valueOf(tzOffsetMM);
				if (minuteString.length() == 1) {
					minuteString = "0" + minuteString;
				}
				String sign = "+";
				if (String.valueOf(tzOffsetMS).indexOf("+") != -1) {
					sign = "-";
				}
				dateString = dateString.concat(sign + hourString + ":" + minuteString);
			}
		} catch (java.text.ParseException e) {

		}
		return dateString;
	}

	/*
	 * 将格林尼治时间转成时间戳
	 */
	public static long toLong(Date date) throws ParseException {
		long a = date.getTime();
		return a;
	}

	// 将UTC时间转换成本地时间戳
	public static long UtcToLocalTimestamp(Date date) {
		// 1、取得本地时间：
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 2、取得时间偏移量：
		final int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		// 3、取得夏令时差：
		final int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		// 4、从本地时间里加上这些差量，即可以取得UTC时间：
		cal.add(java.util.Calendar.MILLISECOND, (zoneOffset + dstOffset));
		return cal.getTimeInMillis();
	}

	// 将本地时间根据时区转换为UTC时间
	public static Date getUTC(Date date) {
		// 1、取得本地时间：
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 2、取得时间偏移量：
		final int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		// 3、取得夏令时差：
		final int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		return cal.getTime();
	}

	// 将本地时间根据时区转换为UTC时间
	public static Date getUTC(Long l) {
		// 1、取得本地时间：
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(l);
		// 2、取得时间偏移量：
		final int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		// 3、取得夏令时差：
		final int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		return cal.getTime();
	}

	/**
	 * 时间2014-06-13+10:59:16:943 转时间截
	 * 
	 * @param time
	 * @return
	 */
	public static String getCurrentTimeMillis(String time) {
		long r_time = 0;
		if (time != null && !"".equals(time)) {
			SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
			try {
				r_time = sdf.parse(time).getTime();
			} catch (ParseException e) {
				return null;
			}
		}
		if (r_time != 0) {
			return String.valueOf(r_time);
		}
		return null;
	}

	/**
	 * 时间截转时间2014-06-13+10:59:16:943
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateString(String date) {
		String order_time = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
			Date d = new Date(Long.valueOf(date));
			order_time = sdf.format(d);
		} catch (Exception e) {
			return null;
		}
		return order_time;
	}

	public static String getDate(String date) {
		String order_time = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
			Date d = new Date(Long.valueOf(date));
			order_time = sdf.format(d);
		} catch (Exception e) {
			return null;
		}
		return order_time;
	}

	/**
	 * 获取每日0点的时间戳
	 * 
	 * @return
	 */
	public static Long getStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	/**
	 * 得到某时间day天前（后）的时间
	 * 
	 * @param d
	 *            某时间点
	 * @param day
	 *            整数为后day天，负数为前day天
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/************
	 * 
	 * 描述 得到某时间day天前（后）的时间
	 * 
	 * @param startTime
	 * @param day
	 * @return
	 */
	public static String getDateAfter(String startTime, int day) {
		String endTime = "";
		SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT1);
		Date dateStart;
		try {
			dateStart = sdf.parse(startTime);
			Calendar now = Calendar.getInstance();
			now.setTime(dateStart);
			now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
			endTime = sdf.format(now.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return endTime;
	}

	/************
	 * 
	 * 描述 得到某时间day天前（后）的时间
	 * 
	 * @param startTime
	 * @param day
	 * @param format
	 *            yyyy-MM-dd HH:mm:ss等
	 * @return
	 */
	public static String getDateAfter(String startTime, int day, String format) {
		String endTime = "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateStart;
		try {
			dateStart = sdf.parse(startTime);
			Calendar now = Calendar.getInstance();
			now.setTime(dateStart);
			now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
			endTime = sdf.format(now.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return endTime;
	}

	/**
	 * 比较时间大小 格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param startTime
	 * @param EndTime
	 * @return int: 1|-1|0 参数说明：为0的时候两个时间相等 -1的时候c1小于c2 1的时候c1大于c2
	 */
	public static int getDateCompareTo(String startTime, String EndTime, String dateType) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateType);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(sdf.parse(startTime));
			c2.setTime(sdf.parse(EndTime));
		} catch (java.text.ParseException e) {
			System.err.println("比较时间大小出错！");
		}
		int result = c1.compareTo(c2);
		return result;
	}

	public static Date parse(String str, String format) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		boolean giveFormat = true;
		if (StringUtils.isEmpty(format)) {
			giveFormat = false;
			format = "yyyy-MM-dd";
		}

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.parse(str);
		} catch (ParseException e) {
			if (!giveFormat) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(format + " HH:mm:ss");
				try {
					return dateFormat.parse(str);
				} catch (ParseException e1) {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * 日期转化为字符串
	 * 
	 * @param date
	 * @param DataFormat
	 *            要转化后的字符串格式 如：yyyy-MM-dd HH:mm:ss
	 * @return String
	 */
	public static String dateToString(Date date, String DataFormat) {
		SimpleDateFormat sdf = null;
		if (!StringUtils.isNotEmpty(DataFormat)) {
			sdf = new SimpleDateFormat(DATA_FORMAT1);
		} else {
			sdf = new SimpleDateFormat(DataFormat);
		}
		return sdf.format(date);
	}

	/**
	 * 得到当前指定格式的时间
	 * 
	 * @param DataFormat
	 *            要转化后的字符串格式 如：yyyy-MM-dd HH:mm:ss
	 * @return String
	 * @throws ParseException
	 */
	public static Date getCurrentDate(String DataFormat) {
		SimpleDateFormat sdf = null;
		if (StringUtils.isEmpty(DataFormat)) {
			sdf = new SimpleDateFormat(DATA_FORMAT1);
		} else {
			sdf = new SimpleDateFormat(DataFormat);
		}
		Date returnDate = new Date();
		try {
			returnDate = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			throw new RuntimeException("获取当前时间失败: " + e);
		}
		return returnDate;
	}

	/**
	 * 当前时间的String类型[yyyy-MM-dd HH:mm:ss]
	 * 
	 * @return
	 */
	public static String stringDate() {
		return dateToString(new Date(), DATA_FORMAT1);
	}

	/**************
	 * 得到二个日期间的间隔天数(字符串格式 如：yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param sj1
	 *            大的
	 * @param sj2
	 *            小的
	 * @return
	 */
	public static long getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat(DATA_FORMAT1);
		long day = 0;
		try {
			java.util.Date date = myFormatter.parse(sj1);
			java.util.Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return day;
		}
		return day;
	}

	/**************
	 * 得到二个日期间的间隔天数(字符串格式 如：yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param sj1
	 *            大的
	 * @param sj2
	 *            小的
	 * @return
	 */
	public static long getTwoDay(String sj1, String sj2, String format) {
		SimpleDateFormat myFormatter = new SimpleDateFormat(format);
		long day = 0;
		try {
			java.util.Date date = myFormatter.parse(sj1);
			java.util.Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return day;
		}
		return day;
	}

	/***
	 * 获取当前日期的各个部分 DateType:传入的时间类型（year:返回年 month：返回月份 date：返回日 hour：返回小时
	 * minute：返回分钟 second：返回秒）
	 * 
	 * @return
	 */
	public static String getDatePart(String DateType) {
		Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
		if (DateType.equals("year")) {
			return c.get(Calendar.YEAR) + "";
		}
		if (DateType.equals("month")) {
			int month = c.get(Calendar.MONTH) + 1;
			if (month < 10) {
				return "0" + month + "";
			} else {
				return month + "";
			}
		}
		if (DateType.equals("date")) {
			return c.get(Calendar.DATE) + "";
		}
		if (DateType.equals("hour")) {
			return c.get(Calendar.HOUR_OF_DAY) + "";
		}
		if (DateType.equals("minute")) {
			return c.get(Calendar.MINUTE) + "";
		}
		if (DateType.equals("second")) {
			return c.get(Calendar.SECOND) + "";
		}
		return stringDate(); // 否则返回当前系统时间
	}

	/***
	 * 获取当前日期的各个部分 DateType:传入的时间类型（year:返回年 month：返回月份 date：返回日 hour：返回小时
	 * minute：返回分钟 second：返回秒）
	 * 
	 * @return
	 */
	public static String getDatePart2(String DateType) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT2);
		String date = sdf.format(new Date());// 可以对每个时间域单独修改
		if (DateType.equals("year")) {
			return new String(date.substring(0, 4));
		}
		if (DateType.equals("month")) {
			return new String(date.substring(5, 7));
		}
		if (DateType.equals("date")) {
			return new String(date.substring(8));
		}
		return stringDate(); // 否则返回当前系统时间
	}

	public static String getDatePartzh() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT2);
		String date = sdf.format(new Date());// 可以对每个时间域单独修改
		String d = date.substring(0, 4) + "年" + date.substring(5, 7) + "月" + date.substring(8) + "日";
		return d;
	}

	/***
	 * 获取当前日期的各个部分 DateType:传入的时间类型（year:返回年 month：返回月份 date：返回日 hh：返回小时
	 * min：返回分钟 sec：返回秒）
	 * 
	 * @return
	 */
	public static String getDatePart3(String formatdate, String DateType) {
		String format = DATA_FORMAT1;
		if (formatdate.length() == 16) {
			format = DATA_FORMAT3;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String date = sdf.format(stringToDate(formatdate, format));// 可以对每个时间域单独修改
		if (DateType.equals("year")) {
			return date.substring(0, 4);
		}
		if (DateType.equals("month")) {
			return date.substring(5, 7);
		}
		if (DateType.equals("date")) {
			return date.substring(8, 10);
		}
		if (DateType.equals("hh")) {
			return date.substring(11, 13);
		}
		if (DateType.equals("min")) {
			return date.substring(14, 16);
		}
		if (DateType.equals("sec")) {
			return date.substring(17, 19);
		}
		if (DateType.equals("beforepart")) {
			return date.substring(0, 10);
		}
		if (DateType.equals("afterpart")) {
			return date.substring(11);
		}
		return stringDate(); // 否则返回当前系统时间
	}

	/**
	 * 方法说明：时间戳转为String日期
	 * 
	 * @param times
	 *            时间戳
	 * @param fm
	 *            转换格式
	 * @return
	 * @author Ou 2014-5-6
	 */
	public static String timesToStr(long times, String fm) {
		String str = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(fm);
			str = sdf.format(new Date(times));
		} catch (Exception e) {
			throw new RuntimeException("时间戳转为String日期出错。");
		}
		return str;
	}

	/**
	 * 
	 * @Description (判断当前日期与时间date的大小(如果当前日期 等于此 Date，则返回值 0；如果当前日期 在 Date
	 *              参数之前(即当前日期小于date)，则返回小于 0 的值；如果当前日期 在 Date
	 *              参数之后(即当前日期大于date)，则返回大于 0 的值。异常则返回值 为0。))
	 * @param aMask
	 *            日期格式 :列2005-4-21
	 * @param date
	 *            时间
	 * @return Integer
	 * @date 2015-8-21下午3:15:03
	 * @author qiufahong
	 */
	public static Integer isDateCompareTo(String aMask, String strDate) {
		try {
			SimpleDateFormat df = null;
			df = new SimpleDateFormat(aMask);
			Date date1 = new Date();
			String strDate1 = df.format(date1);
			// String strDate = df.format(date);
			return df.parse(strDate1).compareTo(df.parse(strDate));
		} catch (ParseException e) {
			System.out.print("[SYS] " + e.getMessage());
			return 0;
		}
	}

	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/**
	 * 
	 * @Description (某一个月第一天和最后一天)
	 * @param strDate
	 *            (最少精确到月，格式：2015-10)
	 * @return Map<String,String>
	 * @date 2015-10-28上午11:42:58
	 * @author qiufh
	 */
	public static Map<String, String> getFirstday_Lastday_Month(String strDate) {
		String[] split = strDate.split("-");
		if (split.length <= 2) {
			strDate = strDate + "-01";
		}
		SimpleDateFormat df = new SimpleDateFormat(DATA_FORMAT2);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(stringToDate(strDate, DATA_FORMAT2));
		// calendar.add(Calendar.MONTH, -1); //减1上个月
		Date theDate = calendar.getTime();
		// 某月第一天
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String day_first = df.format(gcLast.getTime());
		StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
		day_first = str.toString();
		// 某月最后一天
		calendar.add(Calendar.MONTH, 1); // 加一个月
		calendar.set(Calendar.DATE, 1); // 设置为该月第一天
		calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
		String day_last = df.format(calendar.getTime());
		StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59:59");
		day_last = endStr.toString();
		Map<String, String> map = new HashMap<String, String>();
		map.put("first", day_first);
		map.put("last", day_last);
		return map;
	}

	/**
	 * 获取当前北京时间
	 * 
	 * @return
	 */
	public static String getBeijinTime(String dateF) {
		java.util.Locale locale = java.util.Locale.CHINA; // 这是获得本地中国时区
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(dateF, locale);// 设定日期格式
		java.util.Date date = new java.util.Date();
		java.net.URLConnection uc = null;
		try {
			java.net.URL url = new java.net.URL("http://www.bjtime.cn");// 取得资源对象
			uc = url.openConnection();// 生成连接对象
			uc.connect(); // 发出连接
		} catch (Exception e) {
			System.out.println("获取北京时间出错！" + e);
		}
		long ld = uc.getDate(); // 取得网站日期时间
		date = new Date(ld); // 转换为标准时间对象
		String bjTime = df.format(date);
		return bjTime;
	}

	/**
	 * 获取当前北京时间（时间戳）
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static Long getBeijinMill(String dateF) throws ParseException {
		String bjTime = getBeijinTime(dateF);
		Long result = stringToDate(bjTime, dateF).getTime();
		return result;
	}

	/**
	 * 
	 * @Description (得到某时间minute分钟前（后）的时间)
	 * @param date
	 * @param minute
	 * @return String minute为正整数date为minute分钟后，minute为负整数date为minute分钟前
	 * @date 2015-11-2下午5:17:17
	 * @author qiufh
	 */
	public static String getBeforeOneHourToNowDate(Date date, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		/* HOUR_OF_DAY 指示一天中的小时 */
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minute);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(calendar.getTime());
	}

	public static void main(String[] args) throws ParseException {
		// System.out.println(isDateCompareTo("yyyy-MM-dd", "2015-08-15
		// 20:30"));
		// System.out.println(toLong(getCurrentDate(DATA_FORMAT2)) ==
		// toLong(stringToDate("2015-09-25 14:30:02",DATA_FORMAT2)));
		/*
		 * Date date = getCurrentDate(DATA_FORMAT2); long currentLong =
		 * toLong(date); long long1 = toLong(stringToDate("2015-09-25",
		 * DATA_FORMAT2)); long long2 = toLong(stringToDate("2015-12-31",
		 * DATA_FORMAT2)); if (currentLong >= long1 && currentLong <= long2) {
		 * String weekOfDate = getWeekOfDate(date); if
		 * ("星期五".equals(weekOfDate)) { Long currentHHmm =
		 * toLong(getCurrentDate("HH:mm")); long long3 =
		 * toLong(stringToDate("11:30", "HH:mm")); long long4 =
		 * toLong(stringToDate("12:30", "HH:mm")); long long5 =
		 * toLong(stringToDate("16:00", "HH:mm")); long long6 =
		 * toLong(stringToDate("19:00", "HH:mm")); if ((currentHHmm >= long3 &&
		 * currentHHmm <= long4) || (currentHHmm >= long5 && currentHHmm <=
		 * long6)) { System.out.println("开始抢票"); } else {
		 * System.out.println("时间还没到!"); } } else {
		 * System.out.println("时间还没到!"); } } else {
		 * System.out.println("时间还没到!"); }
		 */
		// Date currentDate = getCurrentDate(DATA_FORMAT1);
		// int minute = 1;
		// System.out.println(dateToString(currentDate, DATA_FORMAT1) + " " +
		// minute + "分钟后: " + getBeforeOneHourToNowDate(currentDate, 1));
		// String strDate = getBeijinTime(DATA_FORMAT1);
		/*
		 * System.out.println("当前北京时间： " + strDate); System.out.println(
		 * "当前北京时间时间戳： " + getBeijinMill(DATA_FORMAT1)); Map<String, String> map
		 * = getFirstday_Lastday_Month(strDate); System.out.println(strDate +
		 * ",月的第一天：" + map.get("first")); System.out.println(strDate +
		 * ",月的最后一天：" + map.get("last")); System.out.println(strDate + "的7天前： "
		 * + getDateAfter(strDate, -7, DATA_FORMAT2));
		 * System.out.println(strDate + "的30天前： " + getDateAfter(strDate, -30,
		 * DATA_FORMAT2));
		 */

		System.out.println(isDateCompareTo("yyyy-MM-dd", "2015-11-10"));
	}

}
