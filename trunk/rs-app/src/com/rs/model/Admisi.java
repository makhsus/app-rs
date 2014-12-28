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
@Table(name = "admisi")
public class Admisi implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="admisi_id")
	private Long admisiId;
	
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
	@Column(name="admisi_date", nullable=false)
	private Date admisiDate;
	
	@Temporal(TemporalType.TIME)
	@Column(name="admisi_start_time")
	private Date admisiStartTime;
	
	@Temporal(TemporalType.TIME)
	@Column(name="admisi_end_time")
	private Date admisiEndTime;
	
	@ManyToOne
	@JoinColumn(name="created_by", nullable=false)
	private Users createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date", nullable = false)
	private Date createdDate;

	
	
	
	public Long getAdmisiId() {
		return admisiId;
	}

	public void setAdmisiId(Long admisiId) {
		this.admisiId = admisiId;
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

	public Date getAdmisiDate() {
		return admisiDate;
	}

	public void setAdmisiDate(Date admisiDate) {
		this.admisiDate = admisiDate;
	}

	public Date getAdmisiStartTime() {
		return admisiStartTime;
	}

	public void setAdmisiStartTime(Date admisiStartTime) {
		this.admisiStartTime = admisiStartTime;
	}

	public Date getAdmisiEndTime() {
		return admisiEndTime;
	}

	public void setAdmisiEndTime(Date admisiEndTime) {
		this.admisiEndTime = admisiEndTime;
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
	
}
