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
@Table(name = "medical_records")
public class MedicalRecords implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="medical_records_id")
	private Long medicalRecordsId;
	
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
	@Column(name="admisi_date", nullable=false)
	private Date admisiDate;
	
	@Column(name="practice_time", length=20)
	private String practiceTime;
	
	@Column(name="record_status", length=100)
	private String recordStatus;
	
	@Type(type="text")
	@Column(name="allergy")
	private String allergy;
	
	@Type(type="text")
	@Column(name="anamnesis")
	private String anamnesis;
	
	@Type(type="text")
	@Column(name="diagnosis")
	private String diagnosis;
	
	@Type(type="text")
	@Column(name="action_plan")
	private String actionPlan;

	@Type(type="text")
	@Column(name="prescription")
	private String prescription;
	
	@ManyToOne
	@JoinColumn(name="created_by", nullable=false)
	private Users createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date", nullable = false)
	private Date createdDate;

	
	
	
	public Long getMedicalRecordsId() {
		return medicalRecordsId;
	}

	public void setMedicalRecordsId(Long medicalRecordsId) {
		this.medicalRecordsId = medicalRecordsId;
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

	public String getPracticeTime() {
		return practiceTime;
	}

	public void setPracticeTime(String practiceTime) {
		this.practiceTime = practiceTime;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getAllergy() {
		return allergy;
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

	public String getAnamnesis() {
		return anamnesis;
	}

	public void setAnamnesis(String anamnesis) {
		this.anamnesis = anamnesis;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getActionPlan() {
		return actionPlan;
	}

	public void setActionPlan(String actionPlan) {
		this.actionPlan = actionPlan;
	}

	public String getPrescription() {
		return prescription;
	}

	public void setPrescription(String prescription) {
		this.prescription = prescription;
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
