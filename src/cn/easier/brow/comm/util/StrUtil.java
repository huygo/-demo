package cn.easier.brow.comm.util;

public class StrUtil {

	public static String valueOf(Object obj) {

		String str = "";
		if (null != obj && !"".equals(obj) && !"null".equals(obj)) {
			str = String.valueOf(obj);
		}

		return str;
	}

	public static final int getLength(String str) {
		return str.trim().length();

	}

	public static final int countChar(String str, String c) {
		int num = 0;
		String[] s = str.split(c);
		if (s.length != 1) {
			num = s.length - 1;
		}
		return num;
	}

	public static final String html2txt(String str) {
		if (str == null || str.equals(""))
			return "";
		StringBuffer result = new StringBuffer();
		int numberOfChar = str.length();
		for (int i = 0; i < numberOfChar; i++) {
			char c = str.charAt(i);
			/* �� <br> &nbsp; <, > , & , @ , &trade; &quot; */
			switch (c) {
			case 10: // '\n'
			case 13: // '\r'
				result.append("<br>");
				break;

			case 32: // ' '
				result.append("&nbsp;");
				break;

			case 60: // '<'
				result.append("&lt;");
				break;

			case 62: // '>'
				result.append("&gt;");
				break;

			case 38: // '&'
				result.append("&amp");
				break;

			case 169:
				result.append("&copy;");
				break;

			case 174:
				result.append("&reg;");
				break;

			case 8482:
				result.append("&trade;");
				break;

			case 34: // '"'
				result.append("&quot;");
				break;

			default:
				result.append(c);
				break;
			}
		}

		return result.toString();
	}

	public static final String stoJSStr(String str) {
		if (str == null || str.equals("")) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			char c = str.charAt(i);
			switch (c) {
			case 12:
				sb.append("\\f");
				break;
			case 9:
				sb.append("\\t");
				break;
			case 8:
				sb.append("\\b");
				break;
			case 10:
				sb.append("\\n");
				break;
			case 13:
				sb.append("\\r");
				break;
			case 39:
				sb.append("\\\'");
				break;
			case 34:
				sb.append("\\\"");
				break;
			case 92:
				sb.append("\\\\");
				break;
			default:
				sb.append(c);
				break;
			}

		} // for end
		return sb.toString();
	}

	public static String toFormValue(String str) {
		if (str == null || str.equals("")) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			char c = str.charAt(i);
			switch (c) {
			case 62: // '>'
				sb.append("&gt;");
				break;
			case 60: // '<'
				sb.append("&lt;");
				break;
			case 38: // '&'
				sb.append("&amp;");
				break;
			case 34: // '"'
				sb.append("&quot;");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	public static String splitLongString(String str, int len) {
		if (str == null) {
			return null;
		}
		int strLen = str.length();
		StringBuffer sb = new StringBuffer();
		int k = 0;
		for (int i = 0; i < strLen; ++i) {
			char c = str.charAt(i);
			sb.append(c);
			if (++k > len - 1) {
				sb.append("\n");
				k = 0;
			}
		}
		return sb.toString();
	}

	public static final String produceFormatString(String str, int max) {
		if (str == null) {
			return "";
		}

		int strlen = str.length();
		if (max < strlen) {
			return str;
		}
		int diff = max - strlen;
		StringBuffer sb = new StringBuffer();
		int kongxiNum = strlen - 1;
		sb.append(str.charAt(0));

		int shang = (int) (diff * 2 / kongxiNum);
		for (int i = 0; i < kongxiNum; ++i) {
			for (int k = 0; k < shang; ++k) {
				sb.append("&nbsp;");
			}
			sb.append(str.charAt(i + 1));
		}
		return sb.toString();
	}

	public static final String toSqlStr(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		int strLen = str.length();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strLen; ++i) {
			char c = str.charAt(i);
			if (c == '\'') {
				sb.append("''");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static final String checkSqlInject(String str) {

		String inj_str = "-- exec select insert update delete count % chr mid master truncate char declare ; or - + ,";
		String[] inj_stra = inj_str.split(" ");
		for (int i = 0; i < inj_stra.length; i++) {
			if (str.equalsIgnoreCase(inj_stra[i])) {
				return "";
			}
		}
		return str;

	}

	public static void main(String[] args) {
		//String s = "a b<\'";
		// System.out.println(getLength(s));
		// System.out.println(countChar("123\"56", ""));
		// System.out.println(html2txt(s));
		// System.out.println(stoJSStr(s));
		// System.out.println(toFormValue(s));
		// System.out.println(splitLongString(s, 5));
		System.out.println(checkSqlInject("select * from tablename where username='ab'  1='1'"));
		// System.out.println(CoStr.produceFormatString("�ַ����", 10));

	}

}
