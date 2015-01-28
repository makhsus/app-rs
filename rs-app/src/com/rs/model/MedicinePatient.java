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
@Table(name = "medicine_patient")
public class MedicinePatient implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="mp_id")
	private Long medicamentPatientId;
	
	@ManyToOne
	@JoinColumn(name="medical_record", nullable=false)
	private MedicalRecords medicalRecord;
	
	@ManyToOne
	@JoinColumn(name="medicine", nullable=false)
	private Medicine medicine;
	
	@Column(name="medicine_name")
	private String medicineName;
	
	@Column(name="medicine_code")
	private String medicineCode;
	
	@Column(name="medicine_unit")
	private String medicineUnit;
	
	@Column(name="medicine_price")
	private BigDecimal medicinePrice;
	
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

	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public String getMedicineName() {
		return medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	public String getMedicineCode() {
		return medicineCode;
	}

	public void setMedicineCode(String medicineCode) {
		this.medicineCode = medicineCode;
	}

	public String getMedicineUnit() {
		return medicineUnit;
	}

	public void setMedicineUnit(String medicineUnit) {
		this.medicineUnit = medicineUnit;
	}

	public BigDecimal getMedicinePrice() {
		return medicinePrice;
	}

	public void setMedicinePrice(BigDecimal medicinePrice) {
		this.medicinePrice = medicinePrice;
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
