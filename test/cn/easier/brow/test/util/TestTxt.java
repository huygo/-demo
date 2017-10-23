package cn.easier.brow.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestTxt {

	
	@Test
	public void test1() throws IOException{
		String temp = "";
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		FileOutputStream fos = null;
		PrintWriter pw = null;
		List<Integer> list = new ArrayList<>();
		list.add(100);
		list.add(101);
		list.add(102);
		list.add(103);
		try {
			File targetFile = new File("D:Dest.txt", "");
			File file = new File("D:Dest.txt");
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
			buf.append(list.get(a).toString());
			}
			fos = new FileOutputStream(targetFile);
			pw = new PrintWriter(fos);
			pw.write(buf.toString().toCharArray());
			pw.flush();

		} catch (Exception e) {
			// 抛出异常
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
	}


