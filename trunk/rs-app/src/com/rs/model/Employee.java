package com.rs.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;


@Entity
@Table(name = "employee")
public class Employee implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_employee")
	private Long idEmployee;
	
	@Column(name="NIK", nullable = false, length=200, unique = true)
	private String nik;
	
	@Column(name="full_name", length=200, nullable = false)
	private String fullName;
	
	@Type(type="text")
	@Column(name="address", nullable = false)
	private String address;
	
	@Column(name="gender", length=1, nullable = false)
	private String gender;
	
	@Temporal(TemporalType.DATE)
	@Column(name="birthdate", nullable = false)
	private Date birthdate;
	
	@Column(name="status", length=1, nullable = false)
	private String status;
	
	@ManyToOne
	@JoinColumn(name="city_id", referencedColumnName="city_id", nullable=false)
	private City cityid;
	
	@ManyToOne
	@JoinColumn(name="province_id", referencedColumnName="province_id", nullable=false)
	private Province provinceId;
	
	@Column(name="email", length=100)
	private String email;
	
	@Column(name="phone_number", length=30)
	private String phoneNumber;
	
	@Column(name="identity_number", length=50, nullable = false)
	private String identityNumber;
	
	@Temporal(TemporalType.DATE)
	@Column(name="date_expired_identity_number", nullable = false)
	private Date dateExpiredIdentityNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="register_date", nullable = false)
	private Date registerDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

	@ManyToOne
	@JoinColumn(name="occupation")
	private Occupation occupationId;	
	
	@ManyToOne
	@JoinColumn(name="create_by")
	private Users createBy;
	
	@Column(name="village")
	private String village;
	
	@Column(name="religion", length=50)
	private String religion;
	
	@Column(name="blood_type", length=2)
	private String bloodType;
	
	@Column(name="marital_status", length=50)
	private String maritalStatus;
	
	@Column(name="education")
	private String education;
	
	@Column(name="major")
	private String major;
	
	public Long getIdEmployee() {
		return idEmployee;
	}

	public void setIdEmployee(Long idEmployee) {
		this.idEmployee = idEmployee;
	}

	public String getNik() {
		return nik;
	}

	public void setNik(String nik) {
		this.nik = nik;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public City getCityid() {
		return cityid;
	}

	public void setCityid(City cityid) {
		this.cityid = cityid;
	}

	public Province getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Province provinceId) {
		this.provinceId = provinceId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Occupation getOccupationId() {
		return occupationId;
	}

	public void setOccupationId(Occupation occupationId) {
		this.occupationId = occupationId;
	}

	public Users getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Users createBy) {
		this.createBy = createBy;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public Date getDateExpiredIdentityNumber() {
		return dateExpiredIdentityNumber;
	}

	public void setDateExpiredIdentityNumber(Date dateExpiredIdentityNumber) {
		this.dateExpiredIdentityNumber = dateExpiredIdentityNumber;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

}
