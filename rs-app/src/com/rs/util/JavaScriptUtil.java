package com.rs.util;

public class JavaScriptUtil {

	public static String shakeWindow(String window){
		String jqCommand = "var l = jq(\"$"+window+"\").position().left;"
				+ "jq(\"$"+window+"\").animate({"
				+ "left : l - 25"
				+ "}, 50).animate({"
				+ "backgroundColor : \"red\""
				+ "}, 50).animate({"
				+ "left : l"
				+ "}, 50).animate({"
				+ "left : l + 25"
				+ "}, 50).animate({"
				+ "left : l"
				+ "}, 50).animate({"
				+ "backgroundColor : \"white\""
				+ "}, 50);"
				+ "jq(\"$"+window+"\").css('background-color','transparent');";
		
		return jqCommand;
	}
	
}
