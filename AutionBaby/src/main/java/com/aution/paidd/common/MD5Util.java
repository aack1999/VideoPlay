package com.aution.paidd.common;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 描述 MD5加密
 * 
 * @author caoxiongmin
 * 
 */
public class MD5Util {

	private static final String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String encode(String password) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] byteArray = md5.digest(password.getBytes("utf-8"));
			String passwordMD5 = byteArrayToHexString(byteArray);
			return passwordMD5;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}

	private static String byteArrayToHexString(byte[] byteArray) {
		StringBuffer sb = new StringBuffer();
		for (byte b : byteArray) {
			sb.append(byteToHexChar(b));
		}
		return sb.toString();
	}

	private static Object byteToHexChar(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hex[d1] + hex[d2];
	}

	/**
	 * 获取字符串的MD5
	 *
	 * @param string
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getStringMD5(String string) {
		byte[] byteString = string.getBytes(Charset.forName("utf-8"));
		MessageDigest md = null;
		StringBuffer sb= new StringBuffer();
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(byteString);
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

}