package cn.easier.brow.comm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

public class ConverterCharacterSetUtil {

	public static String converterToUtf8(HttpServletRequest req) throws UnsupportedEncodingException, IOException {
		String str = null;
		StringBuilder buffer = null;
		buffer = new StringBuilder();
		BufferedReader reader = null;
		reader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
		String line = null;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		str = buffer.toString();

		return str;
	}

}
