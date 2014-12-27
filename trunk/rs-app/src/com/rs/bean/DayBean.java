package com.rs.bean;

public class DayBean {

	private int dayNumber;
	private String dayName;
	
	public DayBean() {
		// TODO Auto-generated constructor stub
	}
	
	public DayBean(int dayNumber, String dayName) {
		this.dayNumber = dayNumber;
		this.dayName = dayName;
	}
	
	public int getDayNumber() {
		return dayNumber;
	}
	public void setDayNumber(int dayNumber) {
		this.dayNumber = dayNumber;
	}
	public String getDayName() {
		return dayName;
	}
	public void setDayName(String dayName) {
		this.dayName = dayName;
	}
	
}
