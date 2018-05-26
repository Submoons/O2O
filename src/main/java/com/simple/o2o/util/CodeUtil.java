package com.simple.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
	/**
	 * 验证验证码是否正确
	 * @param request
	 * @return
	 */
	public static boolean checkVerifyCode(HttpServletRequest request){
		String verifyCodeExcepted = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
		if(verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExcepted)){
			return false;
		}
		return true;
	}
}
