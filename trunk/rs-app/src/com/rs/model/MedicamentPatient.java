package com.rs.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "medicament_patient")
public class MedicamentPatient implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="mp_id")
	private Long medicamentPatientId;
	
	@ManyToOne
	@JoinColumn(name="medical_record", nullable=false)
	private MedicalRecords medicalRecord;
	
	@ManyToOne
	@JoinColumn(name="medicament", nullable=false)
	private Medicament medicament;
	
	@Column(name="medicament_name")
	private String medicamentName;
	
	@Column(name="medicament_code")
	private String medicamentCode;
	
	@Column(name="medicament_unit")
	private String medicamentUnit;
	
	@Column(name="medicament_price")
	private BigDecimal medicamentPrice;
	
	@Column(name="quantity", nullable=false)
	private Integer quantity;
	
	@Column(name="total_price")
	private BigDecimal totalPrice;
	
	@ManyToOne
	@JoinColumn(name="created_by", nullable=false)
	private Users createdBy;
	
	@Column(name="created_date", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	public Long getMedicamentPatientId() {
		return medicamentPatientId;
	}

	public void setMedicamentPatientId(Long medicamentPatientId) {
		this.medicamentPatientId = medicamentPatientId;
	}

	public MedicalRecords getMedicalRecord() {
		return medicalRecord;
	}

	public void setMedicalRecord(MedicalRecords medicalRecord) {
		this.medicalRecord = medicalRecord;
	}

	public Medicament getMedicament() {
		return medicament;
	}

	public void setMedicament(Medicament medicament) {
		this.medicament = medicament;
	}

	public String getMedicamentName() {
		return medicamentName;
	}

	public void setMedicamentName(String medicamentName) {
		this.medicamentName = medicamentName;
	}

	public String getMedicamentCode() {
		return medicamentCode;
	}

	public void setMedicamentCode(String medicamentCode) {
		this.medicamentCode = medicamentCode;
	}

	public String getMedicamentUnit() {
		return medicamentUnit;
	}

	public void setMedicamentUnit(String medicamentUnit) {
		this.medicamentUnit = medicamentUnit;
	}

	public BigDecimal getMedicamentPrice() {
		return medicamentPrice;
	}

	public void setMedicamentPrice(BigDecimal medicamentPrice) {
		this.medicamentPrice = medicamentPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
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
