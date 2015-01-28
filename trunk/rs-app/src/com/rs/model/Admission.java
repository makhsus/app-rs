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

@Entity
@Table(name = "admission")
public class Admission implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="adm_id")
	private Long admId;
	
	@Column(name="category", length=50, nullable = false)
	private String category;
	
	@Column(name="registration_no", unique=true, nullable=false)
	private String registrationNo;
	
	@Column(name="sequence_no", length=6)
	private int sequenceNo;
	
	@ManyToOne
	@JoinColumn(name="patient", nullable=false)
	private Patient patient;
	
	@ManyToOne
	@JoinColumn(name="poly", nullable=false)
	private Polyclinic poly;
	
	@ManyToOne
	@JoinColumn(name="doctor", nullable=false)
	private Employee doctor;
	
	@Temporal(TemporalType.DATE)
	@Column(name="adm_date", nullable=false)
	private Date admDate;
	
	@Temporal(TemporalType.TIME)
	@Column(name="adm_start_time")
	private Date admStartTime;
	
	@Temporal(TemporalType.TIME)
	@Column(name="adm_end_time")
	private Date admEndTime;
	
	@ManyToOne
	@JoinColumn(name="created_by", nullable=false)
	private Users createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date", nullable = false)
	private Date createdDate;

	
	
	
	public Long getAdmId() {
		return admId;
	}

	public void setAdmId(Long admId) {
		this.admId = admId;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public Polyclinic getPoly() {
		return poly;
	}
	
	public void setPoly(Polyclinic poly) {
		this.poly = poly;
	}

	public Employee getDoctor() {
		return doctor;
	}

	public void setDoctor(Employee doctor) {
		this.doctor = doctor;
	}

	public Date getAdmDate() {
		return admDate;
	}

	public void setAdmDate(Date admDate) {
		this.admDate = admDate;
	}

	public Date getAdmStartTime() {
		return admStartTime;
	}

	public void setAdmStartTime(Date admStartTime) {
		this.admStartTime = admStartTime;
	}

	public Date getAdmEndTime() {
		return admEndTime;
	}

	public void setAdmEndTime(Date admEndTime) {
		this.admEndTime = admEndTime;
	}

	public Users getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Users createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	
}
