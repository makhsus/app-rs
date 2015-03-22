package com.rs.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Makhsus Bilmajdi
 * 
 * 
 * Referensi:
 * 1. http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
 */
public class CalendarUtil {

	public static TimeZone tx = TimeZone.getTimeZone("Asia/Jakarta");
	
	
	public static Calendar calendarNow(){
		return Calendar.getInstance(tx);
	}
	
	public static Date dateNow(){
		return Calendar.getInstance(tx).getTime();
	}
	
	public static String formatDate(Date date, String pattern){
		String result = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		result = sdf.format(date);
		
		return result;
	}
	
	public static String dateIndonesia_DD_MMM_YYYY(Calendar cal){
		String result = "";
		
		int tahun = cal.get(Calendar.YEAR);
		int bulanIndex = cal.get(Calendar.MONTH);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		String tanggal = sdf.format(cal.getTime());
		
		String bulan = "";
		if(bulanIndex==0){bulan="Jan";}
		else if(bulanIndex==1){bulan="Feb";}
		else if(bulanIndex==2){bulan="Mar";}
		else if(bulanIndex==3){bulan="Apr";}
		else if(bulanIndex==4){bulan="Mei";}
		else if(bulanIndex==5){bulan="Jun";}
		else if(bulanIndex==6){bulan="Jul";}
		else if(bulanIndex==7){bulan="Agt";}
		else if(bulanIndex==8){bulan="Sep";}
		else if(bulanIndex==9){bulan="Okt";}
		else if(bulanIndex==10){bulan="Nov";}
		else if(bulanIndex==11){bulan="Des";}
		
		result = tanggal+"-"+bulan+"-"+tahun;
				
		return result;
	}
	
	public static String calendarIndonesia(Calendar cal){
		String result = "";
		
		int tahun = cal.get(Calendar.YEAR);
		int bulanIndex = cal.get(Calendar.MONTH);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		String tanggal = sdf.format(cal.getTime());
		
		int hariNumber = cal.get(Calendar.DAY_OF_WEEK);
		String hari = "";
		if(hariNumber==1){hari="Minggu";}
		else if(hariNumber==2){hari="Senin";}
		else if(hariNumber==3){hari="Selasa";}
		else if(hariNumber==4){hari="Rabu";}
		else if(hariNumber==5){hari="Kamis";}
		else if(hariNumber==6){hari="Jumat";}
		else if(hariNumber==7){hari="Sabtu";}
		
		String bulan = "";
		if(bulanIndex==0){bulan="Januari";}
		else if(bulanIndex==1){bulan="Februari";}
		else if(bulanIndex==2){bulan="Maret";}
		else if(bulanIndex==3){bulan="April";}
		else if(bulanIndex==4){bulan="Mei";}
		else if(bulanIndex==5){bulan="Juni";}
		else if(bulanIndex==6){bulan="Juli";}
		else if(bulanIndex==7){bulan="Agustus";}
		else if(bulanIndex==8){bulan="September";}
		else if(bulanIndex==9){bulan="Oktober";}
		else if(bulanIndex==10){bulan="Nov";}
		else if(bulanIndex==11){bulan="Des";}
		
		result = hari+", "+tanggal+" "+bulan+" "+tahun;
		
		return result;
	}
	
	
}
