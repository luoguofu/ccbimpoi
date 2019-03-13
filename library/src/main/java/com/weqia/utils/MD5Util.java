package com.weqia.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 生成一个MD5串
 * @author Berwin
 * @version 1.0
 */
public class MD5Util {

	/**
	 * 32 位标准 MD5 加密
	 * 
	 * @Title: md32
	 * @param plainText
	 * @return
	 * @return String
	 * @throws
	 */
	public static String md32(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();

		} catch (NoSuchAlgorithmException e) {
		    e.printStackTrace();
		}
		return null;
	}

	/**
	 * 16 位标准 MD5 加密
	 * 
	 * @Title: md16
	 * @param plainText
	 * @return
	 * @return String
	 * @throws
	 */
	public static String md16(String plainText) {
		String result = md32(plainText);
		if (result == null)
			return null;
		return result.toString().substring(8, 24);// 16位的加密
	}

	/**
	 * 可逆的加密算法
	 * 
	 * @Title: reversibleEncry
	 * @param inStr
	 * @return
	 * @return String
	 * @throws
	 */
	public static String reversibleEncry(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 'b');
		}
		String s = new String(a);
		return s;
	}

	public static void main(String args[]) {
		String s = new String("hello");
		System.out.println("原始：" + s);
		System.out.println("MD5后：" + md32(s));
		System.out.println("MD5后再加密：" + reversibleEncry(md32(s)));
		String string = reversibleEncry(md32(s));
		System.out.println("解密为MD5后的：" + reversibleEncry(string));
	}
}