package com.rs.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rs.bean.DayBean;

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
	
	public static List<DayBean> daysList(){
		List<DayBean> list = new ArrayList<>();
		list.add(new DayBean(1, "Senin"));
		list.add(new DayBean(2, "Selasa"));
		list.add(new DayBean(3, "Rabu"));
		list.add(new DayBean(4, "Kamis"));
		list.add(new DayBean(5, "Jumat"));
		list.add(new DayBean(6, "Sabtu"));
		list.add(new DayBean(7, "Minggu"));
		return list;
	}
	
	public static String daysConvertNumberToName(String daysNumber){
		StringBuffer buffer = new StringBuffer();
		String[] pisah = daysNumber.split("-");
		for(int i=0;i<pisah.length;i++){
			String str = pisah[i];
			String rplce = "";
			if(str.equalsIgnoreCase("1")){
				rplce = "Senin";
			}else if(str.equalsIgnoreCase("2")){
				rplce = "Selasa";
			}else if(str.equalsIgnoreCase("3")){
				rplce = "Rabu";
			}else if(str.equalsIgnoreCase("4")){
				rplce = "Kamis";
			}else if(str.equalsIgnoreCase("5")){
				rplce = "Jumat";
			}else if(str.equalsIgnoreCase("6")){
				rplce = "Sabtu";
			}else if(str.equalsIgnoreCase("7")){
				rplce = "Minggu";
			}
			
			if(i==(pisah.length-1)){
				buffer.append(rplce);
			}else{
				buffer.append(rplce+", ");
			}
		}
		return buffer.toString().trim();
	}
	
	public static String dateFormat(Date date, String pattern){
		String result = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		result = sdf.format(date);
		
		return result;
	}
}
