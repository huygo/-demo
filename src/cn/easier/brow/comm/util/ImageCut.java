package cn.easier.brow.comm.util;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang3.StringUtils;

import sun.misc.BASE64Decoder;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@SuppressWarnings("rawtypes")
public class ImageCut {
	/**
	 * 
	 * @Description (根据尺寸图片居中裁剪)
	 * @param src   待裁剪图片
	 * @param dest  裁剪后保存路径（默认为源路径）
	 * @param w  图片宽度
	 * @param h  图片高度
	 * @return boolean
	 * @date 2015-11-4上午9:35:57
	 * @author qiufh
	 */
	public static boolean cutCenterImage(String src, String dest, int w, int h) {
		ImageInputStream iis = null;
		InputStream in = null;
		try {
			String picType = src.substring(src.lastIndexOf(".") + 1, src.length());
			Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
			ImageReader reader = (ImageReader) iterator.next();
			in = new FileInputStream(src);
			iis = ImageIO.createImageInputStream(in);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			int imageIndex = 0;
			Rectangle rect = new Rectangle((reader.getWidth(imageIndex) - w) / 2, (reader.getHeight(imageIndex) - h) / 2, w, h);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			if (StringUtils.isEmpty(dest)) {
				ImageIO.write(bi, picType, new File(src));
			} else {
				ImageIO.write(bi, picType, new File(dest));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (iis != null) {
						try {
							iis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @Description (图片裁剪二分之一)
	 * @param src  待裁剪图片
	 * @param dest  裁剪后保存路径（默认为源路径）
	 * @return boolean
	 * @date 2015-11-4上午9:34:45
	 * @author qiufh
	 */
	public static boolean cutHalfImage(String src, String dest) {
		InputStream in = null;
		ImageInputStream iis = null;
		try {
			String picType = src.substring(src.lastIndexOf(".") + 1, src.length());
			Iterator iterator = ImageIO.getImageReadersByFormatName(picType);
			ImageReader reader = (ImageReader) iterator.next();
			in = new FileInputStream(src);
			iis = ImageIO.createImageInputStream(in);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			int imageIndex = 0;
			int width = reader.getWidth(imageIndex) / 2;
			int height = reader.getHeight(imageIndex) / 2;
			Rectangle rect = new Rectangle(width / 2, height / 2, width, height);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			if (StringUtils.isEmpty(dest)) {
				ImageIO.write(bi, picType, new File(src));
			} else {
				ImageIO.write(bi, picType, new File(dest));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (iis != null) {
						try {
							iis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @Description (图片截取)
	 * @param src  待剪切图片路径
	 * @param dest 裁剪后保存路径（默认为源路径）
	 * @param x   裁剪起始横坐标
	 * @param y   裁剪起始纵坐标
	 * @param w   裁剪宽度
	 * @param h   裁剪高度 
	 * @return boolean
	 * @date 2015-11-4上午9:36:56
	 * @author qiufh
	 */
	public static boolean cutImage(String src, String dest, int x, int y, int w, int h) {
		InputStream in = null;
		ImageInputStream iis = null;
		try {
			String picType = src.substring(src.lastIndexOf(".") + 1, src.length());
			Iterator iterator = ImageIO.getImageReadersByFormatName(picType);
			ImageReader reader = (ImageReader) iterator.next();
			in = new FileInputStream(src);
			iis = ImageIO.createImageInputStream(in);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle rect = new Rectangle(x, y, w, h);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			if (StringUtils.isEmpty(dest)) {
				ImageIO.write(bi, picType, new File(src));
			} else {
				ImageIO.write(bi, picType, new File(dest));
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (iis != null) {
						try {
							iis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @Description (图片压缩)
	 * @param source  待压缩图片
	 * @param target  压缩后保存路径
	 * @param width   图片宽度
	 * @param height  图片高度
	 * @param percentage
	 * @return boolean
	 * @date 2015-11-4上午9:36:56
	 * @author qiufh
	 */
	public static boolean s_pic(String source, String target, int width, int height, boolean percentage) {
		FileOutputStream out = null;
		try {
			File srcfile = new File(source);
			BufferedImage src = ImageIO.read(srcfile);
			if (percentage) {
				double rate1 = ((double) src.getWidth(null)) / (double) width + 0.1;
				double rate2 = ((double) src.getHeight(null)) / (double) height + 0.1;
				double rate = rate1 > rate2 ? rate1 : rate2;
				width = (int) (((double) src.getWidth(null)) / rate);
				height = (int) (((double) src.getHeight(null)) / rate);
			}
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			image.getGraphics().drawImage(src.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, null);
			out = new FileOutputStream(target);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			image.flush();
			out.flush();
			src.flush();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @Description (Base64字符串转图片)
	 * @param base64Str
	 * @param imgFilePath
	 * @return boolean
	 * @date 2015-11-4上午9:40:03
	 * @author qiufh
	 */
	public static boolean base64ToImage(String base64Str, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] bytes = decoder.decodeBuffer(base64Str);
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @Description (获取图片类型)
	 * @param file  源文件
	 * @return String
	 * @date 2015-11-4上午9:39:14
	 * @author qiufh
	 */
	public static String getSuffix(File file) {
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(file);
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				return null;
			}
			ImageReader reader = iter.next();
			iis.close();
			return reader.getFormatName();
		} catch (IOException e) {
		}
		return null;
	}
}
