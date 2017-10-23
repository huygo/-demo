package cn.easier.brow.comm.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

/**
 * 安全编码组件
 * 
 * <pre>
 * 支持 DES、DESede(TripleDES,就是3DES)、AES、Blowfish、RC2、RC4(ARCFOUR) 
 * DES                  key size must be equal to 56 
 * DESede(TripleDES)    key size must be equal to 112 or 168 
 * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available 
 * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive) 
 * RC2                  key size must be between 40 and 1024 bits 
 * RC4(ARCFOUR)         key size must be between 40 and 1024 bits 
 * 具体内容 需要关注 JDK Document http://.../docs/technotes/guides/security/SunProviders.html
 * </pre>
 * 
 * @author 
 * @version 1.0
 * @since 1.0
 */
public class CoderUtil {
	private static Logger log = Logger.getLogger(CoderUtil.class);

	/**
	 * @Title: getMD5
	 * @Description: (MD5加密静态方法)
	 * @param @param source
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @author
	 */
	public static String getMD5(String source) {
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };// 用来将字节转换成16进制表示的字符
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(source.getBytes());
			// MD5 的计算结果是一个 128 位的长整数，
			byte tmp[] = md.digest();
			// 用字节表示就是 16 个字节
			// 每个字节用 16 进制表示的话，使用两个字符， 所以表示成 16
			char str[] = new char[16 * 2];
			// 进制需要 32 个字符
			// 表示转换结果中对应的字符位置
			int k = 0;
			// 从第一个字节开始，对 MD5 的每一个字节// 转换成 16
			for (int i = 0; i < 16; i++) {
				// 进制字符的转换
				// 取第 i 个字节
				byte byte0 = tmp[i];
				// 取字节中高 4 位的数字转换,// >>>
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				// 为逻辑右移，将符号位一起右移
				// 取字节中低 4 位的数字转换
				str[k++] = hexDigits[byte0 & 0xf];

			}
			// 换后的结果转换为字符串
			s = new String(str);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("MD5加密失败!", e);
		}
		return s;
	}

	/**
	 * 生成AES加密密钥(key)
	 * 
	 * @param str
	 * @return String
	 * @throws Exception
	 */
	public static String createAESKey(String str) {
		String s = null;
		char hexDigits[] = { '0', '1', 'A', '2', '3', '4', 'B', '5', 'C', '6', '7', 'D', '8', 'E', '9', 'F' };// 用来将字节转换成16进制表示的字符
		try {
			// str 转换成byte数组
			byte tmp[] = str.getBytes();
			// 用字节表示就是 16 个字节
			// 每个字节用 16 进制表示的话，使用两个字符， 所以表示成 16
			char strArr[] = new char[16];
			// 进制需要 32 个字符
			// 表示转换结果中对应的字符位置
			int k = 0;
			int j = 0;
			// 从第一个字节开始，对STR的每一个字节// 转换成 8
			for (int i = 0; i < 8; i++) {
				// 进制字符的转换
				// 取第 i 个字节
				if ((i + 1) >= tmp.length) {
					if ((i + 1) % tmp.length == 0) {
						j = 0;
					}
				}
				byte byte0 = tmp[j++];
				// 取字节中高 4 位的数字转换,// >>>
				strArr[k++] = hexDigits[byte0 >>> 4 & 0xf];
				// 为逻辑右移，将符号位一起右移
				// 取字节中低 4 位的数字转换
				strArr[k++] = hexDigits[byte0 & 0xf];

			}
			// 换后的结果转换为字符串
			s = new String(strArr);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("生成AES加密密钥!", e);
		}
		return s;
	}

	/**
	 * AES加密程序
	 * 
	 * @param sSrc
	 * @param sKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptAES(String sSrc, String sKey) throws Exception {
		if (sKey == null) {
			log.error("AES加密,Key为空null");
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			log.error("AES加密,Key长度不是16位");
			return null;
		}
		byte[] raw = sKey.getBytes("ASCII");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes());
		return byte2hex(encrypted).toLowerCase();
	}

	/**
	 * AES解密程序
	 * 
	 * @param sSrc
	 * @param sKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptAES(String sSrc, String sKey) throws Exception {
		try {
			// 判断Key是否正确
			if (sKey == null) {
				log.error("AES解密,Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (sKey.length() != 16) {
				log.error("AES解密,Key长度不是16位");
				return null;
			}
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = hex2byte(sSrc);
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
				System.out.println(e.toString());
				log.error("AES解密失败，消息：" + e.getMessage());
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			log.error("AES解密失败，消息：" + ex.getMessage());
			return null;
		}
	}

	/**
	 * SHA加密(SHA-256)
	 * 
	 * @param plainText
	 *            需要加密的字符串
	 * @param algorithm
	 *            加密长度.默认设置为 SHA-256
	 * @return
	 */
	public static String encryptSHA(String plainText, String algorithm)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(plainText.getBytes());
		byte[] b = md.digest();
		StringBuilder output = new StringBuilder(32);
		for (int i = 0; i < b.length; i++) {
			String temp = Integer.toHexString(b[i] & 0xff);
			if (temp.length() < 2) {
				output.append("0");
			}
			output.append(temp);
		}
		return output.toString();
	}

	/**
	 * 16进制字符串转byte数组
	 * 
	 * @param strhex
	 * @return
	 */
	public static byte[] hex2byte(String strhex) {
		if (strhex == null) {
			return null;
		}
		int len = strhex.length();
		if (len % 2 == 1) {
			return null;
		}
		byte[] b = new byte[len / 2];

		for (int i = 0; i != len / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
		}

		return b;
	}

	/**
	 * byte数组转16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	/**
	 * 创建密匙
	 * 
	 * @param algorithm
	 *            加密算法,可用 DES,DESede,Blowfish
	 * @return SecretKey 秘密（对称）密钥
	 */
	public static SecretKey createSecretKey(String algorithm) {
		// 声明KeyGenerator对象
		KeyGenerator keygen;
		// 声明 密钥对象
		SecretKey deskey = null;
		try {
			// 返回生成指定算法的秘密密钥的 KeyGenerator 对象
			keygen = KeyGenerator.getInstance(algorithm);
			// 生成一个密钥
			deskey = keygen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 返回密匙
		return deskey;
	}

	/**
	 * 指定字符串转换密钥
	 * 
	 * @param key
	 *            二进制密钥
	 * @param algorithm
	 *            加密算法,可用 DES,DESede,Blowfish
	 * @return 密钥
	 */
	private static SecretKey toKey(byte[] key, String algorithm) {
		// 生成密钥
		return new SecretKeySpec(key, algorithm);
	}

	/**
	 * 根据密匙进行DES加密
	 * 
	 * @param key
	 *            密匙
	 * @param info
	 *            要加密的信息
	 * @param algorithm
	 *            加密算法,可用  DES、DESede(TripleDES,就是3DES)、AES、Blowfish、RC2、RC4(ARCFOUR)
	 * @return String 加密后的信息
	 */
	public static String encryptDES(SecretKey key, String info, String algorithm) {
		// 加密随机数生成器 (RNG),(可以不写)
		SecureRandom sr = new SecureRandom();
		// 定义要生成的密文
		byte[] cipherByte = null;
		try {
			// 得到加密/解密器
			Cipher c1 = Cipher.getInstance(algorithm);
			// 用指定的密钥和模式初始化Cipher对象
			// 参数:(ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE,UNWRAP_MODE)
			c1.init(Cipher.ENCRYPT_MODE, key, sr);
			// 对要加密的内容进行编码处理,
			cipherByte = c1.doFinal(info.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回密文的十六进制形式
		return byte2hex(cipherByte);
	}

	/**
	 * 根据密匙进行DES加密,支持CBC模式、PKCS5（7）填充
	 * 
	 * @param key
	 *            密匙
	 * @param info
	 *            要加密的信息
	 * @param algorithm
	 *            加密算法/工作模式/填充方式,可用  DES、DESede(TripleDES,就是3DES)、AES、Blowfish、RC2、RC4(ARCFOUR)
	 * @param iv
	 *            向量，AES为16bytes. DES为8bytes.
	 * @return String 加密后的信息
	 */
	public static String encryptDES(SecretKey key, String info, String algorithm, String iv) {
		// 定义要生成的密文
		byte[] cipherByte = null;
		try {
			// 得到加密/解密器
			Cipher c1 = Cipher.getInstance(algorithm);
			// 用指定的密钥和模式初始化Cipher对象
			// 参数:(ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE,UNWRAP_MODE)
			IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());//iv
			c1.init(Cipher.ENCRYPT_MODE, key, ivSpec);
			// 对要加密的内容进行编码处理,
			cipherByte = c1.doFinal(info.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回密文的十六进制形式
		return byte2hex(cipherByte);
	}

	/**
	 * 根据密匙进行DES解密
	 * 
	 * @param key
	 *            密匙
	 * @param sInfo
	 *            要解密的密文
	 * @param algorithm
	 *            解密算法,可用 DES、DESede(TripleDES,就是3DES)、AES、Blowfish、RC2、RC4(ARCFOUR)
	 * @return String 返回解密后信息
	 */
	public static String decryptDES(SecretKey key, String sInfo, String algorithm) {
		// 加密随机数生成器 (RNG)
		SecureRandom sr = new SecureRandom();
		byte[] cipherByte = null;
		try {
			// 得到加密/解密器
			Cipher c1 = Cipher.getInstance(algorithm);
			// 用指定的密钥和模式初始化Cipher对象
			c1.init(Cipher.DECRYPT_MODE, key, sr);
			// 对要解密的内容进行编码处理
			cipherByte = c1.doFinal(hex2byte(sInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return byte2hex(cipherByte);
		return new String(cipherByte);
	}

	/**
	 * 根据密匙进行DES解密,支持CBC模式、PKCS5（7）填充
	 * 
	 * @param key
	 *            密匙
	 * @param sInfo
	 *            要解密的密文
	 * @param algorithm
	 *            加密算法/工作模式/填充方式,可用 DES、DESede(TripleDES,就是3DES)、AES、Blowfish、RC2、RC4(ARCFOUR) 
	 * @param iv
	 *            向量，AES为16bytes. DES为8bytes.
	 * @return String 返回解密后信息
	 */
	public static String decryptDES(SecretKey key, String sInfo, String algorithm, String iv) {
		byte[] cipherByte = null;
		try {
			// 得到加密/解密器
			Cipher c1 = Cipher.getInstance(algorithm);
			IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());//iv
			// 用指定的密钥和模式初始化Cipher对象
			c1.init(Cipher.DECRYPT_MODE, key, ivSpec);
			// 对要解密的内容进行编码处理
			cipherByte = c1.doFinal(hex2byte(sInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return byte2hex(cipherByte);
		return new String(cipherByte);
	}

	/***
	 * 获取对称数
	 * 
	 * @param s
	 * @return
	 */
	public static String getSymmetrical(String s) {
		int len = s.length();
		String str = "";
		for (int i = 0; i < len / 2; i++) {
			if (s.charAt(i) == s.charAt(len - i - 1)) {
				str = str + s.charAt(i);
				if (str.length() == 16) {
					break;
				}
			}

		}
		return str;
	}

	/**
	 * Base64混编码
	 * 
	 * @param base64Str
	 * @return
	 */
	public static String encryptBASE64(String base64Str) {
		StringBuffer bsNewStr = new StringBuffer();
		for (int i = 0; i < base64Str.length() - 2; i++) {
			bsNewStr.append(base64Str.charAt(i)).append(StringUtil.getRandomString(1));
		}
		bsNewStr.append(base64Str.charAt(base64Str.length() - 2)).append(base64Str.charAt(base64Str.length() - 1));
		return bsNewStr.toString();
	}

	/**
	 * Base64混编码解码
	 * 
	 * @param base64Str
	 * @return
	 */
	public static String decryptBASE64(String base64Str) {
		StringBuffer bsNewStr = new StringBuffer();
		for (int i = 0; i < base64Str.length() - 2; i++) {
			if (i % 2 == 0) {
				bsNewStr.append(base64Str.charAt(i));
			}
		}
		bsNewStr.append(base64Str.charAt(base64Str.length() - 2)).append(base64Str.charAt(base64Str.length() - 1));
		return new String(bsNewStr);
	}

	public static void main(String[] args) throws Exception {
		String algorithmKey = "TripleDES"; // 算法
		String algorithmCoder = "TripleDES/CBC/PKCS5Padding"; // 算法/模式/填充方式
		String iv = "abcdrfgh"; //向量
		String inputStr = "sid=222;mobile=13333333333"; //待加密字符串
		//SecretKey key = createSecretKey(algorithmKey); //系统生成key
		String keyStr = "f5s8a5s8df555sa#_fs!654.";//指定key
		SecretKey key = toKey(keyStr.getBytes(), algorithmKey);
		System.err.println("原文:\t" + inputStr);

		System.err.println("算法:\t" + key.getAlgorithm());
		System.err.println("密钥key:\t" + byte2hex(key.getEncoded()));

		inputStr = encryptDES(key, inputStr, algorithmCoder, iv);

		inputStr = encryptBASE64(inputStr);
		inputStr = URLEncoder.encode(inputStr, "UTF-8");
		System.err.println("加密后:\t" + inputStr);

		String outputStr = URLDecoder.decode((decryptBASE64(inputStr)), "UTF-8");
		outputStr = decryptDES(key, outputStr, algorithmCoder, iv);

		System.err.println("解密后:\t" + outputStr);
	}
}
