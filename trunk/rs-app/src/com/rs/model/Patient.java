package com.rs.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "patients")
public class Patient implements Serializable {

	private static final long serialVersionUID = 1829130580225664772L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_patient")
	private Long id;
	
	@Column(name="name", length=100, nullable = false)
	private String name;
	
	@Column(name="gender", length=1, nullable = false)
	private String gender;
	
	@Column(name="husband_name", length=100)
	private String husbandName;
	
	@Column(name="wife_name", length=100)
	private String wifeName;
	
	@Column(name="parent_name", length=100)
	private String parentName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="birthdate")
	private Date birthdate;
	
	@Column(name="religion", length=30)
	private String religion;
	
	@Column(name="address")
	private String address;
	
	@Column(name="last_study", length=15)
	private String lastStudy;
	
	@Column(name="occupation", length=30)
	private String occupation;
	
	@Column(name="phone", length=15)
	private String phone;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date", nullable=false)
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="card_no", length=30)
	private String cardNumber;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}
	
	public String getGenderString() {
		String genString = "";
		if (gender.equalsIgnoreCase("F")){
			genString = "Perempuan";
		}else{
			genString = "Laki - laki";
		}
			
		return genString;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHusbandName() {
		return husbandName;
	}

	public void setHusbandName(String husbandName) {
		this.husbandName = husbandName;
	}

	public String getWifeName() {
		return wifeName;
	}

	public void setWifeName(String wifeName) {
		this.wifeName = wifeName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLastStudy() {
		return lastStudy;
	}

	public void setLastStudy(String lastStudy) {
		this.lastStudy = lastStudy;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	
	
}
