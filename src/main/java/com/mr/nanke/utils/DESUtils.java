package com.mr.nanke.utils;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * 
 * DES加密算法 ：是一种对称加密算法，所谓的对称加密算法即：加密和解密使用相同的秘钥的算法
 * @author 夏小颜
 *
 */
public class DESUtils {

	private static Key key;
	//设置秘钥KEY
	private static String KEY_STR = "myKey";
	private static String CHARSETNAME = "UTF-8";
	private static String ALGORITHM = "DES";

	static {
		try {
			//生成DES算法对象
			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
			//运用SHA1安全策略
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			//设置上秘钥种子
			secureRandom.setSeed(KEY_STR.getBytes());
			//初始化基于SHA1的算法对象
			generator.init(secureRandom);
			//生成秘钥对象
			key = generator.generateKey();
			generator = null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取加密信息进行加密
	 * @param str
	 * @return
	 */
	@SuppressWarnings("restriction")
	public static String getEncryptString(String str) {
		//基于BASE64Encoder编码，接受byte[]并转换成String
		BASE64Encoder base64encoder = new BASE64Encoder();
		try {
			//按UTF-8编码
			byte[] bytes = str.getBytes(CHARSETNAME);
			//获取加密对象
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			//初始化密码信息
			cipher.init(Cipher.ENCRYPT_MODE, key);
			//加密
			byte[] doFinal = cipher.doFinal(bytes);
			//加密后的转换String并返回
			return base64encoder.encode(doFinal);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取加密的信息进行获取解密后的信息
	 * @param str
	 * @return
	 */
	public static String getDecryptString(String str) {
		@SuppressWarnings("restriction")
		//基于BASE64Encoder编码，接受byte[]并转换成String
		BASE64Decoder base64decoder = new BASE64Decoder();
		try {
			@SuppressWarnings("restriction")
			byte[] bytes = base64decoder.decodeBuffer(str);
			//获取解密对象
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			//初始化解密信息
			cipher.init(Cipher.DECRYPT_MODE, key);
			//解密
			byte[] doFinal = cipher.doFinal(bytes);
			//返回解密的信息
			return new String(doFinal, CHARSETNAME);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getEncryptString("root"));
		System.out.println(getEncryptString("admin"));
		//System.out.println(getEncryptString("wxd7f6c5b8899fba83"));
		//System.out.println(getEncryptString("665ae80dba31fc91ab6191e7da4d676d"));
	}

}