package com.mr.nanke.utils;

import javax.servlet.http.HttpServletRequest;

// 验证码校验工具
public class CodeUtil {
	public static boolean checkVerifyCode(HttpServletRequest request) {
		// 图片里的验证码
		String verifyCodeExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		// 输入的验证码
		String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
		//校验
		if(verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)) {
			return false;
		}
		return true;
	}
}
