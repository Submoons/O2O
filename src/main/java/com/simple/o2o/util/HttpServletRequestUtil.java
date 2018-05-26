package com.simple.o2o.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理request的参数工具类
 * @author simple
 *
 */
public class HttpServletRequestUtil {
	/**
	 * 把key转化为整型
	 * @param request
	 * @param key
	 * @return
	 */
	public static int getInt(HttpServletRequest request, String key){
		try{
			return Integer.decode(request.getParameter(key));
		}catch(Exception e){
			return -1;
		}
	}
	
	public static long getLong(HttpServletRequest request, String key){
		try{
			return Long.valueOf(request.getParameter(key));
		}catch(Exception e){
			return -1;
		}
	}
	
	public static Double getDouble(HttpServletRequest request, String key){
		try{
			return Double.valueOf(request.getParameter(key));
		}catch(Exception e){
			return -1d;
		}
	}
	
	public static boolean getBoolean(HttpServletRequest request, String key){
		try{
			return Boolean.valueOf(request.getParameter(key));
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 转化为String类型
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getString(HttpServletRequest request, String key){
		try{
			String result = request.getParameter(key);
			if(result != null){
				result = result.trim();
			}
			if("".equals(result)){
				return null;
			}
			return result;
		}catch(Exception e){
			return null;
		}
	}
}
