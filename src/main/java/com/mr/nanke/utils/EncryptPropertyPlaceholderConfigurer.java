package com.mr.nanke.utils;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/***
 * JDBC用户名及密码解密返回
 * @author 夏小颜
 *
 */
public class EncryptPropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer {
	//需要加密的数组
	private String[] encryptPropNames = { "jdbc.username", "jdbc.password" };

	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		//判断是否已经加密
		if (isEncryptProp(propertyName)) {
			//对已加密的进行解密工作
			String decryptValue = DESUtils.getDecryptString(propertyValue);
			return decryptValue;
		} else {
			return propertyValue;
		}
	}

	private boolean isEncryptProp(String propertyName) {
		//若等于需要加密的字段，则进行加密
		for (String encryptpropertyName : encryptPropNames) {
			if (encryptpropertyName.equals(propertyName))
				return true;
		}
		return false;
	}
}
