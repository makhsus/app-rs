package com.rs.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "occupation")
public class Occupation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="occupation_id")
	private Long occupationId;
	
	@Column(name="occupation_code", length=20, nullable=false, unique=true)
	private String occupationCode;
	
	@Column(name="occupation_name", length=100, nullable=false)
	private String occupationName;

	
	public Long getOccupationId() {
		return occupationId;
	}

	public void setOccupationId(Long occupationId) {
		this.occupationId = occupationId;
	}

	public String getOccupationName() {
		return occupationName;
	}

	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
	}
	
	public String getOccupationCode() {
		return occupationCode;
	}
	
	public void setOccupationCode(String occupationCode) {
		this.occupationCode = occupationCode;
	}
	
}
