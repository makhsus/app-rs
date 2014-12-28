package com.rs.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.rs.bean.DayBean;
import com.rs.dao.UniqueNumberDao;
import com.rs.model.UniqueNumber;

public class CommonUtil {

	public static final String SESSION_FACTORY 				= "S_F";
	public static final String LOGIN_USER 					= "login_user";
	public static final String CODE_FOR_REG_RAWAT_JALAN		= "RRJ";
	public static final TimeZone JAKARTA_TIMEZONE			= TimeZone.getTimeZone("Asia/Jakarta");
	
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
	
	public static int dayNumberConvertFromJavaCalendar(){
		int result = 1;
		Calendar cal = Calendar.getInstance(JAKARTA_TIMEZONE);
		int dayJava = cal.get(Calendar.DAY_OF_WEEK);
		
		if(dayJava==1){result = 7;}
		else if(dayJava==2){result = 1;}
		else if(dayJava==3){result = 2;}
		else if(dayJava==4){result = 3;}
		else if(dayJava==5){result = 4;}
		else if(dayJava==6){result = 5;}
		else if(dayJava==7){result = 6;}
		
		return result;
	}
	
	/**
	 * Example: 19 to 000019
	 */
	public static String numberToString(int digitMax, int number){
		String result = "";
		
		String numberStr = Integer.toString(number);
		int numberLength = numberStr.length();
		
		StringBuffer buffer = new StringBuffer();
		int addedZero = digitMax-numberLength;
		for(int i=0;i<addedZero;i++){
			buffer.append("0");
		}
		
		result = buffer.toString()+number;
		
		/*if(number>100000){
			result = ""+number;
		}else{
			if(number>10000){
				result = "0"+number;
			}else{
				if(number>1000){
					result = "00"+number;
				}else{
					if(number>100){
						result = "000"+number;
					}else{
						if(number>10){
							result = "0000"+number;
						}else{
							result = "00000"+number;
						}
					}
				}
			}
		}*/
		
		return result;
	}
	
	private static String bulanHurufDuaDigit(int bulan){
		String result = "";	
		if(bulan>9){result = ""+bulan;}
		else{result = "0"+bulan;}
		return result;
	}
	
	public static UniqueNumber generateUniqueNumber(SessionFactory sessionFactory, String code){
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);
		
		SessionFactory _sessionFactory = sessionFactory;
		if(sessionFactory==null){
			_sessionFactory = HibernateUtil.getSessionFactory();
		}
		
		UniqueNumberDao dao = new UniqueNumberDao();
		dao.setSessionFactory(_sessionFactory);
		
		Criterion cr1 = Restrictions.eq("code", code);
		Criterion cr2 = Restrictions.eq("month", month);
		Criterion cr3 = Restrictions.eq("year", year);
		List<UniqueNumber> list = dao.loadBy(Order.desc("uniqueNumber"), cr1, cr2, cr3);
		
		UniqueNumber un = null;
		int number = 1;
		if(list.size()>0){
			un = list.get(0);
			number = un.getNumber()+1;
		}else{
			un = new UniqueNumber();
			un.setCode(code);
			un.setMonth(month);
			un.setYear(year);
		}
		
		String uniqueNumber = code+year+bulanHurufDuaDigit(month)+numberToString(6, number);
		
		un.setNumber(number);
		un.setUniqueNumber(uniqueNumber);
		
		return un;
	}
}
