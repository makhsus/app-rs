package com.rs.util;

public class CommonUtil {

	public static final String SESSION_FACTORY = "S_F";
	public static final String LOGIN_USER = "login_user";
	
	
	public static String neatString(String source){
		StringBuffer buffer = new StringBuffer();
		String[] strArr = source.split(" ");
		for(int i=0;i<strArr.length;i++){
			String str = strArr[i];
			buffer.append(str.substring(0, 1).toUpperCase()+str.substring(1)+" ");
		}
		return buffer.toString().trim();
	}
}
