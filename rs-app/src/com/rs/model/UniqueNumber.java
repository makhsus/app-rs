package com.rs.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "unique_number")
public class UniqueNumber implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="unique_number_id")
	private Long uniqueNumberId;
	
	@Column(name="unique_number", nullable=false)
	private String uniqueNumber;
	
	@Column(name="code_c", nullable=false)
	private String code;
	
	@Column(name="number_c", nullable=false)
	private int number;

	@Column(name="month_c", nullable=false)
	private int month;
	
	@Column(name="year_c", nullable=false)
	private int year;

	public Long getUniqueNumberId() {
		return uniqueNumberId;
	}

	public void setUniqueNumberId(Long uniqueNumberId) {
		this.uniqueNumberId = uniqueNumberId;
	}

	public String getUniqueNumber() {
		return uniqueNumber;
	}

	public void setUniqueNumber(String uniqueNumber) {
		this.uniqueNumber = uniqueNumber;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
